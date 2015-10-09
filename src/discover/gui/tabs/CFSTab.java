/**
 * @author Tony Pinkston
 */
package discover.gui.tabs;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import discover.common.Version;
import discover.gui.Utilities;
import discover.system.CaptureThread;
import discover.system.CaptureThreadListener;
import discover.system.Network;
import discover.vdis.PDU;
import discover.vdis.common.EntityId;
import discover.vdis.common.PDUHeader;
import discover.vdis.datum.AbstractDatumRecord;
import discover.vdis.datum.CommandFromSimulator;
import discover.vdis.datum.DatumSpecificationRecord;
import discover.vdis.enums.VDIS;
import discover.vdis.pdu.ActionRequest;
import discover.vdis.pdu.ActionResponse;
import discover.vdis.pdu.EntityState;
import discover.vdis.types.EntityType;
import discover.vdis.types.EntityTypes;

public class CFSTab
    extends Tab
    implements ActionListener, CaptureThreadListener, FocusListener {

    private static final DateFormat format = DateFormat.getDateTimeInstance();
    private static final int BASE_LENGTH = 64;

    private static final String TRANSLATE = "Translate Entity State PDUs";

    private enum State {

        /** Waiting for user to start (inputs enabled) */
        PENDING_START,

        /** Started, waiting for 1st entity state PDU on CFS port */
        PENDING_ENTITY,

        /** Sent CFS action request, waiting for response */
        PENDING_RESPONSE,

        /** Received CFS action response, translating entity state PDUs */
        PENDING_COMPLETE,

        /** Received CFS action response, not translating entity state PDUs */
        COMPLETE
    };

    private final JTextArea status = new JTextArea(18, 25);
    private final JCheckBox translate = new JCheckBox(TRANSLATE);
    private final JTextField marking = new JTextField();
    private final JButton start = new JButton();
    private final JButton stop = new JButton();
    private final JButton clear = new JButton("Clear");
    private final JFormattedTextField site = Utilities.getIntegerField(10);
    private final JFormattedTextField application = Utilities.getIntegerField(20);
    private final JFormattedTextField entity = Utilities.getIntegerField(30);
    private final JFormattedTextField cfsPort = Utilities.getIntegerField(3000);
    private final JFormattedTextField cfsExercise = Utilities.getIntegerField(1);
    private final JFormattedTextField safPort = Utilities.getIntegerField(4000);
    private final JFormattedTextField safExercise = Utilities.getIntegerField(2);

    private final Data data = new Data();
    private CaptureThread cfsCaptureThread = null;
    private CaptureThread safCaptureThread = null;
    private DatagramSocket socket = null;
    private State state = State.PENDING_START;
    private boolean updatingData = false;

    public CFSTab(String name) {

        super(name, TabType.CFS);

        this.configureWidgets();
        this.updateWidgets();
        this.setState(State.PENDING_START);
        this.fill();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        Object source = event.getSource();

        if (source == this.start) {

            this.start();
        }
        else if (source == this.stop) {

            this.stop();
        }
        else if (source == this.clear) {

            status.setText("");
        }
        else if (!this.updatingData) {

            this.updateData();
        }
    }

    @Override
    public void focusGained(FocusEvent event) {

        if (!this.updatingData) {

            this.updateData();
        }
    }

    @Override
    public void focusLost(FocusEvent event) {

        if (!this.updatingData) {

            this.updateData();
        }
    }

    @Override
    public void pdusCaptured(List<PDU> list) {

        EntityId entityId = new EntityId();

        for(final PDU pdu : list) {

            if (pdu.getType() == VDIS.PDU_TYPE_ENTITY_STATE) {

                pdu.getEntityId(entityId);

                if (pdu.getPort() == this.data.cfsPort) {

                    if (this.data.entityId.equals(entityId)) {

                        pdu.decode(false);
                        this.processEntityState(pdu);
                    }
                }
            }
            else if (pdu.getType() == VDIS.PDU_TYPE_ACTION_RESPONSE) {

                if (pdu.getPort() == this.data.safPort) {

                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {

                            if (state == State.PENDING_RESPONSE) {

                                processActionResponse(pdu);
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void load(Version version, DataInputStream stream)
        throws IOException {

        // Tab type and name should have already been read...
        this.data.entityId.setSite(stream.readUnsignedShort());
        this.data.entityId.setApplication(stream.readUnsignedShort());
        this.data.entityId.setEntity(stream.readUnsignedShort());
        this.data.protocol = stream.readByte();
        this.data.marking = stream.readUTF();
        this.data.requestId = stream.readInt();
        this.data.cfsPort = stream.readInt();
        this.data.cfsExercise = stream.readInt();
        this.data.safPort = stream.readInt();
        this.data.safExercise = stream.readInt();

        this.updateWidgets();
    }

    @Override
    public void save(DataOutputStream stream) throws IOException {

        stream.writeInt(super.getTabType().ordinal());
        stream.writeUTF(super.getTabName());
        stream.writeShort(this.data.entityId.getSite());
        stream.writeShort(this.data.entityId.getApplication());
        stream.writeShort(this.data.entityId.getEntity());
        stream.writeByte(this.data.protocol);
        stream.writeUTF(this.data.marking);
        stream.writeInt(this.data.requestId);
        stream.writeInt(this.data.cfsPort);
        stream.writeInt(this.data.cfsExercise);
        stream.writeInt(this.data.safPort);
        stream.writeInt(this.data.safExercise);
    }

    @Override
    public void close() {

        this.start.removeActionListener(this);
        this.stop.removeActionListener(this);
        this.clear.removeActionListener(this);
        this.cfsPort.removeActionListener(this);
        this.cfsPort.removeFocusListener(this);
        this.cfsExercise.removeActionListener(this);
        this.cfsExercise.removeFocusListener(this);
        this.safPort.removeActionListener(this);
        this.safPort.removeFocusListener(this);
        this.safExercise.removeActionListener(this);
        this.safExercise.removeFocusListener(this);
        this.marking.removeActionListener(this);
        this.marking.removeFocusListener(this);
        this.site.removeActionListener(this);
        this.site.removeFocusListener(this);
        this.application.removeActionListener(this);
        this.application.removeFocusListener(this);
        this.entity.removeActionListener(this);
        this.entity.removeFocusListener(this);

        this.stop();
    }

    private void processEntityState(final PDU pdu) {

        if (this.state == State.PENDING_COMPLETE) {

            // Forward entity state PDU from CFS node, whatever it may be,
            // to SAF node.
            byte buffer[] = Arrays.copyOf(pdu.getData(), pdu.getLength());

            // The 1st byte is the protocol.
            buffer[0] = (byte)this.data.protocol;

            // The 2nd byte is the exercise.
            buffer[1] = (byte)this.data.safExercise;

            // The 143th byte in the buffer should be 3rd byte of the 4-byte
            // entity capabilities record.  The zero bit denotes the entity
            // is "Task Organizable", this bit must be set.
            buffer[142] |= 0x01;

            try {

                this.sendBuffer(buffer, buffer.length);
            }
            catch(IOException exception) {

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {

                        setStatus(
                            "Error sending Entity State PDU" +
                            "See log file...");
                    }
                });
            }
        }
        else if (this.state == State.PENDING_ENTITY) {

            // Found CFS entity, send action request to SAF node.
            final String name;
            final EntityType type;

            name = pdu.getMarking();
            type = EntityTypes.getEntityType(pdu.getEntityType());

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {

                    if (state == State.PENDING_ENTITY) {

                        StringBuffer buffer = new StringBuffer();

                        buffer.append("Found CFS Entity \"" + name + "\"");
                        buffer.append("\n" + type);
                        buffer.append("\n" + type.description);

                        setStatus(buffer.toString());

                        if (sendActionRequest(pdu)) {

                            setState(State.PENDING_RESPONSE);
                        }
                        else {

                            setStatus(
                                "Error sending CFS Action Request\n" +
                                "See log file...");
                        }
                    }
                }
            });
        }
    }

    private boolean sendActionRequest(PDU pdu) {

        EntityState entityState = (EntityState)pdu.getPDU();

        try {

            ActionRequest request = new ActionRequest();
            PDUHeader header = request.getHeader();
            CommandFromSimulator record = new CommandFromSimulator(15505);

            header.setProtocol(this.data.protocol);
            header.setExercise(this.data.safExercise);
            header.setType(VDIS.PDU_TYPE_ACTION_REQUEST);
            header.setFamily(5); // PDU_FAMILY_SIMULATION_MANAGEMENT
            header.setTimestamp(entityState.getHeader().getTimestamp());
            header.setLength(BASE_LENGTH + this.getTotalMarkingLength());

            record.getEntityId().set(this.data.entityId);
            record.setEntityType(entityState.getEntityType());
            record.setName(this.data.marking);

            request.setActionId(48); // ACTREQ_ID_COMMAND_FROM_SIMULATOR
            request.setRequestId(this.data.getNextRequestId());
            request.getOriginator().set(entityState.getEntityId());
            request.getOriginator().setEntity(0);
            request.getRecipient().set(65535, 65535, 65535);
            request.getSpecification().getVariable().add(record);

            byte buffer[] = request.write();

            if (buffer.length != header.getLength()) {

                logger.error("Buffer length and PDU length inconsistent!");
            }

            if ((buffer != null) && (buffer.length > 0)) {

                this.sendBuffer(buffer, buffer.length);
            }
            else {

                throw new Exception("Bad buffer from ActionRequest class!");
            }

            return true;
        }
        catch(Exception exception) {

            logger.error("Caught exception!", exception);
            return false;
        }
    }

    private void processActionResponse(PDU pdu) {

        int capturedId = pdu.getRequestId();
        int expectedId = this.data.requestId;

        if (capturedId == expectedId) {

            pdu.decode(true);

            ActionResponse response;
            DatumSpecificationRecord specification;
            AbstractDatumRecord record;
            CommandFromSimulator cfs;

            response = (ActionResponse)pdu.getPDU();
            specification = response.getSpecification();

            for(int i = 0; (i < specification.getVariable().size()); ++i) {

                record = specification.getVariable().get(i);

                if (record instanceof CommandFromSimulator) {

                    cfs = (CommandFromSimulator)record;

                    if (cfs.getEntityId().equals(this.data.entityId)) {

                        this.setStatus(
                            "Action Response Status: " +
                            VDIS.getDescription(
                                VDIS.ACTRES_REQ_STATUS,
                                response.getStatus()));

                        // ACTRES_REQ_COMPLETE?
                        if (response.getStatus() == 4) {

                            // Stop SAF capture thread, no longer needed.
                            this.safCaptureThread.setStopped(true);
                            this.safCaptureThread = null;

                            if (this.translate.isSelected()) {

                                this.setState(State.PENDING_COMPLETE);
                            }
                            else {

                                this.setState(State.COMPLETE);
                            }
                        }
                    }
                }
            }
        }
    }

    private void start() {

        try {

            this.cfsCaptureThread = new CaptureThread(
                (super.getTabName() + ":" + this.data.cfsPort),
                this,
                this.data.cfsPort);
            this.safCaptureThread = new CaptureThread(
                (super.getTabName() + ":" + this.data.safPort),
                this,
                this.data.safPort);

            System.out.println(
                this.getTabName() +
                ": Listening on ports: " + this.data.cfsPort +
                " and " + this.data.safPort);

            this.socket = new DatagramSocket();
            this.socket.setBroadcast(true);

            this.cfsCaptureThread.start();
            this.safCaptureThread.start();

            this.setState(State.PENDING_ENTITY);
            this.enableInputs(false);

            this.translate.setEnabled(false);
            this.start.setEnabled(false);
            this.stop.setEnabled(true);
        }
        catch(Exception exception) {

            logger.error("Caught exception!", exception);
        }
    }

    private void stop() {

        if (this.cfsCaptureThread != null) {

            this.cfsCaptureThread.setStopped(true);
            this.cfsCaptureThread = null;
        }

        if (this.safCaptureThread != null) {

            this.safCaptureThread.setStopped(true);
            this.safCaptureThread = null;
        }

        this.setState(State.PENDING_START);
        this.enableInputs(true);
        this.translate.setEnabled(true);
        this.start.setEnabled(true);
        this.stop.setEnabled(false);
    }

    private void setState(State state) {

        StringBuilder buffer = new StringBuilder();
        this.state = state;

        switch(state) {

            case PENDING_START:

                buffer.append("Waiting to start...");
                break;

            case PENDING_ENTITY:

                buffer.append("Waiting for Entity State PDUs from:\n");
                buffer.append("Port: ");
                buffer.append(this.data.cfsPort + "\n");
                buffer.append("Exercise ID: ");
                buffer.append(this.data.cfsExercise + "\n");
                buffer.append("Entity Id: ");
                buffer.append(this.data.entityId.toString());
                break;

            case PENDING_RESPONSE:

                buffer.append("Sent CFS Action Request with Request Id: ");
                buffer.append(this.data.requestId + "\n");
                buffer.append("Waiting for response on:\n");
                buffer.append("Port: ");
                buffer.append(this.data.safPort + "\n");
                buffer.append("Exercise ID: ");
                buffer.append(this.data.safExercise);
                break;

            case PENDING_COMPLETE:

                buffer.append("Translating Entity States on:");
                buffer.append("Port: ");
                buffer.append(this.data.safPort + "\n");
                buffer.append("Exercise ID: ");
                buffer.append(this.data.safExercise);
                break;

            case COMPLETE:

                buffer.append("CFS complete...");
                break;
        }

        this.setStatus(buffer.toString());
    }

    private void setStatus(String text) {

        this.status.append(format.format(System.currentTimeMillis()));
        this.status.append("\n");
        this.status.append(text);
        this.status.append("\n\n");
    }

    private int getTotalMarkingLength() {

        int length = this.data.marking.length();

        length += (8 - (length % 8));

        return length;
    }

    private void sendBuffer(byte buffer[], int length) throws IOException {

        DatagramPacket packet = new DatagramPacket(
            buffer,
            length,
            Network.getPlaybackAddress(),
            this.data.safPort);

        this.socket.send(packet);
    }

    private void updateData() {

        this.updatingData = true;

        String text = this.marking.getText();

        this.data.cfsPort = Utilities.getIntegerValue(this.cfsPort);
        this.data.cfsExercise = Utilities.getIntegerValue(this.cfsExercise);
        this.data.safPort = Utilities.getIntegerValue(this.safPort);
        this.data.safExercise = Utilities.getIntegerValue(this.safExercise);

        this.data.entityId.set(
            Utilities.getIntegerValue(this.site),
            Utilities.getIntegerValue(this.application),
            Utilities.getIntegerValue(this.entity));

        if (text.isEmpty()) {

            this.marking.setText(this.data.marking);
        }
        else {

            this.data.marking = text;
        }

        if (logger.isDebugEnabled()) {

            logger.debug(data.toString());
        }

        this.updatingData = false;
    }

    private void updateWidgets() {

        this.marking.setText(this.data.marking);
        this.site.setValue(this.data.entityId.getSite());
        this.application.setValue(this.data.entityId.getApplication());
        this.entity.setValue(this.data.entityId.getEntity());
        this.cfsPort.setValue(this.data.cfsPort);
        this.cfsExercise.setValue(this.data.cfsExercise);
        this.safPort.setValue(this.data.safPort);
        this.safExercise.setValue(this.data.safExercise);
    }

    private void configureWidgets() {

        this.status.setMargin(new Insets(4, 4, 4, 4));

        this.start.setIcon(Utilities.getImageIcon("playback_play.png"));
        this.start.setHideActionText(true);
        this.start.addActionListener(this);

        this.stop.setIcon(Utilities.getImageIcon("playback_stop.png"));
        this.stop.setHideActionText(true);
        this.stop.addActionListener(this);

        this.clear.addActionListener(this);

        this.status.setEditable(false);

        this.cfsPort.addActionListener(this);
        this.cfsPort.addFocusListener(this);

        this.cfsExercise.addActionListener(this);
        this.cfsExercise.addFocusListener(this);

        this.safPort.addActionListener(this);
        this.safPort.addFocusListener(this);

        this.safExercise.addActionListener(this);
        this.safExercise.addFocusListener(this);

        this.marking.addActionListener(this);
        this.marking.addFocusListener(this);

        this.site.addActionListener(this);
        this.site.addFocusListener(this);

        this.application.addActionListener(this);
        this.application.addFocusListener(this);

        this.entity.addActionListener(this);
        this.entity.addFocusListener(this);

        this.translate.setSelected(true);
        this.translate.setToolTipText("Check if Gateway is not is use.");
    }

    private void enableInputs(boolean enabled) {

        this.marking.setEnabled(enabled);
        this.site.setEnabled(enabled);
        this.application.setEnabled(enabled);
        this.entity.setEnabled(enabled);
        this.cfsPort.setEnabled(enabled);
        this.cfsExercise.setEnabled(enabled);
        this.safPort.setEnabled(enabled);
        this.safExercise.setEnabled(enabled);
    }

    private void fill() {

        Utilities.setGridBagLayout(super.getPanel());

        Utilities.addComponent(
            super.getPanel(),
            this.getTools(),
            Utilities.HORIZONTAL,
            0, 0,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 8, 2));
        Utilities.addComponent(
            super.getPanel(),
            this.getIdentificationPanel(),
            Utilities.HORIZONTAL,
            0, 1,
            2, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            super.getPanel(),
            this.getCFSSettingsPanel(),
            Utilities.HORIZONTAL,
            0, 2,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            super.getPanel(),
            this.getSAFSettingsPanel(),
            Utilities.HORIZONTAL,
            1, 2,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            super.getPanel(),
            new JLabel("  "),
            Utilities.BOTH,
            0, 3,
            2, 1,
            0.0, 1.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            super.getPanel(),
            this.getStatusPanel(),
            Utilities.BOTH,
            2, 1,
            1, 4,
            1.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
    }

    private JPanel getIdentificationPanel() {

        JPanel panel = Utilities.getGridBagPanel("CFS Entity Identification");

        this.site.setColumns(5);
        this.site.setHorizontalAlignment(JTextField.RIGHT);
        this.application.setColumns(5);
        this.application.setHorizontalAlignment(JTextField.RIGHT);
        this.entity.setColumns(5);
        this.entity.setHorizontalAlignment(JTextField.RIGHT);

        Utilities.addComponent(
            panel,
            new JLabel("Marking: "),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel,
            this.marking,
            Utilities.HORIZONTAL,
            1, 0,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("Entity Id: "),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel,
            this.site,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            this.application,
            Utilities.HORIZONTAL,
            2, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            this.entity,
            Utilities.HORIZONTAL,
            3, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));

        return panel;
    }

    private JPanel getCFSSettingsPanel() {

        JPanel panel = Utilities.getGridBagPanel("CFS Settings");

        this.cfsPort.setColumns(5);
        this.cfsPort.setHorizontalAlignment(JTextField.RIGHT);
        this.cfsExercise.setColumns(5);
        this.cfsExercise.setHorizontalAlignment(JTextField.RIGHT);

        Utilities.addComponent(
            panel,
            new JLabel("CFS Port: "),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel,
            this.cfsPort,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("CFS Exercise: "),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel,
            this.cfsExercise,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));

        return panel;
    }

    private JPanel getSAFSettingsPanel() {

        JPanel panel = Utilities.getGridBagPanel("SAF Settings");

        this.safPort.setColumns(5);
        this.safPort.setHorizontalAlignment(JTextField.RIGHT);
        this.safExercise.setColumns(5);
        this.safExercise.setHorizontalAlignment(JTextField.RIGHT);

        Utilities.addComponent(
            panel,
            new JLabel("SAF Port: "),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel,
            this.safPort,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("SAF Exercise: "),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel,
            this.safExercise,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));

        return panel;
    }

    private JPanel getStatusPanel() {

        JPanel panel = Utilities.getGridBagPanel("Status");

        Utilities.addComponent(
            panel,
            new JScrollPane(this.status),
            Utilities.BOTH,
            0, 0,
            1, 1,
            1.0, 1.0,
            Utilities.getInsets(2, 2, 2, 2));

        return panel;
    }

    private JToolBar getTools() {

        JToolBar tools = new JToolBar();

        tools.setFloatable(false);

        tools.add(this.start);
        tools.add(this.stop);
        tools.addSeparator();
        tools.add(this.clear);
        tools.addSeparator();
        tools.add(this.translate);

        return tools;
    }

    static class Data {

        public final EntityId entityId = new EntityId(10, 20, 30);
        public String marking = "LDR/1/1/A/1-12CAV/1CD";
        public int protocol =  7;
        public int requestId = 1000;
        public int cfsPort = 3000;
        public int cfsExercise = 1;
        public int safPort = 4000;
        public int safExercise = 2;

        public int getNextRequestId() {

            this.requestId++;

            return this.requestId;
        }

        @Override
        public String toString() {

            StringBuilder builder = new StringBuilder();

            builder.append("\n entityId = " + this.entityId.toString());
            builder.append("\n marking = " + this.marking);
            builder.append("\n protocol = " + this.protocol);
            builder.append("\n requestId = " + this.requestId);
            builder.append("\n cfsPort = " + this.cfsPort);
            builder.append("\n cfsExercise = " + this.cfsExercise);
            builder.append("\n safPort = " + this.safPort);
            builder.append("\n safExercise = " + this.safExercise);

            return builder.toString();
        }
    }
}
