package discover.gui.tabs;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.Timer;

import discover.common.ByteArray;
import discover.common.Hexadecimal;
import discover.common.Version;
import discover.common.buffer.HypertextBuffer;
import discover.common.buffer.PlainTextBuffer;
import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;
import discover.gui.panels.TextPanel;
import discover.gui.widgets.BitRecordWidget;
import discover.gui.widgets.CartesianWidget;
import discover.gui.widgets.DeadReckoningWidget;
import discover.gui.widgets.EntityIdWidget;
import discover.gui.widgets.EntityTypeWidget;
import discover.gui.widgets.LocationWidget;
import discover.gui.widgets.OrientationWidget;
import discover.gui.widgets.TimestampWidget;
import discover.gui.widgets.VPRecordsWidget;
import discover.system.Network;
import discover.vdis.PDU;
import discover.vdis.appearance.AbstractAppearance;
import discover.vdis.appearance.DefaultAppearance;
import discover.vdis.common.EntityCapabilities;
import discover.vdis.common.PDUHeader;
import discover.vdis.common.Timestamp;
import discover.vdis.enums.VDIS;
import discover.vdis.pdu.EntityState;
import discover.vdis.types.EntityType;

/**
 * @author Tony Pinkston
 */
public class EntityTab extends Tab implements ActionListener {

    private static final String HTML = "text/html";
    private static final String PLAIN = "text/plain";
    private static final int DEFAULT_PORT = 3000;

    private final JButton start = new JButton();
    private final JButton stop = new JButton();
    private final JButton apply = new JButton("Apply");
    private final JLabel label = new JLabel();
    private final JFormattedTextField port = Utilities.getIntegerField(DEFAULT_PORT);
    private final JFormattedTextField interval = Utilities.getFloatField(2.0f, null);
    private final JFormattedTextField exercise = Utilities.getIntegerField(1);
    private final JComboBox<String> protocol = new JComboBox<>();
    private final JTextField marking = new JTextField();
    private final JComboBox<String> force = new JComboBox<>();

    private final TimestampWidget timestamp = new TimestampWidget("Initial Timestamp");
    private final EntityIdWidget entityId = new EntityIdWidget();
    private final EntityTypeWidget primary = new EntityTypeWidget("Primary Entity Type");
    private final EntityTypeWidget alternative = new EntityTypeWidget("Alternate Entity Type");
    private final LocationWidget location = new LocationWidget("Location");
    private final CartesianWidget velocity = new CartesianWidget("Velocity");
    private final OrientationWidget orientation = new OrientationWidget("Orientation");
    private final DeadReckoningWidget deadReckoning = new DeadReckoningWidget();
    private final BitRecordWidget appearance = new BitRecordWidget(new DefaultAppearance());
    private final BitRecordWidget capabilities = new BitRecordWidget(new EntityCapabilities());
    private final VPRecordsWidget records = new VPRecordsWidget();

    private final TextPanel content = new TextPanel(HTML, null);
    private final TextPanel hexadecimal = new TextPanel(PLAIN, Font.MONOSPACED);

    private final PDU pdu;

    private DatagramSocket socket = null;
    private Timer timer = null;
    private Timestamp runningTimestamp = new Timestamp();

    private float playbackInterval = 2.0f;
    private boolean ignoreActions = false;

    public EntityTab(PDU pdu) {

        super(pdu.getMarking(), TabType.ENTITY);

        this.pdu = pdu;

        configureWidgets();
        updateWidgets();
        updateAppearance();
        fill();
        updateTextPanels();
    }

    public EntityTab(String name) {

        super(name, TabType.ENTITY);

        pdu = new PDU();

        EntityState state = new EntityState();
        state.getHeader().setProtocol(7);
        state.getHeader().setType(1); // ENTITY_STATE
        state.getHeader().setFamily(1); // ENTITY_INFORMATION_INTERACTION
        state.getHeader().setExercise(1);
        state.getHeader().setLength(state.calculateLength());

        pdu.setPort(DEFAULT_PORT);
        pdu.setPDU(state);
        pdu.encode();

        configureWidgets();
        apply();
        fill();
        updateTextPanels();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (!ignoreActions) {

            Object source = event.getSource();

            if (source == start) {

                start();
            }
            else if (source == stop) {

                stop();
            }
            else if (source == apply) {

                updateAppearance();
                apply();
                updateTextPanels();
            }
            else if (timer != null) {

                if (source == port) {

                    Object object = port.getValue();

                    if (object instanceof Number) {

                        pdu.setPort(((Number)object).intValue());
                    }
                }
                else if (source == interval) {

                    int duration = getTimerDurationMillis();

                    logger.info("Resetting timer for " + duration + " millis");

                    timer.stop();
                    timer = new Timer(duration, this);
                    timer.start();
                }
                else if (source == timer) {

                    sendEntityStatePDU(true);
                }
            }
        }
    }

