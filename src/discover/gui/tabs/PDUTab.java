package discover.gui.tabs;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import discover.common.Hexadecimal;
import discover.common.Version;
import discover.common.buffer.HypertextBuffer;
import discover.common.buffer.PlainTextBuffer;
import discover.gui.Utilities;
import discover.gui.dialogs.FilterDialog;
import discover.gui.frames.DiscoverFrame;
import discover.gui.panels.TextPanel;
import discover.vdis.PDU;
import discover.vdis.common.EntityId;
import discover.vdis.enums.PDU_TYPE;

/**
 * @author Tony Pinkston
 */
@SuppressWarnings("serial")
public abstract class PDUTab
        extends Tab
        implements ClipboardTab, ListSelectionListener, MouseListener {

    protected static final DateFormat format = DateFormat.getDateTimeInstance();

    private static final Column COLUMNS[] = Column.values();
    private static final String HTML = "text/html";
    private static final String PLAIN = "text/plain";

    // TODO: Make private:
    protected final JTable table = new JTable();
    protected final JToolBar tools = new JToolBar();
    protected final JLabel total = new JLabel();
    protected final JLabel visible = new JLabel();
    protected final JLabel clipboard = new JLabel();
    protected final JPopupMenu popup = new JPopupMenu();
    protected final ArrayList<PDU> list = new ArrayList<PDU>();
    protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected final TableModel model = new TableModel();
    protected final TableFilter filter = new TableFilter();
    protected final TableRowSorter<TableModel> sorter;
    protected final TextPanel content = new TextPanel(HTML, null);
    protected final TextPanel hexadecimal = new TextPanel(PLAIN, Font.MONOSPACED);

    protected boolean scrollLocked = true;

    protected PDU current = null;

    protected PDUTab(String name, TabType type) {

        super(name, type);

        tools.setFloatable(false);

        sorter = new TableRowSorter<TableModel>(model);
        sorter.setRowFilter(filter);

        table.setModel(model);
        table.setRowSorter(sorter);
        table.getSelectionModel().addListSelectionListener(this);
        table.addMouseListener(this);

        popup.add(new WriteAction());
        popup.addSeparator();
        popup.add(new ApplyFilterAction());
        popup.addSeparator();
        popup.add(new EntityAction());
        popup.addSeparator();
        popup.add(DiscoverFrame.getInstance().getCutAction());
        popup.add(DiscoverFrame.getInstance().getCopyAction());
        popup.add(DiscoverFrame.getInstance().getPasteAction());
        popup.addSeparator();
        popup.add(DiscoverFrame.getInstance().getDeleteAction());
        popup.add(DiscoverFrame.getInstance().getDeleteAllAction());

        TableColumnModel columnModel = table.getColumnModel();

        for(Column column : COLUMNS) {

            TableColumn tableColumn = columnModel.getColumn(column.ordinal());

            tableColumn.setMinWidth(column.widthMinimum);
            tableColumn.setMaxWidth(column.widthMaximum);
            tableColumn.setPreferredWidth(column.widthPreferred);
            tableColumn.setResizable(true);

            sorter.setComparator(
                column.ordinal(),
                column.columnComparator);
        }

        updateTotalStatus();
        updateVisibleStatus();
        updateClippedStatus(0);
    }

    public abstract void delete();

    public abstract void deleteAll();

    @Override
    public void load(Version version, DataInputStream stream)
        throws IOException {

        // Tab type and name have already been read by LoadWorker.
        int count = stream.readInt();

        for(int i = 0; i < count; ++i) {

            PDU pdu = PDU.create(stream);

            if (pdu != null) {

                list.add(pdu);
            }
        }

        model.fireTableDataChanged();
    }

    @Override
    public void save(DataOutputStream stream) throws IOException {

        stream.writeInt(getType().ordinal());
        stream.writeUTF(getName());

        lock.readLock().lock();

        stream.writeInt(list.size());

        for(int i = 0; i < list.size(); ++i) {

            list.get(i).save(stream);
        }

        lock.readLock().unlock();
    }

    /**
     * Used only when loading PDU data from file.
     *
     * @param pdu - {@link PDU}
     */
    public void addPDU(PDU pdu) {

        list.add(pdu);
    }

    public List<PDU> getSelectedPDUs() {

        List<PDU> list = new ArrayList<PDU>();

        lock.readLock().lock();
        int selection[] = table.getSelectedRows();
        lock.readLock().unlock();

        for(int i = 0; i < selection.length; ++i) {

            int index = sorter.convertRowIndexToModel(selection[i]);

            list.add(getPDU(index));
        }

        return list;
    }

    public PDU getSelectedPDU() {

        PDU pdu = null;

        lock.readLock().lock();
        int selection[] = table.getSelectedRows();
        lock.readLock().unlock();

        if (selection.length == 1) {

            pdu = getPDU(selection[0]);
        }

        return pdu;
    }

    /**
     * Returns copy of PDU list.
     *
     * @return List of {@link PDU}
     */
    public List<PDU> getListCopy() {

        List<PDU> copy = new ArrayList<PDU>();

        lock.readLock().lock();
        copy.addAll(list);
        lock.readLock().unlock();

        return copy;
    }

    /**
     * @return Number of PDUs in the list.
     */
    public int getPDUCount() {

        lock.readLock().lock();
        int count = list.size();
        lock.readLock().unlock();

        return count;
    }

    /**
     * @return Number of PDUs in the list that are visible in JTable.
     */
    public int getVisiblePDUCount() {

        lock.readLock().lock();
        int count = sorter.getViewRowCount();
        lock.readLock().unlock();

        return count;
    }

    @Override
    public void updateClipboardStatus(ArrayList<PDU> clipboard) {

        updateClippedStatus((clipboard == null) ? 0 : clipboard.size());
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
    public void valueChanged(ListSelectionEvent event) {

        if (!event.getValueIsAdjusting()) {

            int row = table.getSelectedRow();

            if (row > -1) {

                row = sorter.convertRowIndexToModel(row);

                showPDU(getPDU(row));
            }
        }
    }

    public void writeTextTo(File file, List<PDU> selections) {

        List<PDU> list = selections;

        if (list == null) {

            list = getListCopy();
        }

        if (!list.isEmpty()) {

            TextWriter writer = new TextWriter(file, list);

            writer.start();
        }
    }

    protected String getThreadName(Integer port) {

        return (getName() + "[" + port + "]");
    }

    protected void clearAll() {

        lock.writeLock().lock();
        list.clear();
        lock.writeLock().unlock();

        model.fireTableDataChanged();

        showPDU(null);
    }

    protected void showPDU() {

        showPDU(current);
        showPDU(current);
    }

    protected void showPDU(PDU pdu) {

        current = pdu;

        if (current == null) {

            content.setText("");
            hexadecimal.setText("");
        }
        else {

            try {

                HypertextBuffer buffer = new HypertextBuffer();

                buffer.addText("<html><body>");
                buffer.addBuffer(current);
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
    }

    protected void showPopup(MouseEvent event) {

        if (event.isPopupTrigger()) {

            lock.readLock().lock();
            int selections[] = table.getSelectedRows();
            lock.readLock().unlock();

            if ((selections != null) && (selections.length > 0)) {

                popup.show(table, event.getX(), event.getY());
            }
        }
    }

    protected void updateTotalStatus() {

        total.setText("Total: " + getPDUCount());
    }

    protected void updateVisibleStatus() {

        visible.setText("Visible: " + getVisiblePDUCount());
    }

    protected void updateClippedStatus(int count) {

        clipboard.setText("Clipboard: " + count);
    }

    /**
     * @param index - int
     *
     * @return {@link PDU} at the given index.
     */
    protected PDU getPDU(int index) {

        lock.readLock().lock();
        PDU pdu = list.get(index);
        lock.readLock().unlock();

        return pdu;
    }

    /**
     * @param index - int
     *
     * @return {@link PDU} at the given index before it is removed.
     */
    protected PDU cutPDU(int index) {

        lock.readLock().lock();
        PDU pdu = list.remove(index);
        lock.readLock().unlock();

        return pdu;
    }

    protected boolean isEditable(int row, int column) {

        return false;
    }

    protected void setCellValue(int row, int column, Object object) {

    }

    protected Object getCellValue(int row, int column) {

        if (column == 0) {

            return new Integer(row);
        }
        else {

            PDU pdu = getPDU(row);

            switch(COLUMNS[column]) {

                case INDEX:
                    return new Integer(row);
                case PORT:
                    return new Integer(pdu.getPort());
                case EXERCISE:
                    return new Integer(pdu.getExercise());
                case TYPE:
                    return pdu.getTitle();
                case INITIATOR:
                    return pdu.getId();
                case TIMESTAMP:
                    return pdu.getTimestamp();
                case TIME:
                    return format.format(pdu.getTime());
                case SOURCE:
                    return pdu.getSource();
            }

            return null;
        }
    }

    public void refresh() {

        model.fireTableDataChanged();
    }

    /**
     * Defines columns for table.
     */
    public static enum Column {

        INDEX("Index", Integer.class, 20, 40, 70),
        EXERCISE("Exercise", Integer.class, 30, 50, 70),
        PORT("Port", Integer.class, 20, 40, 70),
        TYPE("Type", String.class, 50, 80, 300),
        INITIATOR("Initiator", EntityId.class, 50, 70, 200),
        TIMESTAMP("Timestamp", String.class, 50, 90, 200),
        TIME("Time", String.class, 50, 100, 200),
        SOURCE("Source", String.class, 50, 90, 200);

        final String columnName;
        final Class<?> columnClass;
        final Comparator<?> columnComparator;
        final int widthPreferred;
        final int widthMinimum;
        final int widthMaximum;

        private Column(
                String columnName,
                Class<?> columnClass,
                int widthMinimum,
                int widthPreferred,
                int widthMaximum) {

            this.columnName = columnName;
            this.columnClass = columnClass;
            columnComparator = Utilities.getComparator(columnClass);
            this.widthPreferred = widthPreferred;
            this.widthMinimum = widthMinimum;
            this.widthMaximum = widthMaximum;
        }
    }

    public class TableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {

            return getPDUCount();
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

            return isEditable(row, column);
        }

        @Override
        public Object getValueAt(int row, int column) {

            return getCellValue(row, column);
        }

        @Override
        public void setValueAt(Object object, int row, int column) {

            setCellValue(row, column, object);
        }

        @Override
        public void fireTableDataChanged() {

            super.fireTableDataChanged();
            updateTotalStatus();
            updateVisibleStatus();
        }
    }

    public class TableFilter extends RowFilter<TableModel, Integer> {

        public List<PDU_TYPE> includedTypes = new ArrayList<>();
        public List<PDU_TYPE> excludedTypes = new ArrayList<>();
        public Integer port = null;
        public Integer exercise = null;
        public Integer protocol = null;
        public Integer family = null;
        public Integer domain = null;
        public Integer kind = null;
        public Integer site = null;
        public Integer application = null;
        public Integer entity = null;
        public Integer request = null;
        public String marking = null;

        @Override
        public boolean include(RowFilter.Entry<
            ? extends TableModel,
            ? extends Integer> entry) {

            PDU pdu = getPDU(entry.getIdentifier());

            if (exclude(pdu.getTypeEnum()) ||
                exclude(request, pdu.getRequestId()) ||
                exclude(port, pdu.getPort()) ||
                exclude(exercise, pdu.getExercise()) ||
                exclude(protocol, pdu.getProtocol()) ||
                exclude(family, pdu.getFamily())) {

                return false;
            }

            if (pdu.getTypeEnum() == PDU_TYPE.ENTITY_STATE) {

                if (exclude(site, pdu.getSiteId()) ||
                    exclude(application, pdu.getApplicationId()) ||
                    exclude(entity, pdu.getEntityId())) {

                    return false;
                }

                if ((marking != null) && !marking.isEmpty()) {

                    if (!pdu.getMarking().matches(marking)) {

                        return false;
                    }
                }
            }

            if (pdu.hasEntityType()) {

                if (exclude(kind, pdu.getEntityKind())) {

                    return false;
                }

                if (exclude(domain, pdu.getEntityDomain())) {

                    return false;
                }
            }

            return true;
        }

        @Override
        public String toString() {

            StringBuffer buffer = new StringBuffer();

            buffer.append("typesIncluded: " + includedTypes + "\n");
            buffer.append("typesExcluded: " + excludedTypes + "\n");
            buffer.append("port: " + port + "\n");
            buffer.append("exercise: " + exercise + "\n");
            buffer.append("protocol: " + protocol + "\n");
            buffer.append("family: " + family + "\n");
            buffer.append("domain: " + domain + "\n");
            buffer.append("kind: " + kind + "\n");
            buffer.append("site: " + site + "\n");
            buffer.append("application: " + application + "\n");
            buffer.append("entity: " + entity + "\n");
            buffer.append("request: " + request + "\n");
            buffer.append("marking: " + marking + "\n");

            return buffer.toString();
        }

        private boolean exclude(Integer object, int value) {

            return ((object == null) ? false : !(object.intValue() == value));
        }

        private boolean exclude(PDU_TYPE type) {

            if (excludedTypes.contains(type)) {

                return true;
            }
            else if (!includedTypes.isEmpty()) {

                return !includedTypes.contains(type);
            }

            return false;
        }
    }

    class FilterDialogAction extends AbstractAction {

        public FilterDialogAction() {

            super("Filter PDUs");

            putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("filter.png"));
            putValue(
                Action.SHORT_DESCRIPTION,
                "Filter PDUs.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            new FilterDialog(getName(), model, filter);
        }
    }

    class ApplyFilterAction extends AbstractAction {

        public ApplyFilterAction() {

            super("Apply to Filter");

            putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("filter.png"));
            putValue(
                Action.SHORT_DESCRIPTION,
                "Applies the selected PDU to the filter.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            List<PDU> selections = getSelectedPDUs();

            if (selections.size() == 1) {

                new FilterDialog(
                    getName(),
                    selections.get(0),
                    model,
                    filter);
            }
        }
    }

    class EntityAction extends AbstractAction {

        public EntityAction() {

            super("Create Entity Tab");

            putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("person.png"));
            putValue(
                Action.SHORT_DESCRIPTION,
                "Creates Entity Tab using the selected Entity State PDU.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            List<PDU> selections = getSelectedPDUs();

            if (selections.size() == 1) {

                PDU pdu = selections.get(0);

                if (pdu.getTypeEnum() == PDU_TYPE.ENTITY_STATE) {

                    // Decode to ensure that EntityState object gets created.
                    pdu.decode(false);

                    DiscoverFrame.getInstance().createEntityTab(pdu);
                }
            }
        }
    }

    class ScrollAction extends AbstractAction {

        private Icon lockedIcon = Utilities.getImageIcon(
            "scroll_locked.png");
        private Icon unlockedIcon = Utilities.getImageIcon(
            "scroll_unlocked.png");

        public ScrollAction() {

            super("Toggle Auto-scroll");

            putValue(
                Action.SMALL_ICON,
                lockedIcon);
            putValue(
                Action.SHORT_DESCRIPTION,
                "Locks PDU table auto-scrolling when PDUs are added.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            scrollLocked = !scrollLocked;

            putValue(
                Action.SMALL_ICON,
                (scrollLocked ? lockedIcon : unlockedIcon));
        }
    }

    class WriteAction extends AbstractAction {

        private static final String WRITE = "Write to Text File";

        private JFileChooser chooser = null;

        public WriteAction() {

            super(WRITE);

            putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("file_save.gif"));
            putValue(
                Action.SHORT_DESCRIPTION,
                "Writes selected PDU(s) to text file.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            List<PDU> selections = getSelectedPDUs();

            if (chooser == null) {

                chooser = new JFileChooser(
                    DiscoverFrame.getInstance().getSavedDataPath());
            }

            if (selections.size() > 0) {

                File file = Utilities.getSaveFile(WRITE, chooser);

                if (file != null) {

                    writeTextTo(file, selections);
                }
            }
        }
    }

    static class TextWriter extends Thread {

        private static int counter = 0;

        final File file;
        final List<PDU> pdus;

        public TextWriter(File file, List<PDU> pdus) {

            super("TextWriter-" + ++(counter));

            this.file = file;
            this.pdus = pdus;
        }

        @Override
        public void run() {

            logger.info("Thread started: " + getName());

            try {

                PlainTextBuffer buffer = new PlainTextBuffer();

                for(PDU pdu : pdus) {

                    buffer.addBuffer(pdu);
                    buffer.addBreak();
                    buffer.addText("~~~~~~~~~~~~~~~~~~~~ ");
                    buffer.addText("BUFFER ~~~~~~~~~~~~~~~~~~~~~");
                    buffer.addBreak();

                    Hexadecimal.toBuffer(buffer, "  -  ", 8, false, pdu.getData());

                    buffer.addText("~~~~~~~~~~~~~~~~~~~~~~~~");
                    buffer.addText("~~~~~~~~~~~~~~~~~~~~~~~~~");
                    buffer.addBreak();
                }

                logger.info("Text writing complete...");

                OutputStreamWriter writer = getWriter();

                writer.append(buffer.toString());
                writer.close();
            }
            catch(Exception exception) {

                logger.error("Caught exception!", exception);
            }

            logger.info("Thread finished: {}", getName());
        }

        private OutputStreamWriter getWriter() throws FileNotFoundException {

            return new OutputStreamWriter(new FileOutputStream(file));
        }
    }
}
