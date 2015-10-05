/**
 * @author Tony Pinkston
 */
package discover.gui.frames;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import discover.Discover;
import discover.common.Binary;
import discover.common.Hexadecimal;
import discover.gui.Utilities;
import discover.gui.tabs.PlaybackTab;
import discover.vdis.PDU;

@SuppressWarnings("serial")
public class BulkEditorFrame implements ActionListener, MouseListener {

    private static final Logger logger = Discover.getLogger();

    private static final Cursor WAIT_CURSOR;

    private static final String TITLE = "Bulk Editor";
    private static final String INSERT_BYTE = "Insert Byte";
    private static final String REMOVE_BYTE = "Remove Byte";
    private static final String OKAY = "Okay";
    private static final String CANCEL = "Cancel";

    private static final Column COLUMNS[] = Column.values();

    private static BulkEditorFrame instance = null;

    static {

        WAIT_CURSOR = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    }

    private final JFrame frame = new JFrame() {

        @Override
        public void dispose() {

            instance = null;

            cleanup();
            super.dispose();
        }
    };

    private final JTable table = new JTable();
    private final JPopupMenu popup = new JPopupMenu();
    private final JMenuItem insert = new JMenuItem(INSERT_BYTE);
    private final JMenuItem remove = new JMenuItem(REMOVE_BYTE);
    private final JButton okay = new JButton(OKAY);
    private final JButton cancel = new JButton(CANCEL);

    private final TableModel model = new TableModel();

    /** List of PDUs to be modified. */
    private final List<PDU> list = new ArrayList<PDU>();

    /** Playback tab listing PDUs. */
    private final PlaybackTab tab;

    /** Byte locations in PDU data buffers. */
    private int indexes[] = new int[] { 0 };

    /** Byte array to put into PDU data buffers at specified indexes. */
    private byte bytes[] = new byte[] { (byte)0 };

    public static void create(PlaybackTab panel, List<PDU> list) {

        if (instance == null) {

            instance = new BulkEditorFrame(panel, list);
        }
    }

    public static JFrame getFrame() {

        if (instance == null) {

            return null;
        }
        else {

            return instance.frame;
        }
    }

