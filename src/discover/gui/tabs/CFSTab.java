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
import discover.vdis.enums.PDU_FAMILY;
import discover.vdis.enums.PDU_TYPE;
import discover.vdis.enums.PROTOCOL_VERSION;
import discover.vdis.pdu.ActionRequest;
import discover.vdis.pdu.ActionResponse;
import discover.vdis.pdu.EntityState;
import discover.vdis.types.EntityType;
import discover.vdis.types.EntityTypes;

/**
 * @author Tony Pinkston
 */
@SuppressWarnings("serial")
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

        configureWidgets();
        updateWidgets();
        setState(State.PENDING_START);
        fill();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        Object source = event.getSource();

        if (source == start) {

            start();
        }
        else if (source == stop) {

            stop();
        }
        else if (source == clear) {

            status.setText("");
        }
        else if (!updatingData) {

            updateData();
        }
    }

    @Override
    public void focusGained(FocusEvent event) {

        if (!updatingData) {

            updateData();
        }
    }

    @Override
    public void focusLost(FocusEvent event) {

        if (!updatingData) {

            updateData();
        }
    }

    @Override
    public void pdusCaptured(List<PDU> list) {

        EntityId entityId = new EntityId();

        for(final PDU pdu : list) {

            if (pdu.getTypeEnum() == PDU_TYPE.ENTITY_STATE) {

                pdu.getEntityId(entityId);

                if (pdu.getPort() == data.cfsPort) {

                    if (data.entityId.equals(entityId)) {

                        pdu.decode(false);
                        processEntityState(pdu);
                    }
                }
            }
            else if (pdu.getTypeEnum() == PDU_TYPE.ACTION_RESPONSE) {

                if (pdu.getPort() == data.safPort) {

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
        data.entityId.setSite(stream.readUnsignedShort());
        data.entityId.setApplication(stream.readUnsignedShort());
        data.entityId.setEntity(stream.readUnsignedShort());
        data.protocol = PROTOCOL_VERSION.get(stream.readByte());
        data.marking = stream.readUTF();
        data.requestId = stream.readInt();
        data.cfsPort = stream.readInt();
        data.cfsExercise = stream.readInt();
        data.safPort = stream.readInt();
        data.safExercise = stream.readInt();

        updateWidgets();
    }

    @Override
    public void save(DataOutputStream stream) throws IOException {

        stream.writeInt(getType().ordinal());
        stream.writeUTF(getName());
        stream.writeShort(data.entityId.getSite());
        stream.writeShort(data.entityId.getApplication());
        stream.writeShort(data.entityId.getEntity());
        stream.writeByte(data.protocol.value);
        stream.writeUTF(data.marking);
        stream.writeInt(data.requestId);
        stream.writeInt(data.cfsPort);
        stream.writeInt(data.cfsExercise);
        stream.writeInt(data.safPort);
        stream.writeInt(data.safExercise);
    }

    @Override
    public void close() {

        start.removeActionListener(this);
        stop.removeActionListener(this);
        clear.removeActionListener(this);
        cfsPort.removeActionListener(this);
        cfsPort.removeFocusListener(this);
        cfsExercise.removeActionListener(this);
        cfsExercise.removeFocusListener(this);
        safPort.removeActionListener(this);
        safPort.removeFocusListener(this);
        safExercise.removeActionListener(this);
        safExercise.removeFocusListener(this);
        marking.removeActionListener(this);
        marking.removeFocusListener(this);
        site.removeActionListener(this);
        site.removeFocusListener(this);
        application.removeActionListener(this);
        application.removeFocusListener(this);
        entity.removeActionListener(this);
        entity.removeFocusListener(this);

        stop();
    }

    private void processEntityState(final PDU pdu) {

        if (state == State.PENDING_COMPLETE) {

            // Forward entity state PDU from CFS node, whatever it may be,
            // to SAF node.
            byte buffer[] = Arrays.copyOf(pdu.getData(), pdu.getLength());

            // The 1st byte is the protocol.
            buffer[0] = (byte)data.protocol.value;

            // The 2nd byte is the exercise.
            buffer[1] = (byte)data.safExercise;

            // The 143th byte in the buffer should be 3rd byte of the 4-byte
            // entity capabilities record.  The zero bit denotes the entity
            // is "Task Organizable", this bit must be set.
            buffer[142] |= 0x01;

            try {

                sendBuffer(buffer, buffer.length);
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
        else if (state == State.PENDING_ENTITY) {

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

            header.setProtocol(data.protocol);
            header.setExercise(data.safExercise);
            header.setType(PDU_TYPE.ACTION_REQUEST);
            header.setFamily(PDU_FAMILY.SIMULATION_MANAGEMENT);
            header.setTimestamp(entityState.getHeader().getTimestamp());
            header.setLength(BASE_LENGTH + getTotalMarkingLength());

            record.getEntityId().set(data.entityId);
            record.setEntityType(entityState.getEntityType());
            record.setName(data.marking);

            request.setActionId(48); // ACTREQ_ID_COMMAND_FROM_SIMULATOR
            request.setRequestId(data.getNextRequestId());
            request.getOriginator().set(entityState.getEntityId());
            request.getOriginator().setEntity(0);
            request.getRecipient().set(65535, 65535, 65535);
            request.getSpecification().getVariable().add(record);

            byte buffer[] = request.write();

            if (buffer.length != header.getLength()) {

                logger.error("Buffer length and PDU length inconsistent!");
            }

            if ((buffer != null) && (buffer.length > 0)) {

                sendBuffer(buffer, buffer.length);
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
        int expectedId = data.requestId;

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

                    if (cfs.getEntityId().equals(data.entityId)) {

                        setStatus(
                            "Action Response Status: " +
                            response.getStatusEnum().description);

                        // ACTRES_REQ_COMPLETE?
                        if (response.getStatus() == 4) {

                            // Stop SAF capture thread, no longer needed.
                            safCaptureThread.setStopped(true);
                            safCaptureThread = null;

                            if (translate.isSelected()) {

                                setState(State.PENDING_COMPLETE);
                            }
                            else {

                                setState(State.COMPLETE);
                            }
                        }
                    }
                }
            }
        }
    }

    private void start() {

        try {

            cfsCaptureThread = new CaptureThread(
                (getName() + ":" + data.cfsPort),
                this,
                data.cfsPort);
            safCaptureThread = new CaptureThread(
                (getName() + ":" + data.safPort),
                this,
                data.safPort);

            System.out.println(
                getName() +
                ": Listening on ports: " + data.cfsPort +
                " and " + data.safPort);

            socket = new DatagramSocket();
            socket.setBroadcast(true);

            cfsCaptureThread.start();
            safCaptureThread.start();

            setState(State.PENDING_ENTITY);
            enableInputs(false);

            translate.setEnabled(false);
            start.setEnabled(false);
            stop.setEnabled(true);
        }
        catch(Exception exception) {

            logger.error("Caught exception!", exception);
        }
    }

    private void stop() {

        if (cfsCaptureThread != null) {

            cfsCaptureThread.setStopped(true);
            cfsCaptureThread = null;
        }

        if (safCaptureThread != null) {

            safCaptureThread.setStopped(true);
            safCaptureThread = null;
        }

        setState(State.PENDING_START);
        enableInputs(true);
        translate.setEnabled(true);
        start.setEnabled(true);
        stop.setEnabled(false);
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
                buffer.append(data.cfsPort + "\n");
                buffer.append("Exercise ID: ");
                buffer.append(data.cfsExercise + "\n");
                buffer.append("Entity Id: ");
                buffer.append(data.entityId.toString());
                break;

            case PENDING_RESPONSE:

                buffer.append("Sent CFS Action Request with Request Id: ");
                buffer.append(data.requestId + "\n");
                buffer.append("Waiting for response on:\n");
                buffer.append("Port: ");
                buffer.append(data.safPort + "\n");
                buffer.append("Exercise ID: ");
                buffer.append(data.safExercise);
                break;

            case PENDING_COMPLETE:

                buffer.append("Translating Entity States on:");
                buffer.append("Port: ");
                buffer.append(data.safPort + "\n");
                buffer.append("Exercise ID: ");
                buffer.append(data.safExercise);
                break;

            case COMPLETE:

                buffer.append("CFS complete...");
                break;
        }

        setStatus(buffer.toString());
    }

    private void setStatus(String text) {

        status.append(format.format(System.currentTimeMillis()));
        status.append("\n");
        status.append(text);
        status.append("\n\n");
    }

    private int getTotalMarkingLength() {

        int length = data.marking.length();

        length += (8 - (length % 8));

        return length;
    }

    private void sendBuffer(byte buffer[], int length) throws IOException {

        DatagramPacket packet = new DatagramPacket(
            buffer,
            length,
            Network.getPlaybackAddress(),
            data.safPort);

        socket.send(packet);
    }

    private void updateData() {

        updatingData = true;

        String text = marking.getText();

        data.cfsPort = Utilities.getIntegerValue(cfsPort);
        data.cfsExercise = Utilities.getIntegerValue(cfsExercise);
        data.safPort = Utilities.getIntegerValue(safPort);
        data.safExercise = Utilities.getIntegerValue(safExercise);

        data.entityId.set(
            Utilities.getIntegerValue(site),
            Utilities.getIntegerValue(application),
            Utilities.getIntegerValue(entity));

        if (text.isEmpty()) {

            marking.setText(data.marking);
        }
        else {

            data.marking = text;
        }

        if (logger.isDebugEnabled()) {

            logger.debug(data.toString());
        }

        updatingData = false;
    }

    private void updateWidgets() {

        marking.setText(data.marking);
        site.setValue(data.entityId.getSite());
        application.setValue(data.entityId.getApplication());
        entity.setValue(data.entityId.getEntity());
        cfsPort.setValue(data.cfsPort);
        cfsExercise.setValue(data.cfsExercise);
        safPort.setValue(data.safPort);
        safExercise.setValue(data.safExercise);
    }

    private void configureWidgets() {

        status.setMargin(new Insets(4, 4, 4, 4));

        start.setIcon(Utilities.getImageIcon("playback_play.png"));
        start.setHideActionText(true);
        start.addActionListener(this);

        stop.setIcon(Utilities.getImageIcon("playback_stop.png"));
        stop.setHideActionText(true);
        stop.addActionListener(this);

        clear.addActionListener(this);

        status.setEditable(false);

        cfsPort.addActionListener(this);
        cfsPort.addFocusListener(this);

        cfsExercise.addActionListener(this);
        cfsExercise.addFocusListener(this);

        safPort.addActionListener(this);
        safPort.addFocusListener(this);

        safExercise.addActionListener(this);
        safExercise.addFocusListener(this);

        marking.addActionListener(this);
        marking.addFocusListener(this);

        site.addActionListener(this);
        site.addFocusListener(this);

        application.addActionListener(this);
        application.addFocusListener(this);

        entity.addActionListener(this);
        entity.addFocusListener(this);

        translate.setSelected(true);
        translate.setToolTipText("Check if Gateway is not is use.");
    }

    private void enableInputs(boolean enabled) {

        marking.setEnabled(enabled);
        site.setEnabled(enabled);
        application.setEnabled(enabled);
        entity.setEnabled(enabled);
        cfsPort.setEnabled(enabled);
        cfsExercise.setEnabled(enabled);
        safPort.setEnabled(enabled);
        safExercise.setEnabled(enabled);
    }

    private void fill() {

        Utilities.setGridBagLayout(this);

        Utilities.addComponent(
            this,
            getTools(),
            Utilities.HORIZONTAL,
            0, 0,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 8, 2));
        Utilities.addComponent(
            this,
            getIdentificationPanel(),
            Utilities.HORIZONTAL,
            0, 1,
            2, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            this,
            getCFSSettingsPanel(),
            Utilities.HORIZONTAL,
            0, 2,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            this,
            getSAFSettingsPanel(),
            Utilities.HORIZONTAL,
            1, 2,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            this,
            new JLabel("  "),
            Utilities.BOTH,
            0, 3,
            2, 1,
            0.0, 1.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            this,
            getStatusPanel(),
            Utilities.BOTH,
            2, 1,
            1, 4,
            1.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
    }

    private JPanel getIdentificationPanel() {

        JPanel panel = Utilities.getGridBagPanel("CFS Entity Identification");

        site.setColumns(5);
        site.setHorizontalAlignment(JTextField.RIGHT);
        application.setColumns(5);
        application.setHorizontalAlignment(JTextField.RIGHT);
        entity.setColumns(5);
        entity.setHorizontalAlignment(JTextField.RIGHT);

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
            marking,
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
            site,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            application,
            Utilities.HORIZONTAL,
            2, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            entity,
            Utilities.HORIZONTAL,
            3, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));

        return panel;
    }

    private JPanel getCFSSettingsPanel() {

        JPanel panel = Utilities.getGridBagPanel("CFS Settings");

        cfsPort.setColumns(5);
        cfsPort.setHorizontalAlignment(JTextField.RIGHT);
        cfsExercise.setColumns(5);
        cfsExercise.setHorizontalAlignment(JTextField.RIGHT);

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
            cfsPort,
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
            cfsExercise,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));

        return panel;
    }

    private JPanel getSAFSettingsPanel() {

        JPanel panel = Utilities.getGridBagPanel("SAF Settings");

        safPort.setColumns(5);
        safPort.setHorizontalAlignment(JTextField.RIGHT);
        safExercise.setColumns(5);
        safExercise.setHorizontalAlignment(JTextField.RIGHT);

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
            safPort,
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
            safExercise,
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
            new JScrollPane(status),
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

        tools.add(start);
        tools.add(stop);
        tools.addSeparator();
        tools.add(clear);
        tools.addSeparator();
        tools.add(translate);

        return tools;
    }

    static class Data {

        public final EntityId entityId = new EntityId(10, 20, 30);
        public String marking = "LDR/1/1/A/1-12CAV/1CD";
        public PROTOCOL_VERSION protocol = PROTOCOL_VERSION.PTCL_VER_IEEE_1278_1_2012;
        public int requestId = 1000;
        public int cfsPort = 3000;
        public int cfsExercise = 1;
        public int safPort = 4000;
        public int safExercise = 2;

        public int getNextRequestId() {

            requestId++;

            return requestId;
        }

        @Override
        public String toString() {

            StringBuilder builder = new StringBuilder();

            builder.append("\n entityId = " + entityId.toString());
            builder.append("\n marking = " + marking);
            builder.append("\n protocol = " + protocol);
            builder.append("\n requestId = " + requestId);
            builder.append("\n cfsPort = " + cfsPort);
            builder.append("\n cfsExercise = " + cfsExercise);
            builder.append("\n safPort = " + safPort);
            builder.append("\n safExercise = " + safExercise);

            return builder.toString();
        }
    }
}
