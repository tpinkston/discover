package discover.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import discover.common.ByteArray;
import discover.common.Common;
import discover.common.Hexadecimal;
import discover.common.Version;
import discover.common.buffer.HypertextBuffer;
import discover.common.buffer.PlainTextBuffer;
import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;
import discover.gui.panels.TextPanel;
import discover.vdis.PDU;
import discover.vdis.datum.CDTApplicationConfiguration;
import discover.vdis.datum.CDTGeneralDiscoveryRecord;
import discover.vdis.datum.CDTOneSAFConfigurationRecord;
import discover.vdis.datum.CDTSpecificConnectionRecord;
import discover.vdis.datum.CDTWaypoint;
import discover.vdis.datum.DatumSpecificationRecord;
import discover.vdis.datum.FixedDatumRecord;
import discover.vdis.pdu.ActionRequest;
import discover.vdis.pdu.ApplicationControlPDU;
import discover.vdis.pdu.EntityState;
import discover.vdis.types.EntityTypes;
import discover.vdis.types.Septuple;

/**
 * @author Tony Pinkston
 */
public class BuilderTab extends Tab implements ClipboardTab, MouseListener {

    public static final String BUILDER_TAB = "(Builder Tab)";

    private static final String HTML = "text/html";
    private static final String PLAIN = "text/plain";

    private final JToolBar tools = new JToolBar();
    private final JTable table = new JTable();
    private final JLabel clipboard = new JLabel();
    private final JPopupMenu popup = new JPopupMenu();
    private final InsertBytes insert = new InsertBytes();
    private final DeleteBytes delete = new DeleteBytes();
    private final TableModel model = new TableModel();
    private final TextPanel content = new TextPanel(HTML, null);
    private final TextPanel hexadecimal = new TextPanel(PLAIN, Font.MONOSPACED);

    private byte buffer[] = new byte[12];

    public BuilderTab(String name) {

        super(name, TabType.BUILDER);

        this.table.setModel(this.model);
        this.table.addMouseListener(this);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        this.setPDULength();
        this.updateClipboardStatus(null);
        this.fill();
        this.show();
    }

