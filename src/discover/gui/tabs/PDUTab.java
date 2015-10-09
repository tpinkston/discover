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
import discover.vdis.enums.VDIS;
import discover.vdis.enums.VDIS.Handle;

/**
 * @author Tony Pinkston
 */
public abstract class PDUTab
    extends Tab
    implements ClipboardTab, ListSelectionListener, MouseListener {

    protected static final DateFormat format = DateFormat.getDateTimeInstance();

    private static final Column COLUMNS[] = Column.values();
    private static final String HTML = "text/html";
    private static final String PLAIN = "text/plain";

    protected static Handle PDU_TYPE = null;

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

        if (PDU_TYPE == null) {

            PDU_TYPE = VDIS.getHandle(VDIS.PDU_TYPE);
        }

        this.tools.setFloatable(false);

        this.sorter = new TableRowSorter<TableModel>(this.model);
        this.sorter.setRowFilter(this.filter);

        this.table.setModel(this.model);
        this.table.setRowSorter(this.sorter);
        this.table.getSelectionModel().addListSelectionListener(this);
        this.table.addMouseListener(this);

        this.popup.add(new WriteAction());
        this.popup.addSeparator();
        this.popup.add(new ApplyFilterAction());
        this.popup.addSeparator();
        this.popup.add(new EntityAction());
        this.popup.addSeparator();
        this.popup.add(DiscoverFrame.getInstance().getCutAction());
        this.popup.add(DiscoverFrame.getInstance().getCopyAction());
        this.popup.add(DiscoverFrame.getInstance().getPasteAction());
        this.popup.addSeparator();
        this.popup.add(DiscoverFrame.getInstance().getDeleteAction());
        this.popup.add(DiscoverFrame.getInstance().getDeleteAllAction());

        TableColumnModel columnModel = this.table.getColumnModel();

        for(Column column : COLUMNS) {

            TableColumn tableColumn = columnModel.getColumn(column.ordinal());

            tableColumn.setMinWidth(column.widthMinimum);
            tableColumn.setMaxWidth(column.widthMaximum);
            tableColumn.setPreferredWidth(column.widthPreferred);
            tableColumn.setResizable(true);

            this.sorter.setComparator(
                column.ordinal(),
                column.columnComparator);
        }

        this.updateTotalStatus();
        this.updateVisibleStatus();
        this.updateClippedStatus(0);
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

                this.list.add(pdu);
            }
        }

        this.model.fireTableDataChanged();
    }

    @Override
    public void save(DataOutputStream stream) throws IOException {

        stream.writeInt(super.getTabType().ordinal());
        stream.writeUTF(super.getTabName());

        this.lock.readLock().lock();

        stream.writeInt(this.list.size());

        for(int i = 0; i < this.list.size(); ++i) {

            this.list.get(i).save(stream);
        }

        this.lock.readLock().unlock();
    }

    /**
     * Used only when loading PDU data from file.
     *
     * @param pdu - {@link PDU}
     */
    public void addPDU(PDU pdu) {

        this.list.add(pdu);
    }

    public List<PDU> getSelectedPDUs() {

        List<PDU> list = new ArrayList<PDU>();

        this.lock.readLock().lock();
        int selection[] = this.table.getSelectedRows();
        this.lock.readLock().unlock();

        for(int i = 0; i < selection.length; ++i) {

            int index = this.sorter.convertRowIndexToModel(selection[i]);

            list.add(this.getPDU(index));
        }

        return list;
    }

    public PDU getSelectedPDU() {

        PDU pdu = null;

        this.lock.readLock().lock();
        int selection[] = this.table.getSelectedRows();
        this.lock.readLock().unlock();

        if (selection.length == 1) {

            pdu = this.getPDU(selection[0]);
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

        this.lock.readLock().lock();
        copy.addAll(this.list);
        this.lock.readLock().unlock();

        return copy;
    }

    /**
     * @return Number of PDUs in the list.
     */
    public int getPDUCount() {

        this.lock.readLock().lock();
        int count = this.list.size();
        this.lock.readLock().unlock();

        return count;
    }

    /**
     * @return Number of PDUs in the list that are visible in JTable.
     */
    public int getVisiblePDUCount() {

        this.lock.readLock().lock();
        int count = this.sorter.getViewRowCount();
        this.lock.readLock().unlock();

        return count;
    }

    @Override
    public void updateClipboardStatus(ArrayList<PDU> clipboard) {

        this.updateClippedStatus((clipboard == null) ? 0 : clipboard.size());
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
    public void valueChanged(ListSelectionEvent event) {

        if (!event.getValueIsAdjusting()) {

            int row = this.table.getSelectedRow();

            if (row > -1) {

                row = this.sorter.convertRowIndexToModel(row);

                this.show(this.getPDU(row));
            }
        }
    }

    public void writeTextTo(File file, List<PDU> selections) {

        List<PDU> list = selections;

        if (list == null) {

            list = this.getListCopy();
        }

        if (!list.isEmpty()) {

            TextWriter writer = new TextWriter(file, list);

            writer.start();
        }
    }

    protected String getThreadName(Integer port) {

        return (super.getTabName() + "[" + port + "]");
    }

    protected void clearAll() {

        this.lock.writeLock().lock();
        this.list.clear();
        this.lock.writeLock().unlock();

        this.model.fireTableDataChanged();

        this.show(null);
    }

    protected void show() {

        this.show(this.current);
        this.show(this.current);
    }

    protected void show(PDU pdu) {

        this.current = pdu;

        if (this.current == null) {

            this.content.setText("");
            this.hexadecimal.setText("");
        }
        else {

            try {

                HypertextBuffer buffer = new HypertextBuffer();

                buffer.addText("<html><body>");
                buffer.addBuffer(this.current);
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
    }

    protected void showPopup(MouseEvent event) {

        if (event.isPopupTrigger()) {

            this.lock.readLock().lock();
            int selections[] = this.table.getSelectedRows();
            this.lock.readLock().unlock();

            if ((selections != null) && (selections.length > 0)) {

                this.popup.show(this.table, event.getX(), event.getY());
            }
        }
    }

    protected void updateTotalStatus() {

        this.total.setText("Total: " + this.getPDUCount());
    }

    protected void updateVisibleStatus() {

        this.visible.setText("Visible: " + this.getVisiblePDUCount());
    }

    protected void updateClippedStatus(int count) {

        this.clipboard.setText("Clipboard: " + count);
    }

    /**
     * @param index - int
     *
     * @return {@link PDU} at the given index.
     */
    protected PDU getPDU(int index) {

        this.lock.readLock().lock();
        PDU pdu = this.list.get(index);
        this.lock.readLock().unlock();

        return pdu;
    }

    /**
     * @param index - int
     *
     * @return {@link PDU} at the given index before it is removed.
     */
    protected PDU cutPDU(int index) {

        this.lock.readLock().lock();
        PDU pdu = this.list.remove(index);
        this.lock.readLock().unlock();

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

            PDU pdu = this.getPDU(row);

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
            this.columnComparator = Utilities.getComparator(columnClass);
            this.widthPreferred = widthPreferred;
            this.widthMinimum = widthMinimum;
            this.widthMaximum = widthMaximum;
        }
    }

    @SuppressWarnings("serial")
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

        public List<Integer> includedTypes = new ArrayList<Integer>();
        public List<Integer> excludedTypes = new ArrayList<Integer>();
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

            if (this.exclude(pdu.getType()) ||
                this.exclude(this.request, pdu.getRequestId()) ||
                this.exclude(this.port, pdu.getPort()) ||
                this.exclude(this.exercise, pdu.getExercise()) ||
                this.exclude(this.protocol, pdu.getProtocol()) ||
                this.exclude(this.family, pdu.getFamily())) {

                return false;
            }

            if (pdu.getType() == VDIS.PDU_TYPE_ENTITY_STATE) {

                if (this.exclude(this.site, pdu.getSiteId()) ||
                    this.exclude(this.application, pdu.getApplicationId()) ||
                    this.exclude(this.entity, pdu.getEntityId())) {

                    return false;
                }

                if ((this.marking != null) && !this.marking.isEmpty()) {

                    if (!pdu.getMarking().matches(this.marking)) {

                        return false;
                    }
                }
            }

            if (pdu.hasEntityType()) {

                if (this.exclude(this.kind, pdu.getEntityKind())) {

                    return false;
                }

                if (this.exclude(this.domain, pdu.getEntityDomain())) {

                    return false;
                }
            }

            return true;
        }

        public String toString() {

            StringBuffer buffer = new StringBuffer();

            buffer.append("typesIncluded: " + this.includedTypes + "\n");
            buffer.append("typesExcluded: " + this.excludedTypes + "\n");
            buffer.append("port: " + this.port + "\n");
            buffer.append("exercise: " + this.exercise + "\n");
            buffer.append("protocol: " + this.protocol + "\n");
            buffer.append("family: " + this.family + "\n");
            buffer.append("domain: " + this.domain + "\n");
            buffer.append("kind: " + this.kind + "\n");
            buffer.append("site: " + this.site + "\n");
            buffer.append("application: " + this.application + "\n");
            buffer.append("entity: " + this.entity + "\n");
            buffer.append("request: " + this.request + "\n");
            buffer.append("marking: " + this.marking + "\n");

            return buffer.toString();
        }

        private boolean exclude(Integer object, int value) {

            return ((object == null) ? false : !(object.intValue() == value));
        }

        private boolean exclude(int type) {

            if (this.excludedTypes.contains(type)) {

                return true;
            }
            else if (!this.includedTypes.isEmpty()) {

                return !this.includedTypes.contains(type);
            }

            return false;
        }
    }

    @SuppressWarnings("serial")
    class FilterDialogAction extends AbstractAction {

        public FilterDialogAction() {

            super("Filter PDUs");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("filter.png"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Filter PDUs.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            new FilterDialog(getTabName(), model, filter);
        }
    }

    @SuppressWarnings("serial")
    class ApplyFilterAction extends AbstractAction {

        public ApplyFilterAction() {

            super("Apply to Filter");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("filter.png"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Applies the selected PDU to the filter.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            List<PDU> selections = getSelectedPDUs();

            if (selections.size() == 1) {

                new FilterDialog(
                    getPanel().getName(),
                    selections.get(0),
                    model,
                    filter);
            }
        }
    }

    @SuppressWarnings("serial")
    class EntityAction extends AbstractAction {

        public EntityAction() {

            super("Create Entity Tab");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("person.png"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Creates Entity Tab using the selected Entity State PDU.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            List<PDU> selections = getSelectedPDUs();

            if (selections.size() == 1) {

                PDU pdu = selections.get(0);

                if (pdu.getType() == VDIS.PDU_TYPE_ENTITY_STATE) {

                    // Decode to ensure that EntityState object gets created.
                    pdu.decode(false);

                    DiscoverFrame.getInstance().createEntityTab(pdu);
                }
            }
        }
    }

    @SuppressWarnings("serial")
    class ScrollAction extends AbstractAction {

        private Icon lockedIcon = Utilities.getImageIcon(
            "scroll_locked.png");
        private Icon unlockedIcon = Utilities.getImageIcon(
            "scroll_unlocked.png");

        public ScrollAction() {

            super("Toggle Auto-scroll");

            super.putValue(
                Action.SMALL_ICON,
                this.lockedIcon);
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Locks PDU table auto-scrolling when PDUs are added.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            scrollLocked = !scrollLocked;

            super.putValue(
                Action.SMALL_ICON,
                (scrollLocked ? this.lockedIcon : this.unlockedIcon));
        }
    }

    @SuppressWarnings("serial")
    class WriteAction extends AbstractAction {

        private static final String WRITE = "Write to Text File";

        private JFileChooser chooser = null;

        public WriteAction() {

            super(WRITE);

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("file_save.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Writes selected PDU(s) to text file.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            List<PDU> selections = getSelectedPDUs();

            if (this.chooser == null) {

                this.chooser = new JFileChooser(
                    DiscoverFrame.getInstance().getSavedDataPath());
            }

            if (selections.size() > 0) {

                File file = Utilities.getSaveFile(WRITE, this.chooser);

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

                for(PDU pdu : this.pdus) {

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

                OutputStreamWriter writer = this.getWriter();

                writer.append(buffer.toString());
                writer.close();
            }
            catch(Exception exception) {

                logger.error("Caught exception!", exception);
            }

            logger.info("Thread finished: {}", getName());
        }

        private OutputStreamWriter getWriter() throws FileNotFoundException {

            return new OutputStreamWriter(new FileOutputStream(this.file));
        }
    }
}
