/**
 * @author Tony Pinkston
 */
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
import java.util.logging.Level;

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

        this.configureWidgets();
        this.updateWidgets();
        this.updateAppearance();
        this.fill();
        this.updateTextPanels();
    }
    
    public EntityTab(String name) {
        
        super(name, TabType.ENTITY);
        
        this.pdu = new PDU();
        
        EntityState state = new EntityState();
        state.getHeader().setProtocol(7);
        state.getHeader().setType(1); // ENTITY_STATE 
        state.getHeader().setFamily(1); // ENTITY_INFORMATION_INTERACTION
        state.getHeader().setExercise(1); 
        state.getHeader().setLength(state.calculateLength());
        
        this.pdu.setPort(DEFAULT_PORT);
        this.pdu.setPDU(state);
        this.pdu.encode();

        this.configureWidgets();
        this.apply();
        this.fill();
        this.updateTextPanels();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (!this.ignoreActions) {
            
            Object source = event.getSource();
            
            if (source == this.start) {
                
                this.start();
            }
            else if (source == this.stop) {
                
                this.stop();
            }
            else if (source == this.apply) {
                
                this.updateAppearance();
                this.apply();
                this.updateTextPanels();
            }
            else if (this.timer != null) {
                
                if (source == this.port) {
                    
                    Object object = this.port.getValue();
                    
                    if (object instanceof Number) {
                        
                        this.pdu.setPort(((Number)object).intValue());
                    }
                }
                else if (source == this.interval) {
                    
                    int duration = this.getTimerDurationMillis();
                    
                    logger.info("Resetting timer for " + duration + " millis");

                    this.timer.stop();
                    this.timer = new Timer(duration, this);
                    this.timer.start();
                }
                else if (source == this.timer) {
                    
                    this.sendEntityStatePDU(true);
                }
            }
        }
    }

    @Override
    public void load(Version version, DataInputStream stream) 
        throws IOException {
        
        int port = stream.readInt();
        
        int length = stream.readInt();
        
        logger.fine("Loading buffer with " + length + " bytes");
        
        if (length > 0) {
            
            byte buffer[] = new byte[length];
            
            stream.read(buffer);
            
            this.pdu.setPort(port);
            this.pdu.setData(buffer);
            this.pdu.decode(true);
            
            this.updateWidgets();
            this.updateTextPanels();
        }
    }
    
    @Override
    public void save(DataOutputStream stream) throws IOException {
        
        stream.writeInt(super.getTabType().ordinal());
        stream.writeUTF(super.getTabName());
        stream.writeInt(this.pdu.getPort());
        stream.writeInt(this.pdu.getDataLength());
        
        if (this.pdu.getData() != null) {
            
            stream.write(this.pdu.getData());
        }
    }

    @Override
    public void close() {
        
        this.start.removeActionListener(this);
        this.stop.removeActionListener(this);
        this.apply.removeActionListener(this);
        this.port.removeActionListener(this);
        this.interval.removeActionListener(this);
        
        this.stop();
    }
    
    private void start() {
        
        if (this.socket == null) {
            
            try {

                int duration = this.getTimerDurationMillis();
                
                this.socket = new DatagramSocket();
                this.socket.setBroadcast(true);
                
                this.start.setEnabled(false);
                this.stop.setEnabled(true);
                
                logger.info("Setting timer for " + duration + " millis");
                
                this.sendEntityStatePDU(false);
                
                this.timer = new Timer(duration, this);
                this.timer.start();

                System.out.println(
                    super.getTabName() + 
                    ": Started Entity State generation.");

            } 
            catch(SocketException exception) {

                logger.log(Level.SEVERE, "Caught exception!", exception);
                
                JOptionPane.showMessageDialog(
                    DiscoverFrame.getFrame(),
                    "Caught exception created network socket...",
                    super.getTabName(),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void stop() {
        
        if (this.socket != null) {
            
            logger.info(super.getTabName() + ": closing socket");
            
            this.socket.close();
            this.socket = null;
        }
        
        if (this.timer != null) {
            
            logger.info(super.getTabName() + ": stopping timer");

            this.timer.stop();
            this.timer.removeActionListener(this);
            this.timer = null;
            
            this.start.setEnabled(true);
            this.stop.setEnabled(false);

            System.out.println(
                super.getTabName() + 
                ": Stopped Entity State generation.");
        }
    }
    
    private void sendEntityStatePDU(boolean incrementTimestamp) {
        
        byte data[] = this.pdu.getData();
        
        if (incrementTimestamp) {
            
            this.runningTimestamp.add(this.getTimerDurationSeconds());
            
            ByteArray.set32Bits(data, 4, this.runningTimestamp.getValue());
            
            this.label.setText(this.runningTimestamp.toString());
        }
        else {
            
            Timestamp initial = this.pdu.getPDU().getHeader().getTimestamp();

            this.runningTimestamp.setMinutes(initial.getMinutes());
            this.runningTimestamp.setSeconds(initial.getSeconds());
            this.runningTimestamp.setAbsolute(initial.isAbsolute());

            this.label.setText(initial.toString());
        }
        
        DatagramPacket packet = new DatagramPacket(
            data,
            data.length,
            Network.getPlaybackAddress(),
            this.pdu.getPort());
        
        try {
        
            this.socket.send(packet);
        } 
        catch(Exception exception) {
            
            logger.log(Level.SEVERE, "Caught exception!", exception);
        }
    }
    
    private float getTimerDurationSeconds() {
        
        Object object = this.interval.getValue();
        
        if (object instanceof Number) {
            
            return ((Number)object).floatValue();
        }
        
        return 2.0f;
    }
    
    private int getTimerDurationMillis() {
        
        return (int)(1000.0f * this.getTimerDurationSeconds());
    }
    
    private void updateAppearance() {
        
        EntityType type = this.primary.getValue();
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

        this.getState().setAppearance(appearance.clone());
        this.appearance.setValue(appearance);
    }
    
    /**
     * Updates PDU based on widget values.
     */
    private void apply() {
        
        EntityState state = this.getState();
        
        this.entityId.getValue(state.getEntityId());
        this.location.gcc.getValue(state.getLocation());
        this.velocity.getValue(state.getVelocity());
        this.orientation.getValue(state.getOrientation());
        this.deadReckoning.getValue(state.getDeadReckoning());
        
        state.setForceId(Utilities.getComboboxValue(
            this.force, 
            VDIS.FORCE_ID));
        
        state.setEntityType(this.primary.getValue());
        state.setAlternateType(this.alternative.getValue());
        state.getMarking().setMarking(this.marking.getText());
        
        this.appearance.applyValue(state.getAppearance());
        this.capabilities.applyValue(state.getCapabilities());
        
        this.records.getRecords(state.getRecords());

        PDUHeader header = state.getHeader();
        
        header.setTimestamp(this.timestamp.getValue());
        header.setExercise(Utilities.getIntegerValue(this.exercise));
        header.setProtocol(Utilities.getComboboxValue(
            this.protocol, 
            VDIS.PROTOCOL_VERSION));
        
        // Must be done AFTER records have been set...
        header.setLength(state.calculateLength());

        int port = Utilities.getIntegerValue(this.port);
        
        if (port < 1024) {
            
            port = DEFAULT_PORT;
            
            this.ignoreActions = true;
            this.port.setValue(port);
            this.ignoreActions = false;
        }
        
        this.pdu.setPort(port);
        
        this.playbackInterval = Utilities.getFloatValue(this.interval);
        
        if (this.playbackInterval < 0.1f) {

            this.playbackInterval = 0.2f;
            this.ignoreActions = true;
            this.interval.setValue(this.playbackInterval);
            this.ignoreActions = false;
        }
        
        this.pdu.encode();
    }
    
    private void updateTextPanels() {
        
        try {

            HypertextBuffer buffer = new HypertextBuffer();
            
            buffer.addText("<html><body>");
            buffer.addBuffer(this.getState());
            buffer.addText("</body></html>");

            this.content.setText(buffer.toString());
            this.content.setCaretPosition(0);
        }
        catch(Exception exception) {
            
            logger.log(Level.SEVERE, "Caught exception!", exception);
        }
        
        try {
            
            PlainTextBuffer buffer = new PlainTextBuffer();
            
            byte data[] = this.pdu.getData();
            
            if (data != null) {
                
                Hexadecimal.toBuffer(buffer,  "  -  ", 4, false, data);
            }
            
            this.hexadecimal.setText(buffer.toString());
            this.hexadecimal.setCaretPosition(0);
        }
        catch(Exception exception) {
            
            logger.log(Level.SEVERE, "Caught exception!", exception);
        }
    }
   
    private void fill() {
        
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        tabs.add("Content", this.content.getPanel());
        tabs.add("Byte View", this.hexadecimal.getPanel());
        
        split.setLeftComponent(this.getSettingsPanel());
        split.setRightComponent(tabs);
        split.setContinuousLayout(true);

        Utilities.setGridBagLayout(super.getPanel());
        
        Utilities.addComponent(
            super.getPanel(), 
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
        
        EntityState state = this.getState();
        
        this.exercise.setValue(state.getHeader().getExercise());
        this.timestamp.setValue(state.getHeader().getTimestamp());
        this.marking.setText(state.getMarking().getMarking());
        this.entityId.setValue(state.getEntityId());
        this.primary.setValue(state.getEntityType());
        this.alternative.setValue(state.getAlternateType());
        this.location.setValue(state.getLocation());
        this.velocity.setValue(state.getVelocity());
        this.orientation.setValue(state.getOrientation());
        this.deadReckoning.setValue(state.getDeadReckoning());
        this.appearance.setValue(state.getAppearance());
        this.capabilities.setValue(state.getCapabilities());
        this.records.setRecords(state.getRecords());
        
        Utilities.setComboBoxValue(
            this.protocol, 
            VDIS.PROTOCOL_VERSION, 
            Integer.valueOf(state.getHeader().getProtocol()));   
        Utilities.setComboBoxValue(
            this.force, 
            VDIS.FORCE_ID, 
            Integer.valueOf(state.getForceId()));
    }
    
    private void configureWidgets() {
        
        this.start.setIcon(Utilities.getImageIcon("playback_play.png"));
        this.start.setHideActionText(true);
        this.start.addActionListener(this);
        
        this.stop.setIcon(Utilities.getImageIcon("playback_stop.png"));
        this.stop.setHideActionText(true);
        this.stop.addActionListener(this);

        this.apply.addActionListener(this);
        
        this.port.setValue(this.pdu.getPort());
        this.port.addActionListener(this);
        this.port.setColumns(5);
        this.port.setHorizontalAlignment(JTextField.RIGHT);
        
        this.interval.setValue(this.playbackInterval);
        this.interval.addActionListener(this);
        this.interval.setColumns(5);
        this.interval.setHorizontalAlignment(JTextField.RIGHT);
        
        this.exercise.setValue(this.pdu.getExercise());
        this.exercise.setColumns(5);
        this.exercise.setHorizontalAlignment(JTextField.RIGHT);
        
        this.timestamp.minutes.setValue(0);
        this.timestamp.seconds.setValue(0.0f);
        
        this.marking.setColumns(15);
        
        Utilities.configureComboBox(
            this.protocol, 
            VDIS.PROTOCOL_VERSION,
            false);
        Utilities.setComboBoxValue(
            this.protocol, 
            VDIS.PROTOCOL_VERSION, 
            7); // PTCL_VER_IEEE_1278_1_200X_DRAFT (V-DIS)

        Utilities.configureComboBox(
            this.force, 
            VDIS.FORCE_ID,
            false);
    }
    
    private EntityState getState() {
        
        return (EntityState)this.pdu.getPDU();
    }
    
    private JToolBar getTools() {
        
        JToolBar tools = new JToolBar();

        tools.setFloatable(false);
        
        tools.add(this.start);
        tools.add(this.stop);
        tools.addSeparator();
        tools.add(this.apply);
        tools.addSeparator();
        tools.add(new JLabel("Current Timestamp: "));
        tools.add(this.label);

        return tools;
    }
    
    private JPanel getSettingsPanel() {
        
        JPanel panel = Utilities.getGridBagPanel(null);
        JPanel subpanel = Utilities.getGridBagPanel(null);
        JComponent components[] = new JComponent[6];
        
        components[0] = this.getVDISPanel();
        components[1] = this.getIdentificationPanel();
        components[2] = this.getSpatialPanel();
        components[3] = this.appearance.getPanel();
        components[4] = this.capabilities.getPanel();
        components[5] = this.records.getPanel();

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
            this.getTools(), 
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
            this.port, 
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
            this.exercise, 
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
            this.interval, 
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
            this.protocol, 
            Utilities.HORIZONTAL, 
            1, 1, 
            5, 1, 
            0.0, 0.0, 
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel, 
            this.timestamp.getPanel(), 
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
            this.marking, 
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
            this.force, 
            Utilities.HORIZONTAL, 
            3, 0, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel, 
            this.entityId.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 1, 
            4, 1, 
            0.5, 0.0, 
            Utilities.getInsets(6, 2, 2, 2));
        Utilities.addComponent(
            panel, 
            this.primary.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 2, 
            6, 1, 
            0.5, 0.0, 
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            panel, 
            this.alternative.getPanel(), 
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
            this.location.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 0, 
            1, 1, 
            0.5, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel, 
            this.velocity.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 1, 
            1, 1, 
            0.5, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel, 
            this.orientation.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 2, 
            1, 1, 
            0.5, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel, 
            this.deadReckoning.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 3, 
            1, 1, 
            0.5, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.setBorder(panel, "Spatial");
        
        return panel;
    }
}