    @Override
    public void load(Version version, DataInputStream stream)
        throws IOException {

        int port = stream.readInt();

        int length = stream.readInt();

        logger.debug("Loading buffer with {} bytes", length);

        if (length > 0) {

            byte buffer[] = new byte[length];

            stream.read(buffer);

            pdu.setPort(port);
            pdu.setData(buffer);
            pdu.decode(true);

            updateWidgets();
            updateTextPanels();
        }
    }

    @Override
    public void save(DataOutputStream stream) throws IOException {

        stream.writeInt(getTabType().ordinal());
        stream.writeUTF(getTabName());
        stream.writeInt(pdu.getPort());
        stream.writeInt(pdu.getDataLength());

        if (pdu.getData() != null) {

            stream.write(pdu.getData());
        }
    }

    @Override
    public void close() {

        start.removeActionListener(this);
        stop.removeActionListener(this);
        apply.removeActionListener(this);
        port.removeActionListener(this);
        interval.removeActionListener(this);

        stop();
    }

    private void start() {

        if (socket == null) {

            try {

                int duration = getTimerDurationMillis();

                socket = new DatagramSocket();
                socket.setBroadcast(true);

                start.setEnabled(false);
                stop.setEnabled(true);

                logger.info("Setting timer for " + duration + " millis");

                sendEntityStatePDU(false);

                timer = new Timer(duration, this);
                timer.start();

                System.out.println(
                    getTabName() +
                    ": Started Entity State generation.");

            }
            catch(SocketException exception) {

                logger.error("Caught exception!", exception);

                JOptionPane.showMessageDialog(
                    DiscoverFrame.getFrame(),
                    "Caught exception created network socket...",
                    getTabName(),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void stop() {

        if (socket != null) {

            logger.info(getTabName() + ": closing socket");

            socket.close();
            socket = null;
        }

        if (timer != null) {

            logger.info(getTabName() + ": stopping timer");

            timer.stop();
            timer.removeActionListener(this);
            timer = null;

            start.setEnabled(true);
            stop.setEnabled(false);

            System.out.println(
                getTabName() +
                ": Stopped Entity State generation.");
        }
    }

    private void sendEntityStatePDU(boolean incrementTimestamp) {

        byte data[] = pdu.getData();

        if (incrementTimestamp) {

            runningTimestamp.add(getTimerDurationSeconds());

            ByteArray.set32Bits(data, 4, runningTimestamp.getValue());

            label.setText(runningTimestamp.toString());
        }
        else {

            Timestamp initial = pdu.getPDU().getHeader().getTimestamp();

            runningTimestamp.setMinutes(initial.getMinutes());
            runningTimestamp.setSeconds(initial.getSeconds());
            runningTimestamp.setAbsolute(initial.isAbsolute());

            label.setText(initial.toString());
        }

        DatagramPacket packet = new DatagramPacket(
            data,
            data.length,
            Network.getPlaybackAddress(),
            pdu.getPort());

        try {

            socket.send(packet);
        }
        catch(Exception exception) {

            logger.error("Caught exception!", exception);
        }
    }

    private float getTimerDurationSeconds() {

        Object object = interval.getValue();

        if (object instanceof Number) {

            return ((Number)object).floatValue();
        }

        return 2.0f;
    }

    private int getTimerDurationMillis() {

        return (int)(1000.0f * getTimerDurationSeconds());
    }

    private void updateAppearance() {

        EntityType type = primary.getValue();
        AbstractAppearance appearance = null;
        int value = 0x00;

        // Get current 32-bit value:
        appearance = (AbstractAppearance)this.appearance.getValue();
        value = appearance.get();

        // Get updated appearance object based on type:
        appearance = AbstractAppearance.get(type);

        if (appearance == null) {

            appearance = new DefaultAppearance();
        }

        // Keep original 32-bit value...
        appearance.set(value);

        getState().setAppearance(appearance.clone());
        this.appearance.setValue(appearance);
    }

    /**
     * Updates PDU based on widget values.
     */
    private void apply() {

        EntityState state = getState();

        entityId.getValue(state.getEntityId());
        location.gcc.getValue(state.getLocation());
        velocity.getValue(state.getVelocity());
        orientation.getValue(state.getOrientation());
        deadReckoning.getValue(state.getDeadReckoning());

        state.setForceId(Utilities.getComboboxValue(
            force,
            VDIS.FORCE_ID));

        state.setEntityType(primary.getValue());
        state.setAlternateType(alternative.getValue());
        state.getMarking().setMarking(marking.getText());

        appearance.applyValue(state.getAppearance());
        capabilities.applyValue(state.getCapabilities());

        records.getRecords(state.getRecords());

        PDUHeader header = state.getHeader();

        header.setTimestamp(timestamp.getValue());
        header.setExercise(Utilities.getIntegerValue(exercise));
        header.setProtocol(Utilities.getComboboxValue(
            protocol,
            VDIS.PROTOCOL_VERSION));

        // Must be done AFTER records have been set...
        header.setLength(state.calculateLength());

        int port = Utilities.getIntegerValue(this.port);

        if (port < 1024) {

            port = DEFAULT_PORT;

            ignoreActions = true;
            this.port.setValue(port);
            ignoreActions = false;
        }

        pdu.setPort(port);

        playbackInterval = Utilities.getFloatValue(interval);

        if (playbackInterval < 0.1f) {

            playbackInterval = 0.2f;
            ignoreActions = true;
            interval.setValue(playbackInterval);
            ignoreActions = false;
        }

        pdu.encode();
    }

    private void updateTextPanels() {

        try {

            HypertextBuffer buffer = new HypertextBuffer();

            buffer.addText("<html><body>");
            buffer.addBuffer(getState());
            buffer.addText("</body></html>");

            content.setText(buffer.toString());
            content.setCaretPosition(0);
        }
        catch(Exception exception) {

            logger.error("Caught exception!", exception);
        }

        try {

            PlainTextBuffer buffer = new PlainTextBuffer();

            byte data[] = pdu.getData();

            if (data != null) {

                Hexadecimal.toBuffer(buffer,  "  -  ", 4, false, data);
            }

            hexadecimal.setText(buffer.toString());
            hexadecimal.setCaretPosition(0);
        }
        catch(Exception exception) {

            logger.error("Caught exception!", exception);
        }
    }

    private void fill() {

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        tabs.add("Content", content.getPanel());
        tabs.add("Byte View", hexadecimal.getPanel());

        split.setLeftComponent(getSettingsPanel());
        split.setRightComponent(tabs);
        split.setContinuousLayout(true);

        Utilities.setGridBagLayout(getPanel());

        Utilities.addComponent(
            getPanel(),
            split,
            Utilities.BOTH,
            0, 0,
            1, 1,
            1.0, 1.0,
            Utilities.getInsets(0, 0, 0, 0));
    }

    /**
     * Updates widget values based on PDU.
     */
    private void updateWidgets() {

        EntityState state = getState();

        exercise.setValue(state.getHeader().getExercise());
        timestamp.setValue(state.getHeader().getTimestamp());
        marking.setText(state.getMarking().getMarking());
        entityId.setValue(state.getEntityId());
        primary.setValue(state.getEntityType());
        alternative.setValue(state.getAlternateType());
        location.setValue(state.getLocation());
        velocity.setValue(state.getVelocity());
        orientation.setValue(state.getOrientation());
        deadReckoning.setValue(state.getDeadReckoning());
        appearance.setValue(state.getAppearance());
        capabilities.setValue(state.getCapabilities());
        records.setRecords(state.getRecords());

        Utilities.setComboBoxValue(
            protocol,
            VDIS.PROTOCOL_VERSION,
            Integer.valueOf(state.getHeader().getProtocol()));
        Utilities.setComboBoxValue(
            force,
            VDIS.FORCE_ID,
            Integer.valueOf(state.getForceId()));
    }

    private void configureWidgets() {

        start.setIcon(Utilities.getImageIcon("playback_play.png"));
        start.setHideActionText(true);
        start.addActionListener(this);

        stop.setIcon(Utilities.getImageIcon("playback_stop.png"));
        stop.setHideActionText(true);
        stop.addActionListener(this);

        apply.addActionListener(this);

        port.setValue(pdu.getPort());
        port.addActionListener(this);
        port.setColumns(5);
        port.setHorizontalAlignment(JTextField.RIGHT);

        interval.setValue(playbackInterval);
        interval.addActionListener(this);
        interval.setColumns(5);
        interval.setHorizontalAlignment(JTextField.RIGHT);

        exercise.setValue(pdu.getExercise());
        exercise.setColumns(5);
        exercise.setHorizontalAlignment(JTextField.RIGHT);

        timestamp.minutes.setValue(0);
        timestamp.seconds.setValue(0.0f);

        marking.setColumns(15);

        Utilities.configureComboBox(
            protocol,
            VDIS.PROTOCOL_VERSION,
            false);
        Utilities.setComboBoxValue(
            protocol,
            VDIS.PROTOCOL_VERSION,
            7); // PTCL_VER_IEEE_1278_1_200X_DRAFT (V-DIS)

        Utilities.configureComboBox(
            force,
            VDIS.FORCE_ID,
            false);
    }

    private EntityState getState() {

        return (EntityState)pdu.getPDU();
    }

    private JToolBar getTools() {

        JToolBar tools = new JToolBar();

        tools.setFloatable(false);

        tools.add(start);
        tools.add(stop);
        tools.addSeparator();
        tools.add(apply);
        tools.addSeparator();
        tools.add(new JLabel("Current Timestamp: "));
        tools.add(label);

        return tools;
    }

    private JPanel getSettingsPanel() {

        JPanel panel = Utilities.getGridBagPanel(null);
        JPanel subpanel = Utilities.getGridBagPanel(null);
        JComponent components[] = new JComponent[6];

        components[0] = getVDISPanel();
        components[1] = getIdentificationPanel();
        components[2] = getSpatialPanel();
        components[3] = appearance.getPanel();
        components[4] = capabilities.getPanel();
        components[5] = records.getPanel();

        for(int y = 0 ; y < components.length; ++y) {

            Utilities.addComponent(
                subpanel,
                components[y],
                Utilities.HORIZONTAL,
                0, y,
                1, 1,
                0.5, 0.0,
                Utilities.getInsets(8, 4, 4, 100));
        }

        Utilities.addComponent(
            subpanel,
            new JLabel("   "),
            Utilities.VERTICAL,
            0, components.length,
            1, 1,
            0.0, 1.0,
            Utilities.getInsets(2, 2, 2, 2));

        Utilities.addComponent(
            panel,
            getTools(),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(0, 0, 0, 0));
        Utilities.addComponent(
            panel,
            new JScrollPane(
                subpanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),
            Utilities.BOTH,
            0, 1,
            1, 1,
            0.5, 0.5,
            Utilities.getInsets(0, 0, 0, 0));

        return panel;
    }

    private JPanel getVDISPanel() {

        JPanel panel = Utilities.getGridBagPanel("V-DIS");

        Utilities.addComponent(
            panel,
            new JLabel("Port:"),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel,
            port,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 6));
        Utilities.addComponent(
            panel,
            new JLabel("Exercise:"),
            Utilities.HORIZONTAL,
            2, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel,
            exercise,
            Utilities.HORIZONTAL,
            3, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 6));
        Utilities.addComponent(
            panel,
            new JLabel("Interval:"),
            Utilities.HORIZONTAL,
            4, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 6, 2, 2));
        Utilities.addComponent(
            panel,
            interval,
            Utilities.HORIZONTAL,
            5, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("Protocol:"),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel,
            protocol,
            Utilities.HORIZONTAL,
            1, 1,
            5, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            timestamp.getPanel(),
            Utilities.HORIZONTAL,
            0, 2,
            6, 1,
            1.0, 0.0,
            Utilities.getInsets(6, 2, 2, 2));

        return panel;
    }

    private JPanel getIdentificationPanel() {

        JPanel panel = Utilities.getGridBagPanel("Identification");

        Utilities.addComponent(
            panel,
            new JLabel("Marking:"),
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
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("Force:"),
            Utilities.HORIZONTAL,
            2, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel,
            force,
            Utilities.HORIZONTAL,
            3, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            entityId.getPanel(),
            Utilities.HORIZONTAL,
            0, 1,
            4, 1,
            0.5, 0.0,
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel,
            primary.getPanel(),
            Utilities.HORIZONTAL,
            0, 2,
            6, 1,
            0.5, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel,
            alternative.getPanel(),
            Utilities.HORIZONTAL,
            0, 3,
            6, 1,
            0.5, 0.0,
            Utilities.getInsets(2, 2, 2, 2));

        return panel;
    }

    private JPanel getSpatialPanel() {

        JPanel panel = Utilities.getGridBagPanel("Spatial");

        Utilities.addComponent(
            panel,
            location.getPanel(),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            velocity.getPanel(),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            orientation.getPanel(),
            Utilities.HORIZONTAL,
            0, 2,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            deadReckoning.getPanel(),
            Utilities.HORIZONTAL,
            0, 3,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.setBorder(panel, "Spatial");

        return panel;
    }
}