    @Override
    public void mousePressed(MouseEvent event) {

        this.showPopup(event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {

        this.showPopup(event);
    }

    @Override
    public void mouseClicked(MouseEvent event) {

        this.showPopup(event);
    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseExited(MouseEvent event) {

    }

    @Override
    public void cut(ArrayList<PDU> clipboard) {

        logger.error("Method not supported!");
    }

    @Override
    public void copy(ArrayList<PDU> clipboard) {

        PDU pdu = new PDU(this.buffer);

        pdu.setSource(BUILDER_TAB);
        pdu.setTimestamp(true);
        pdu.setTime(System.currentTimeMillis());

        try {

            pdu.decodeWithoutCatch(true);

            clipboard.add(pdu);
        }
        catch(IOException exception) {

            logger.error("Caught exception!", exception);
        }
    }

    @Override
    public void paste(ArrayList<PDU> clipboard) {

        if (clipboard != null) {

            if (clipboard.size() != 1) {

                JOptionPane.showMessageDialog(
                    DiscoverFrame.getFrame(),
                    "Builder tab can only paste a single PDU...",
                    "Paste (Builder Tab)",
                    JOptionPane.ERROR_MESSAGE);
            }
            else {

                PDU pdu = clipboard.remove(0);

                this.setBuffer(
                    Arrays.copyOf(pdu.getData(), pdu.getLength()),
                    true);
            }
        }
    }

    @Override
    public void updateClipboardStatus(ArrayList<PDU> clipboard) {

        int count = ((clipboard == null) ? 0 : clipboard.size());

        this.clipboard.setText("Clipboard: " + count);
    }

    @Override
    public void load(
        Version version,
        DataInputStream stream) throws IOException {

        // Tab type and name have already been read by LoadWorker.
        int length = stream.readInt();

        byte bytes[] = new byte[length];

        for(int i = 0; i < length; ++i) {

            bytes[i] = stream.readByte();
        }

        this.setBuffer(bytes, true);
    }

    @Override
    public void save(DataOutputStream stream) throws IOException {

        stream.writeInt(super.getTabType().ordinal());
        stream.writeUTF(super.getTabName());

        stream.writeInt(this.buffer.length);

        for(int i = 0; i < this.buffer.length; ++i) {

            stream.writeByte(this.buffer[i]);
        }
    }

    @Override
    public void close() {

        // TODO Auto-generated method stub
    }

    private void setPDULength() {

        if ((this.buffer != null) && (this.buffer.length > 10)) {

            // Set PDU length in PDU header.
            ByteArray.set16Bits(this.buffer, 8, this.buffer.length);
        }
    }

    private int getRows() {

        if (this.buffer == null) {

            return 0;
        }
        else {

            return (this.buffer.length / 4);
        }
    }

    private int getRow(int index) {

        return (index / 4);
    }

    private int getBufferIndex(int column, int row) {

        int index = -1;

        if (column > 0) {

            index = ((row * 4) + (column - 1));
        }

        return index;
    }

    private void setBuffer(byte bytes[], boolean show) {

        if (bytes != null) {

            if (bytes.length > 11) {

                this.buffer = bytes;
                this.model.fireTableStructureChanged();
                this.setPDULength();

                if (show) {

                    this.show();
                }
            }
        }
    }

    private void showPopup(MouseEvent event) {

        if (event.isPopupTrigger()) {

            Point point = event.getPoint();

            int row = this.table.rowAtPoint(point);
            int column = this.table.columnAtPoint(point);

            this.insert.row = row;
            this.insert.column = column;

            this.delete.rows = this.table.getSelectedRows();
            this.delete.setEnabled(this.buffer.length > 12);

            this.popup.show(this.table, point.x, point.y);
        }
    }

    private void show() {

        if (this.buffer == null) {

            this.content.setText("No buffer to display.");
            this.hexadecimal.setText("No buffer to display.");
        }
        else {

            PDU pdu = new PDU(this.buffer);

            pdu.setSource(BUILDER_TAB);
            pdu.setTimestamp(true);
            pdu.setTime(System.currentTimeMillis());

            try {

                pdu.decodeWithoutCatch(true);

                try {

                    HypertextBuffer buffer = new HypertextBuffer();

                    buffer.addText("<html><body>");
                    buffer.addBuffer(pdu);
                    buffer.addText("</body></html>");

                    this.content.setText(buffer.toString());
                    this.content.setCaretPosition(0);
                }
                catch(Exception exception) {

                    logger.error("Caught exception!", exception);
                }

                try {

                    PlainTextBuffer buffer = new PlainTextBuffer();

                    Hexadecimal.toBuffer(
                        buffer,
                        "  -  ",
                        4,
                        false,
                        pdu.getData());

                    this.hexadecimal.setText(buffer.toString());
                    this.hexadecimal.setCaretPosition(0);
                }
                catch(Exception exception) {

                    logger.error("Caught exception!", exception);
                }
            }
            catch(IOException exception) {

                logger.error("Caught exception!", exception);

                this.content.setText("Caught exception, nothing to display.");
                this.hexadecimal.setText("Caught exception, nothing to display.");
            }
        }
    }

    private void fill() {

        JPanel status = new JPanel(new GridBagLayout());
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JTabbedPane tabbed = new JTabbedPane(JTabbedPane.BOTTOM);
        Insets insets = new Insets(3, 3, 3, 20);

        tabbed.add("Content", this.content.getPanel());
        tabbed.add("Byte View", this.hexadecimal.getPanel());

        Utilities.addComponent(
            status,
            this.clipboard,
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            insets);

        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scroller = new JScrollPane(this.table);

        scroller.setPreferredSize(new Dimension(800, 700));

        panel.add(this.tools, BorderLayout.NORTH);
        panel.add(scroller, BorderLayout.CENTER);
        panel.add(status, BorderLayout.SOUTH);

        split.setContinuousLayout(true);
        split.setLeftComponent(panel);
        split.setRightComponent(tabbed);

        this.tools.add(new Apply());
        this.tools.addSeparator();
        this.tools.add(new Custom());

        Utilities.addComponent(
            super.panel,
            split,
            Utilities.BOTH,
            0, 0,
            1, 1,
            1.0, 1.0,
            null);

        split.setDividerLocation(0.5);

        this.popup.add(this.insert);
        this.popup.add(this.delete);

        this.tools.setFloatable(false);
    }

    @SuppressWarnings("serial")
    class TableModel extends AbstractTableModel {

        @Override
        public Class<?> getColumnClass(int column) {

            return String.class;
        }

        @Override
        public String getColumnName(int column) {

            return null;
        }

        @Override
        public boolean isCellEditable(int row, int column) {

            return (column != 0);
        }

        @Override
        public int getColumnCount() {

            return 5;
        }

        @Override
        public int getRowCount() {

            return getRows();
        }

        @Override
        public Object getValueAt(int row, int column) {

            if (column == 0) {

                return Hexadecimal.toString32(row * 4);
            }
            else {

                int index = getBufferIndex(column, row);

                if (index >= buffer.length) {

                    return "00";
                }
                else {

                    return Hexadecimal.toString8(buffer[index]);
                }
            }
        }

        @Override
        public void setValueAt(Object object, int row, int column) {

            int index = getBufferIndex(column, row);

            if (index > -1) {

                try {

                    Number number = Common.getNumber(
                        object.toString(),
                        Common.SIZE8,
                        Common.HEX,
                        false);

                    if (number instanceof Byte) {

                        buffer[index] = number.byteValue();
                    }
                }
                catch(NumberFormatException exception) {

                    // Do nothing...
                }
            }
        }
    }

    @SuppressWarnings("serial")
    class Apply extends AbstractAction {

        public Apply() {

            super("Apply");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            setPDULength();
            show();
        }
    }

    @SuppressWarnings("serial")
    class Custom extends AbstractAction {

        private final String pdus[] = {

            "Entity State PDU (CDT Scripted Entity Creation)",
            "Action Request (CDT General Discovery)",
            "Action Request (CDT Specific Connection)",
            "Action Request (CDT EDE Configuration)",
            "Action Request (CDT OneSAF Configuration)",
            "Action Request (CDT Scripted Entity Waypoints)"
        };

        public Custom() {

            super("Custom");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Object object = JOptionPane.showInputDialog(
                getPanel(),
                "Select PDU:",
                "Select PDU",
                JOptionPane.PLAIN_MESSAGE,
                null,
                this.pdus,
                this.pdus[0]);

            System.out.println(object);

            if (object.equals(pdus[0])) {

                this.createCDTScriptedEntityState();
            }
            else if (object.equals(pdus[1])) {

                this.createCDTGeneralDiscovery();
            }
            else if (object.equals(pdus[2])) {

                this.createCDTSpecificConnection();
            }
            else if (object.equals(pdus[3])) {

                this.createCDTEdeConfiguration();
            }
            else if (object.equals(pdus[4])) {

                this.createCDTOneSAFConfiguration();
            }
            else if (object.equals(pdus[5])) {

                this.createCDTWaypoints();
            }
        }

        public void createCDTScriptedEntityState() {

            EntityState pdu = new EntityState();

            // Truck_Pickup_Toyota_Tech_with_DSHK_MG
            long type = Septuple.toLong(1,1,110,83,1,1,2);

            pdu.getHeader().setExercise(7);
            pdu.getHeader().setProtocol(7); // V-DIS
            pdu.getHeader().setType(1); // ENTITY_STATE
            pdu.getHeader().setFamily(1); // ENTITY_INFORMATION_INTERACTION
            pdu.getHeader().setPadding(1); // CREATE
            pdu.setEntityType(EntityTypes.getEntityType(type));
            pdu.setAlternateType(EntityTypes.getEntityType(type));
            pdu.setForceId(2);
            pdu.getLocation().set(1796079.31, 4946391.45, 3593016.04);
            pdu.getOrientation().setPsi(2.8002f);
            pdu.getOrientation().setTheta(0.0036f);
            pdu.getOrientation().setPhi(-2.1805f);
            pdu.getEntityId().set(1, 31010, 15);
            pdu.getMarking().setCharacterSet(1);
            pdu.getMarking().setMarking("15");

            try {

                byte bytes[] = pdu.write();

                setBuffer(bytes, true);
            }
            catch(Exception exception) {

                logger.error("Caught exception!", exception);
            }
        }

        public void createCDTGeneralDiscovery() {

            ActionRequest pdu = new ActionRequest();
            pdu.getHeader().setExercise(7);
            pdu.getHeader().setProtocol(7); // V-DIS
            pdu.getHeader().setType(16); // ACTION_REQUEST
            pdu.getHeader().setFamily(5); // SIMULATION_MANAGEMENT
            pdu.setActionId(5100); // Action request ID not in V-DIS spec...
            pdu.setRequestId(2);

            CDTGeneralDiscoveryRecord record = new CDTGeneralDiscoveryRecord();

            pdu.getSpecification().getVariable().add(record);

            try {

                byte bytes[] = pdu.write();

                setBuffer(bytes, true);
            }
            catch(Exception exception) {

                logger.error("Caught exception!", exception);
            }
        }

        public void createCDTSpecificConnection() {

            ActionRequest pdu = new ActionRequest();
            pdu.getHeader().setExercise(7);
            pdu.getHeader().setProtocol(7); // V-DIS
            pdu.getHeader().setType(16); // ACTION_REQUEST
            pdu.getHeader().setFamily(5); // SIMULATION_MANAGEMENT
            pdu.setActionId(5200); // Action request ID not in V-DIS spec...
            pdu.setRequestId(30);
            pdu.getOriginator().set(1, 61000, 0);
            pdu.getRecipient().set(1, 31000, 0);

            CDTSpecificConnectionRecord record = new CDTSpecificConnectionRecord();

            pdu.getSpecification().getVariable().add(record);

            try {

                record.senderIPAddress.address[0] = (byte)0x0A;
                record.senderIPAddress.address[1] = (byte)0x0A;
                record.senderIPAddress.address[2] = (byte)0xC9;
                record.senderIPAddress.address[3] = (byte)0x10;

                record.receiverIPAddress.address[0] = (byte)0x0A;
                record.receiverIPAddress.address[1] = (byte)0x0A;
                record.receiverIPAddress.address[2] = (byte)0xC9;
                record.receiverIPAddress.address[3] = (byte)0x12;

                record.stsNumber = 5;

                byte bytes[] = pdu.write();

                setBuffer(bytes, true);
            }
            catch(Exception exception) {

                logger.error("Caught exception!", exception);
            }
        }

        public void createCDTEdeConfiguration() {

            ApplicationControlPDU pdu = new ApplicationControlPDU();
            pdu.getHeader().setExercise(1);
            pdu.getHeader().setProtocol(7); // V-DIS
            pdu.getHeader().setType(200); // APPLICATION_CONTROL
            pdu.getHeader().setFamily(130); // EXPERIMENTAL_V_DIS
            pdu.getOriginator().set(1, 61000, 0);
            pdu.getRecipient().set(1, 2050, 0);
            pdu.setCurrentPart(1);
            pdu.setTotalParts(1);
            pdu.setRequestId(65);
            pdu.setTimeInterval(1024);
            pdu.setControlType(0); // APP_CTRL_CONTROL_OTHER
            pdu.setOriginatorApplicationType(0); // APP_CTRL_APPLICATION_OTHER
            pdu.setRecpientApplicationType(0); // APP_CTRL_APPLICATION_OTHER

            CDTApplicationConfiguration record = new CDTApplicationConfiguration();

            record.receivingIPAddress.address[0] = (byte)0xC0;
            record.receivingIPAddress.address[1] = (byte)0x08;
            record.receivingIPAddress.address[2] = (byte)0x15;
            record.receivingIPAddress.address[3] = (byte)0xCA;

            StringBuilder builder = new StringBuilder();

            // Write ten bytes 100x to make 1000...
            //
            for(int i = 0; i < 100; ++i) {

                builder.append("123456789\n");
            }

            byte configuration[] = builder.toString().getBytes();

            for(int i = 0; i < CDTApplicationConfiguration.CONFIGURATION_LENGTH; ++i) {

                record.configuration[i] = configuration[i];
            }

            pdu.getRecords().add(record);

            try {

                byte bytes[] = pdu.write();

                setBuffer(bytes, true);
            }
            catch(Exception exception) {

                logger.error("Caught exception!", exception);
            }
        }

        public void createCDTOneSAFConfiguration() {

            ApplicationControlPDU pdu = new ApplicationControlPDU();
            pdu.getHeader().setExercise(1);
            pdu.getHeader().setProtocol(7); // V-DIS
            pdu.getHeader().setType(200); // APPLICATION_CONTROL
            pdu.getHeader().setFamily(130); // EXPERIMENTAL_V_DIS
            pdu.getOriginator().set(1, 61000, 0);
            pdu.getRecipient().set(1, 31000, 0);
            pdu.setCurrentPart(1);
            pdu.setTotalParts(1);
            pdu.setRequestId(30);
            pdu.setTimeInterval(1024);
            pdu.setControlType(0); // APP_CTRL_CONTROL_OTHER
            pdu.setOriginatorApplicationType(0); // APP_CTRL_APPLICATION_OTHER
            pdu.setRecpientApplicationType(0); // APP_CTRL_APPLICATION_OTHER

            CDTOneSAFConfigurationRecord record = new CDTOneSAFConfigurationRecord();

            record.port = 3851;
            record.terrain = "vcts04";
            record.stsIPAddress.address[0] = (byte)0x0A;
            record.stsIPAddress.address[1] = (byte)0x0A;
            record.stsIPAddress.address[2] = (byte)0xC9;
            record.stsIPAddress.address[3] = (byte)0x08;

            pdu.getRecords().add(record);

            try {

                byte bytes[] = pdu.write();

                setBuffer(bytes, true);
            }
            catch(Exception exception) {

                logger.error("Caught exception!", exception);
            }
        }

        public void createCDTWaypoints() {

            ActionRequest pdu = new ActionRequest();
            pdu.getHeader().setExercise(1);
            pdu.getHeader().setProtocol(7); // V-DIS
            pdu.getHeader().setType(16); // ACTION_REQUEST
            pdu.getHeader().setFamily(5); // SIMULATION_MANAGEMENT
            pdu.setActionId(5100); // Action request ID not in V-DIS spec...
            pdu.setRequestId(51);
            pdu.getOriginator().set(1, 7030, 0);
            pdu.getRecipient().set(1, 31010, 10);

            FixedDatumRecord fixed1 = new FixedDatumRecord();
            fixed1.setDatumId(404100); // Datum ID not in V-DIS spec...
            fixed1.setDatumValue(1);

            FixedDatumRecord fixed2 = new FixedDatumRecord();
            fixed2.setDatumId(404200); // Datum ID not in V-DIS spec...
            fixed2.setDatumValue(1);

            CDTWaypoint waypoint1 = new CDTWaypoint();
            waypoint1.speed = 4.47f; // 10 mph
            waypoint1.location.set(1796654.1486096582, 4945545.981014356, 3593884.8363324148);
            waypoint1.description.set(0x15);

            CDTWaypoint waypoint2 = new CDTWaypoint();
            waypoint2.speed = 8.9408f; // 20 mph
            waypoint2.location.set(1796511.7360998795, 4945615.47505377, 3593854.358928526);
            waypoint2.description.set(0x0A);

            CDTWaypoint waypoint3 = new CDTWaypoint();
            waypoint3.speed = 4.47f; // 10 mph
            waypoint3.location.set(1796480.916300284, 4945779.871171827, 3593648.41010321);
            waypoint3.description.set(0x00);

            CDTWaypoint waypoint4 = new CDTWaypoint();
            waypoint4.speed = 0.0f;
            waypoint4.location.set(1796595.6543941074, 4945994.198129044, 3593305.594636572);
            waypoint4.description.set(0x0C);

            DatumSpecificationRecord specs = pdu.getSpecification();
            specs.getFixed().add(fixed1);
            specs.getFixed().add(fixed2);
            specs.getVariable().add(waypoint1);
            specs.getVariable().add(waypoint2);
            specs.getVariable().add(waypoint3);
            specs.getVariable().add(waypoint4);

            try {

                byte bytes[] = pdu.write();

                setBuffer(bytes, true);
            }
            catch(Exception exception) {

                logger.error("Caught exception!", exception);
            }
        }
    }

    @SuppressWarnings("serial")
    class InsertBytes extends AbstractAction {

        public int row = 0;
        public int column = 0;

        public InsertBytes() {

            super("Insert Four Bytes");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            int index = 0;

            if (buffer.length == 12) {

                index = 11;
            }
            else if (this.column == 0) {

                index = getBufferIndex(1, this.row);
            }
            else {

                index = getBufferIndex(this.column, this.row);
            }

            if (index < 11) {

                JOptionPane.showMessageDialog(
                    DiscoverFrame.getFrame(),
                    "Cannot insert bytes into PDU header...",
                    "Insert Four Bytes",
                    JOptionPane.ERROR_MESSAGE);
            }
            else {

                byte bytes[] = new byte[buffer.length + 4];
                boolean inserted = false;

                for(int i = 0; i < buffer.length; ++i) {

                    if (i == (index - 1)) {

                        bytes[i] = buffer[i];

                        for(int j = 0; j < 4; ++j) {

                            bytes[i + j + 1] = 0;
                        }

                        inserted = true;
                    }
                    else if (inserted) {

                        bytes[i + 4] = buffer[i];
                    }
                    else {

                        bytes[i] = buffer[i];
                    }
                }

                setBuffer(bytes, false);
            }
        }
    }

    @SuppressWarnings("serial")
    class DeleteBytes extends AbstractAction {

        int rows[] = null;

        public DeleteBytes() {

            super("Delete Selected Rows");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if ((this.rows != null) && (this.rows.length > 0)) {

                int count = (this.rows.length * 4);

                if (this.tooShort(count)) {

                    JOptionPane.showMessageDialog(
                        DiscoverFrame.getFrame(),
                        "Minimum PDU length is twelve...",
                        "Delete Selected Rows",
                        JOptionPane.ERROR_MESSAGE);
                }
                else if (this.headerSelected()) {

                    JOptionPane.showMessageDialog(
                        DiscoverFrame.getFrame(),
                        "Cannot delete PDU header rows (1st three rows)...",
                        "Delete Selected Rows",
                        JOptionPane.ERROR_MESSAGE);
                }
                else {


                    byte bytes[] = new byte[buffer.length - count];
                    int index = 0;

                    for(int i = 0; i < buffer.length; ++i) {

                        if (!this.inDeletedRow(i)) {

                            bytes[index] = buffer[i];
                            index++;
                        }
                    }

                    setBuffer(bytes, false);
                }
            }
        }

        private boolean tooShort(int count) {

            return ((buffer.length - count) < 12);
        }

        private boolean inDeletedRow(int index) {

            int row = getRow(index);

            for(int i = 0; i < this.rows.length; ++i) {

                if (this.rows[i] == row) {

                    return true;
                }
            }

            return false;
        }

        private boolean headerSelected() {

            for(int i = 0; i < this.rows.length; ++i) {

                // Rows 0, 1 and 2 are header rows.
                if (this.rows[i] < 3) {

                    return true;
                }
            }

            return false;
        }
    }
}