    private BulkEditorFrame(PlaybackTab tab, List<PDU> list) {

        logger.finer("Creating with " + list.size() + " PDUs");

        this.frame.setTitle(TITLE + ": " + tab.getTabName());
        this.tab = tab;
        this.list.addAll(list);

        this.okay.addActionListener(this);
        this.okay.setActionCommand(OKAY);
        this.cancel.addActionListener(this);
        this.cancel.setActionCommand(CANCEL);

        this.table.setModel(this.model);
        this.table.addMouseListener(this);
        this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.setColumnSizes();
        this.fill();

        this.insert.addActionListener(this);
        this.insert.setActionCommand(INSERT_BYTE);
        this.insert.setToolTipText("Inserts new byte before selected byte.");
        this.popup.add(insert);

        this.remove.addActionListener(this);
        this.remove.setActionCommand(REMOVE_BYTE);
        this.remove.setToolTipText("Remove selected byte from byte array.");
        this.popup.add(remove);

        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setPreferredSize(new Dimension(400, 300));
        this.frame.pack();

        Utilities.center(DiscoverFrame.getFrame(), this.frame);

        this.frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();

        if (command == null) {

        }
        else if (command.equals(OKAY)) {

            int option = JOptionPane.showConfirmDialog(
                this.frame,
                "Apply changes to PDUs?",
                TITLE,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {

                this.save();
            }
        }
        else if (command.equals(CANCEL)) {

            this.frame.dispose();
        }
        else if (command.equals(INSERT_BYTE)) {

            this.insertByte();
        }
        else if (command.equals(REMOVE_BYTE)) {

            this.removeByte();
        }
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

    private void save() {

        Cursor original = this.frame.getCursor();
        int total = this.list.size();
        int failures = 0;

        this.frame.setCursor(WAIT_CURSOR);

        // Check for un-editable bytes first.
        for(int j = 0; (j < total); ++j) {

            boolean failure = false;

            for(int i = 0; (i < this.indexes.length) && !failure; ++i) {

                PDU pdu = this.list.get(j);

                if (!PDU.isByteEditable(pdu.getType(), this.indexes[i])) {

                    failure = true;
                }
            }

            failures += (failure ? 1 : 0);
        }

        if (failures > 0) {

            this.frame.setCursor(original);

            this.warning(
                "Cannot edit PDUs, " + failures + " of " + total +
                " have index restrictions.");
        }
        else {

            int count = 0;

            for(PDU pdu : this.list) {

                byte data[] = Arrays.copyOf(pdu.getData(), pdu.getLength());

                for(int i = 0; i < this.indexes.length; ++i) {

                    data[this.indexes[i]] = this.bytes[i];
                }

                pdu.setData(data);
                pdu.decode(true);

                ++count;
            }

            logger.fine("Modified " + count + " PDUs...");

            this.tab.modified(this.list);
            this.frame.setCursor(original);
            this.frame.dispose();
        }
    }

    private void insertByte() {

        final int length = this.indexes.length;
        final int selection = this.table.getSelectedRow();

        int newIndexes[] = new int[length + 1];
        byte newBytes[] = new byte[length + 1];

        for(int i = 0; i < (length + 1); ++i) {

            if (i > selection) {

                newIndexes[i] = (this.indexes[i - 1] + 1);
                newBytes[i] = this.bytes[i - 1];
            }
            else {

                newIndexes[i] = this.indexes[i];

                if (i == selection) {

                    newBytes[i] = (byte)0;
                }
                else {

                    newBytes[i] = this.bytes[i];
                }
            }
        }

        this.indexes = newIndexes;
        this.bytes = newBytes;

        this.model.fireTableDataChanged();
    }

    private void removeByte() {

        final int length = this.indexes.length;

        if (length > 1) {

            final int selection = this.table.getSelectedRow();

            int newIndexes[] = new int[length - 1];
            byte newBytes[] = new byte[length - 1];

            for(int i = 0; i < newBytes.length; ++i) {

                if (i < selection) {

                    newIndexes[i] = this.indexes[i];
                    newBytes[i] = this.bytes[i];
                }
                else if (i == 0) {

                    newIndexes[i] = this.indexes[i + 1];
                    newBytes[i] = this.bytes[i + 1];
                }
                else {

                    newIndexes[i] = (this.indexes[i - 1] + 1);
                    newBytes[i] = this.bytes[i + 1];
                }
            }

            this.indexes = newIndexes;
            this.bytes = newBytes;

            this.model.fireTableDataChanged();
        }
    }

    private void setColumnSizes() {

        TableColumnModel model = this.table.getColumnModel();

        for(Column column : COLUMNS) {

            TableColumn tableColumn = model.getColumn(column.ordinal());

            tableColumn.setResizable(true);
            tableColumn.setPreferredWidth(column.columnWidth + 10);
        }
    }

    private void showPopup(MouseEvent event) {

        if (event.isPopupTrigger()) {

            int selection = this.table.getSelectedRow();

            if (selection != -1) {

                this.remove.setEnabled(this.model.getRowCount() > 1);

                this.popup.show(this.table, event.getX(), event.getY());
            }
        }
    }

    private Object getCellValue(int row, int column) {

        switch(COLUMNS[column]) {

            case INDEX:
                return this.indexes[row];
            case DECIMAL:
                return this.bytes[row];
            case BINARY:
                return Binary.toString8(this.bytes[row]);
            case HEXADECIMAL:
                return Hexadecimal.toString8(this.bytes[row]);
            default:
                return null;
        }
    }

    private void setCellValue(Object object, int row, int column) {

        switch(COLUMNS[column]) {

            case INDEX:
                this.modifyIndex(row, (Integer)object);
                break;
            case DECIMAL:
                this.modifyByte(row, (Byte)object);
                break;
            case BINARY:
                this.modifyBinary(row, (String)object);
                break;
            case HEXADECIMAL:
                this.modifyHexadecimal(row, (String)object);
                break;
        }
    }

    private void modifyIndex(int row, int index) {

        int newIndexes[] = new int[this.indexes.length];

        newIndexes[row] = index;

        for(int i = (row - 1); i > -1; --i) {

            newIndexes[i] = (newIndexes[i + 1] - 1);
        }

        for(int i = (row + 1); i < this.indexes.length; ++i) {

            newIndexes[i] = (newIndexes[i - 1] + 1);
        }

        if (newIndexes[0] < 0) {

            this.error("Buffer index cannot be negative!");
        }
        else {

            this.indexes = newIndexes;

            this.model.fireTableDataChanged();
        }
    }

    private void modifyByte(int row, byte value) {

        this.bytes[row] = value;

        this.model.fireTableDataChanged();
    }

    private void modifyBinary(int row, String value) {

        try {

            Integer number = Integer.parseInt(value, 2);

            this.bytes[row] = number.byteValue();

            this.model.fireTableDataChanged();
        }
        catch(NumberFormatException exception) {

            this.error("Not a valid binary byte value: " + value);
        }
    }

    private void modifyHexadecimal(int row, String value) {

        try {

            Integer number = Integer.parseInt(value, 16);

            this.bytes[row] = number.byteValue();

            this.model.fireTableDataChanged();
        }
        catch(NumberFormatException exception) {

            this.error("Not a valid hexadecimal byte value: " + value);
        }
    }

    private void warning(String message) {

        JOptionPane.showMessageDialog(
            this.frame,
            message,
            TITLE,
            JOptionPane.WARNING_MESSAGE);
    }

    private void error(String message) {

        JOptionPane.showMessageDialog(
            this.frame,
            message,
            TITLE,
            JOptionPane.ERROR_MESSAGE);
    }

    private void cleanup() {

        this.frame.removeAll();
        this.popup.removeAll();

        this.okay.removeActionListener(this);
        this.cancel.removeActionListener(this);
        this.table.removeMouseListener(this);
        this.insert.removeActionListener(this);
        this.remove.removeActionListener(this);
    }

    private void fill() {

        Utilities.setGridBagLayout(this.frame);

        int gridy = 0;

        Utilities.addComponent(
            this.frame,
            new JLabel("Total PDUs to edit: " + this.list.size()),
            Utilities.BOTH,
            0, gridy,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 5, 5, 5));

        ++gridy;

        Utilities.addComponent(
            this.frame,
            new JLabel("(Right click within table below to add/remove bytes.)"),
            Utilities.BOTH,
            0, gridy,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(5, 5, 5, 5));

        ++gridy;

        Utilities.addComponent(
            this.frame,
            new JScrollPane(this.table),
            Utilities.BOTH,
            0, gridy,
            2, 1,
            1.0, 1.0,
            Utilities.getInsets(5, 5, 5, 5));

        ++gridy;

        Utilities.addComponent(
            this.frame,
            this.okay,
            Utilities.HORIZONTAL,
            0, gridy,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(5, 5, 5, 5));
        Utilities.addComponent(
            this.frame,
            this.cancel,
            Utilities.HORIZONTAL,
            1, gridy,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(5, 5, 5, 5));

    }

    enum Column {

        INDEX("Index", Integer.class, 50),
        DECIMAL("Decimal Value", Byte.class, 120),
        BINARY("Binary Value", String.class, 120),
        HEXADECIMAL("Hex Value", String.class, 120);

        final String columnName;
        final Class<?> columnClass;
        final int columnWidth;

        private Column(
            String columnName,
            Class<?> columnClass,
            int columnWidth) {

            this.columnName = columnName;
            this.columnClass = columnClass;
            this.columnWidth = columnWidth;
        }
    }

    /**
     * Table model.
     */
    public class TableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {

            return bytes.length;
        }

        @Override
        public int getColumnCount() {

            return COLUMNS.length;
        }

        @Override
        public Class<?> getColumnClass(int column) {

            return COLUMNS[column].columnClass;
        }

        @Override
        public String getColumnName(int column) {

            return COLUMNS[column].columnName;
        }

        @Override
        public boolean isCellEditable(int row, int column) {

            return true;
        }

        @Override
        public Object getValueAt(int row, int column) {

            return getCellValue(row, column);
        }

        @Override
        public void setValueAt(Object object, int row, int column) {

            setCellValue(object, row, column);
        }
    }
}
