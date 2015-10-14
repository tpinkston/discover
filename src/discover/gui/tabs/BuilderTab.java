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

/**
 * @author Tony Pinkston
 */
@SuppressWarnings("serial")
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

        table.setModel(model);
        table.addMouseListener(this);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        setPDULength();
        updateClipboardStatus(null);
        fill();
        showPDU();
    }

    @Override
    public void mousePressed(MouseEvent event) {

        showPopup(event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {

        showPopup(event);
    }

    @Override
    public void mouseClicked(MouseEvent event) {

        showPopup(event);
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

        PDU pdu = new PDU(buffer);

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

                setBuffer(
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

        setBuffer(bytes, true);
    }

    @Override
    public void save(DataOutputStream stream) throws IOException {

        stream.writeInt(getType().ordinal());
        stream.writeUTF(getName());

        stream.writeInt(buffer.length);

        for(int i = 0; i < buffer.length; ++i) {

            stream.writeByte(buffer[i]);
        }
    }

    @Override
    public void close() {

        // TODO Auto-generated method stub
    }

    private void setPDULength() {

        if ((buffer != null) && (buffer.length > 10)) {

            // Set PDU length in PDU header.
            ByteArray.set16Bits(buffer, 8, buffer.length);
        }
    }

    private int getRows() {

        if (buffer == null) {

            return 0;
        }
        else {

            return (buffer.length / 4);
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

                buffer = bytes;
                model.fireTableStructureChanged();
                setPDULength();

                if (show) {

                    showPDU();
                }
            }
        }
    }

    private void showPopup(MouseEvent event) {

        if (event.isPopupTrigger()) {

            Point point = event.getPoint();

            int row = table.rowAtPoint(point);
            int column = table.columnAtPoint(point);

            insert.row = row;
            insert.column = column;

            delete.rows = table.getSelectedRows();
            delete.setEnabled(buffer.length > 12);

            popup.show(table, point.x, point.y);
        }
    }

    private void showPDU() {

        if (buffer == null) {

            content.setText("No buffer to display.");
            hexadecimal.setText("No buffer to display.");
        }
        else {

            PDU pdu = new PDU(buffer);

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

                    content.setText(buffer.toString());
                    content.setCaretPosition(0);
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

                    hexadecimal.setText(buffer.toString());
                    hexadecimal.setCaretPosition(0);
                }
                catch(Exception exception) {

                    logger.error("Caught exception!", exception);
                }
            }
            catch(IOException exception) {

                logger.error("Caught exception!", exception);

                content.setText("Caught exception, nothing to display.");
                hexadecimal.setText("Caught exception, nothing to display.");
            }
        }
    }

    private void fill() {

        JPanel status = new JPanel(new GridBagLayout());
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JTabbedPane tabbed = new JTabbedPane(JTabbedPane.BOTTOM);
        Insets insets = new Insets(3, 3, 3, 20);

        tabbed.add("Content", content);
        tabbed.add("Byte View", hexadecimal);

        Utilities.addComponent(
            status,
            clipboard,
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            insets);

        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scroller = new JScrollPane(table);

        scroller.setPreferredSize(new Dimension(800, 700));

        panel.add(tools, BorderLayout.NORTH);
        panel.add(scroller, BorderLayout.CENTER);
        panel.add(status, BorderLayout.SOUTH);

        split.setContinuousLayout(true);
        split.setLeftComponent(panel);
        split.setRightComponent(tabbed);

        tools.add(new Apply());
        tools.addSeparator();
        tools.add(new Custom());

        Utilities.addComponent(
            this,
            split,
            Utilities.BOTH,
            0, 0,
            1, 1,
            1.0, 1.0,
            null);

        split.setDividerLocation(0.5);

        popup.add(insert);
        popup.add(delete);

        tools.setFloatable(false);
    }

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

    class Apply extends AbstractAction {

        public Apply() {

            super("Apply");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            setPDULength();
            showPDU();
        }
    }

    class Custom extends AbstractAction {

        public Custom() {

            super("Custom");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

        }
    }

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
            else if (column == 0) {

                index = getBufferIndex(1, row);
            }
            else {

                index = getBufferIndex(column, row);
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

    class DeleteBytes extends AbstractAction {

        int rows[] = null;

        public DeleteBytes() {

            super("Delete Selected Rows");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if ((rows != null) && (rows.length > 0)) {

                int count = (rows.length * 4);

                if (tooShort(count)) {

                    JOptionPane.showMessageDialog(
                        DiscoverFrame.getFrame(),
                        "Minimum PDU length is twelve...",
                        "Delete Selected Rows",
                        JOptionPane.ERROR_MESSAGE);
                }
                else if (headerSelected()) {

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

                        if (!inDeletedRow(i)) {

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

            for(int i = 0; i < rows.length; ++i) {

                if (rows[i] == row) {

                    return true;
                }
            }

            return false;
        }

        private boolean headerSelected() {

            for(int i = 0; i < rows.length; ++i) {

                // Rows 0, 1 and 2 are header rows.
                if (rows[i] < 3) {

                    return true;
                }
            }

            return false;
        }
    }
}
