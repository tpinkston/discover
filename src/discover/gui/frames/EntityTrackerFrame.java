package discover.gui.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.gui.Utilities;
import discover.vdis.Entity;
import discover.vdis.PDU;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;
import discover.vdis.types.EntityType;

/**
 * @author Tony Pinkston
 */
public class EntityTrackerFrame implements ActionListener, KeyListener, ListSelectionListener {

    private static final Logger logger = LoggerFactory.getLogger(EntityTrackerFrame.class);

    private static final int MINIMUM_WIDTH = 700;
    private static final int MINIMUM_HEIGHT = 500;
    private static final int PREFERRED_WIDTH = 900;
    private static final int PREFERRED_HEIGHT = 700;

    private static final DateFormat dateFormat;
    private static final NumberFormat numberFormatter;

    /** Duration at which entities get checked for last update. */
    private final static long PURGE_DURATION = 5000; // 5 seconds

    private final static String TITLE = "Entity Tracker";
    private final static String ENTITY_COUNT = "Entity Count: ";
    private final static String ENTITY_EXPIRATION = "Entity Expiration";

    private static final Column COLUMNS[] = Column.values();

    static {

        dateFormat = DateFormat.getDateTimeInstance();
        numberFormatter = NumberFormat.getInstance();
        numberFormatter.setMinimumFractionDigits(1);
        numberFormatter.setMaximumFractionDigits(1);
    }

    private final JFrame frame = new JFrame();
    private final JTable table = new JTable();
    private final JEditorPane general = new JEditorPane();
    private final JEditorPane associations = new JEditorPane();
    private final JEditorPane articulations = new JEditorPane();
    private final JEditorPane appearance = new JEditorPane();
    private final JEditorPane transmitters = new JEditorPane();
    private final JEditorPane warfare = new JEditorPane();
    private final JEditorPane emissions = new JEditorPane();
    private final JLabel time = new JLabel();
    private final JLabel count = new JLabel(ENTITY_COUNT + "0");
    private final JMenuItem setThreshold;
    private final JCheckBoxMenuItem toggleExpiration;
    private final JCheckBoxMenuItem trackTransmitters;
    private final JCheckBoxMenuItem trackEmissions;

    private final List<JEditorPane> panes = new ArrayList<JEditorPane>();

    private final TableModel model = new TableModel();
    private final TableRowSorter<TableModel> sorter;

    private final Date date = new Date(0);
    private final Timer timer;

    private final List<Entity> list = new ArrayList<Entity>();

    /** Currently selection entity. */
    private Entity current = null;

    /** Purge expired entities if true. */
    private boolean purgeEntities = false;

    /** False if ignoring Transmission PDUs. */
    private boolean trackingTransmitters = false;

    /** False if ignoring EM emissions PDUs. */
    private boolean trackingEmissions = false;

    /** Time since last PDU at which entity is purged (milliseconds). */
    private long purgeThreshold = 60000L;

    /** Time of last purge (milliseconds). */
    private long lastPurge = 0L;

    public EntityTrackerFrame(String title) {

        this.sorter = new TableRowSorter<TableModel>(this.model);

        this.timer = new Timer(500, this);
        this.timer.start();

        this.setThreshold = new JMenuItem("Set Threshold");
        this.setThreshold.addActionListener(this);
        this.setThreshold.setEnabled(false);
        this.setThreshold.setToolTipText(
            "Set the time duration at which entities expire.");

        this.toggleExpiration = new JCheckBoxMenuItem("Expiration");
        this.toggleExpiration.setSelected(false);
        this.toggleExpiration.addActionListener(this);
        this.toggleExpiration.setToolTipText(
            "Select to automatically remove expired entities " +
            "(no longer publishing entity states).");

        this.trackTransmitters = new JCheckBoxMenuItem("Track Transmitters");
        this.trackTransmitters.setSelected(this.trackingTransmitters);
        this.trackTransmitters.addActionListener(this);

        this.trackEmissions = new JCheckBoxMenuItem("Track Emissions");
        this.trackEmissions.setSelected(this.trackingEmissions);
        this.trackEmissions.addActionListener(this);

        this.panes.add(this.general);
        this.panes.add(this.associations);
        this.panes.add(this.articulations);
        this.panes.add(this.appearance);
        this.panes.add(this.transmitters);
        this.panes.add(this.warfare);
        this.panes.add(this.emissions);

        for(int i = 0, size = this.panes.size(); i < size; ++i) {

            this.panes.get(i).setEditable(false);
            this.panes.get(i).setContentType("text/html");
        }

        this.table.addKeyListener(this);
        this.table.setModel(this.model);
        this.table.setRowSorter(this.sorter);
        this.table.getSelectionModel().addListSelectionListener(this);

        TableColumnModel columnModel = this.table.getColumnModel();

        for(Column column : Column.values()) {

            TableColumn tableColumn = columnModel.getColumn(column.ordinal());

            tableColumn.setResizable(true);
            tableColumn.setPreferredWidth(column.columnWidth);

            this.sorter.setComparator(
                column.ordinal(),
                column.columnComparator);
        }

        this.fill();
        this.setTitle(title);

        this.frame.setMinimumSize(new Dimension(700, 500));
        this.frame.setPreferredSize(new Dimension(900, 700));
        this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.frame.pack();
    }

    public JFrame getFrame() {

        return this.frame;
    }

    public void destroy() {

        this.timer.stop();
        this.timer.removeActionListener(this);
        this.setThreshold.removeActionListener(this);
        this.toggleExpiration.removeActionListener(this);
        this.trackTransmitters.removeActionListener(this);
        this.trackEmissions.removeActionListener(this);
        this.table.removeKeyListener(this);
        this.table.getSelectionModel().removeListSelectionListener(this);
        this.frame.dispose();
    }

    public void setTitle(String title) {

        this.frame.setTitle("Entity Tracker [" + title + "]");
    }

    public void clearAll() {

        this.list.clear();
        this.model.fireTableDataChanged();
        this.show(null);
    }

    /**
     * Timer callback only, no user actions.
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == this.timer) {

            final long current = System.currentTimeMillis();

            this.date.setTime(current);
            this.time.setText(dateFormat.format(this.date));

            if (this.purgeEntities) {

                if ((current - this.lastPurge) >= PURGE_DURATION) {

                    this.purgeExpirations(current);
                }
            }
        }
        else if (event.getSource() == this.setThreshold) {

            this.setExpirationThreshold();
        }
        else if (event.getSource() == this.toggleExpiration) {

            this.toggleEntityExpiration();
        }
        else if (event.getSource() == this.trackTransmitters) {

            this.trackingTransmitters = this.trackTransmitters.isSelected();
        }
        else if (event.getSource() == this.trackEmissions) {

            this.trackingEmissions = this.trackEmissions.isSelected();
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {

    }

    @Override
    public void keyReleased(KeyEvent event) {

    }

    @Override
    public void keyTyped(KeyEvent event) {

        if (event.getKeyChar() == KeyEvent.VK_DELETE) {

            int rows[] = this.table.getSelectedRows();

            if ((rows != null) && (rows.length > 0)) {

                for(int i = 0; i < rows.length; ++i) {

                    rows[i] = this.sorter.convertRowIndexToModel(rows[i]);
                }
            }

            if ((rows != null) && (rows.length > 0)) {

                int choice = JOptionPane.showConfirmDialog(
                    this.frame,
                    "Delete " + rows.length + " selected entity(s)?",
                    TITLE,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (choice == JOptionPane.YES_OPTION) {

                    Arrays.sort(rows);

                    this.deleteEntities(rows);
                }
            }
        }
    }

    /**
     * Table selection callback.
     */
    @Override
    public void valueChanged(ListSelectionEvent event) {

        if (!event.getValueIsAdjusting()) {

            int row = this.table.getSelectedRow();

            if (row > -1) {

                int index = this.sorter.convertRowIndexToModel(row);

                this.show(this.getEntity(index));
            }
            else for(int i = 0, size = this.panes.size(); i < size; ++i) {

                this.panes.get(i).setText("");
            }
        }
    }

    /**
     * Processes captured PDUs.
     */
    public void processPDUs(List<PDU> pdus) {

        PDUProcessor processor = new PDUProcessor(pdus);

        if (SwingUtilities.isEventDispatchThread()) {

            processor.run();
        }
        else {

            SwingUtilities.invokeLater(processor);
        }
    }

    private void processEntityState(PDU pdu) {

        EntityId id = new EntityId();
        Entity entity = null;

        pdu.decode(false);
        pdu.getEntityId(id);

        entity = this.getEntity(id);

        if (entity != null) {

            if (entity.update(pdu)) {

                logger.info("Updated entity: " + pdu.getEntityId());

                this.model.fireTableDataChanged();
            }

            if (this.current == entity) {

                this.show(this.current);
            }
        }
        else {

            logger.info("New entity: " + pdu.getEntityId());

            entity = new Entity();
            entity.update(pdu);

            this.list.add(entity);
            this.model.fireTableDataChanged();
            this.count.setText(ENTITY_COUNT + this.list.size());
        }
    }

    private void processActionRequest(PDU pdu) {

        Entity entity1 = this.getEntity(pdu.getId());
        Entity entity2 = this.getEntity(pdu.getRecipient());

        if ((entity1 != null) && entity1.update(pdu)) {

            this.show(this.current);
        }

        if ((entity2 != null) && entity2.update(pdu)) {

            this.show(this.current);
        }
    }

    private void processActionResponse(PDU pdu) {

        Entity entity1 = this.getEntity(pdu.getId());
        Entity entity2 = this.getEntity(pdu.getRecipient());

        if ((entity1 != null) && entity1.update(pdu)) {

            this.show(this.current);
        }

        if ((entity2 != null) && entity2.update(pdu)) {

            this.show(this.current);
        }
    }

    private void processTransmitter(PDU pdu) {

        if (this.trackingTransmitters) {

            Entity entity = this.getEntity(pdu.getId());

            if ((entity != null) && entity.update(pdu)) {

                if (this.current == entity) {

                    this.show(this.current);
                }
            }
        }
    }

    private void processDetonation(PDU pdu) {

        Entity entity1 = this.getEntity(pdu.getId());
        Entity entity2 = this.getEntity(pdu.getRecipient());

        if ((entity1 != null) && entity1.update(pdu)) {

            this.show(this.current);
        }

        if ((entity2 != null) && entity2.update(pdu)) {

            this.show(this.current);
        }
    }


    private void processFire(PDU pdu) {

        Entity entity1 = this.getEntity(pdu.getId());
        Entity entity2 = this.getEntity(pdu.getRecipient());

        if ((entity1 != null) && entity1.update(pdu)) {

            this.show(this.current);
        }

        if ((entity2 != null) && entity2.update(pdu)) {

            this.show(this.current);
        }
    }

    private void processEmission(PDU pdu) {

        if (this.trackingEmissions) {

            Entity entity = this.getEntity(pdu.getId());

            if ((entity != null) && entity.update(pdu)) {

                if (this.current == entity) {

                    this.show(this.current);
                }
            }
        }
    }

    private String getSeconds() {

        double seconds = ((double)this.purgeThreshold / 1000.0);

        return numberFormatter.format(seconds);
    }

    private int getEntityCount() {

        return this.list.size();
    }

    private Entity getEntity(int index) {

        return this.list.get(index);
    }

    private Entity getEntity(EntityId id) {

        for(Entity entity : list) {

            if (entity.getEntityId().equals(id)) {

                return entity;
            }
        }

        return null;
    }

    private void deleteEntities(int indexes[]) {

        for(int i = (indexes.length - 1); i >= 0; --i) {

            Entity entity = this.list.remove(indexes[i]);

            if (this.current == entity) {

                this.show(null);
            }
        }

        this.model.fireTableDataChanged();
    }

    private void setExpirationThreshold() {

        String value = JOptionPane.showInputDialog(
            this.frame,
            "Enter expiration time in seconds,\n" +
            "current time is " + getSeconds() + ":",
            ENTITY_EXPIRATION,
            JOptionPane.QUESTION_MESSAGE);

        if (value != null) {

            try {

                double seconds = Double.parseDouble(value);

                if (seconds < 1) {

                    JOptionPane.showMessageDialog(
                        this.frame,
                        "Threshold must be at least one second!",
                        ENTITY_EXPIRATION,
                        JOptionPane.ERROR_MESSAGE);
                }
                else {

                    this.purgeThreshold = (long)(seconds * 1000.0);
                }
            }
            catch(NumberFormatException exception) {

                JOptionPane.showMessageDialog(
                    this.frame,
                    "Not a valid number: " + value,
                    ENTITY_EXPIRATION,
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void toggleEntityExpiration() {

        if (this.purgeEntities) {

            this.purgeEntities = false;
        }
        else {

            int choice = JOptionPane.showConfirmDialog(
                EntityTrackerFrame.this.frame,
                "Turn on expiration?  This will actively and permanently\n" +
                "remove entities for which Entity State PDUs are no longer\n" +
                "being received after current duration of " +
                this.getSeconds() + " seconds...",
                ENTITY_EXPIRATION,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            this.purgeEntities = (choice == JOptionPane.YES_OPTION);
        }

        this.setThreshold.setEnabled(this.purgeEntities);
        this.toggleExpiration.setSelected(this.purgeEntities);
    }

    private void show(Entity entity) {

        if (entity == null) {

            for(JEditorPane pane : this.panes) {

                pane.setText("");
            }
        }
        else {

            this.general.setText(entity.getGeneralHTML());
            this.associations.setText(entity.getAssociationsHTML());
            this.articulations.setText(entity.getArticulationsHTML());
            this.appearance.setText(entity.getAppearanceHTML());
            this.warfare.setText(entity.getWarfareHTML());

            if (this.trackingTransmitters) {

                this.transmitters.setText(entity.getTransmittersHTML());
            }
            else {

                this.transmitters.setText("Transitters Disabled.");
            }

            if (this.trackingEmissions) {

                this.emissions.setText(entity.getEmissionsHTML());
            }
            else {

                this.emissions.setText("Emissions Disabled.");
            }
       }

        this.current = entity;
    }

    private void purgeExpirations(long current) {

        Iterator<Entity> iterator = this.list.iterator();
        boolean purged = false;

        while(iterator.hasNext()) {

            Entity entity = iterator.next();

            long elapsed = (current - entity.getTime());

            if (elapsed >= this.purgeThreshold) {

                iterator.remove();
                purged = true;

                if (this.current == entity) {

                    this.current = null;
                    this.show(null);
                }

                logger.info(
                    "Purging entity {}: {}", 
                    entity.getId(), 
                    entity.getMarking());
            }
        }

        this.lastPurge = current;

        if (purged) {

            this.model.fireTableDataChanged();
        }
    }

    /**
     * Fills frame with GUI components.
     */
    private void fill() {

        JScrollPane scroller = new JScrollPane(this.table);
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JToolBar status = new JToolBar();
        JMenuBar menus = new JMenuBar();
        JMenu expiration = new JMenu("Expiration");
        JMenu options = new JMenu("Options");
        Dimension minimum = new Dimension();
        Dimension preferred = new Dimension();

        minimum.width = (MINIMUM_WIDTH / 2);
        minimum.height = (MINIMUM_HEIGHT / 2);
        preferred.width = (PREFERRED_WIDTH / 2);
        preferred.height = (PREFERRED_HEIGHT / 2);

        scroller.setMinimumSize(minimum);
        scroller.setPreferredSize(preferred);

        tabs.add("General", new JScrollPane(this.general));
        tabs.add("Associations", new JScrollPane(this.associations));
        tabs.add("Articulations", new JScrollPane(this.articulations));
        tabs.add("Appearance", new JScrollPane(this.appearance));
        tabs.add("Transmitters", new JScrollPane(this.transmitters));
        tabs.add("Warfare", new JScrollPane(this.warfare));
        tabs.add("Emissions", new JScrollPane(this.emissions));
        tabs.setMinimumSize(minimum);
        tabs.setPreferredSize(preferred);

        status.add(this.time);
        status.addSeparator();
        status.add(this.count);
        status.setFloatable(false);

        split.setContinuousLayout(true);
        split.setLeftComponent(scroller);
        split.setRightComponent(tabs);

        expiration.add(this.toggleExpiration);
        expiration.add(this.setThreshold);

        options.add(this.trackTransmitters);
        options.add(this.trackEmissions);

        menus.add(expiration);
        menus.add(options);

        this.frame.setJMenuBar(menus);
        this.frame.add(split, BorderLayout.CENTER);
        this.frame.add(status, BorderLayout.SOUTH);
    }

    class PDUProcessor implements Runnable {

        final List<PDU> list;

        public PDUProcessor(List<PDU> list) {

            this.list = new ArrayList<PDU>(list);
        }

        @Override
        public void run() {

            for(PDU pdu : this.list) {

                switch(pdu.getType()) {

                    case VDIS.PDU_TYPE_ENTITY_STATE:
                        processEntityState(pdu);
                        break;
                    case VDIS.PDU_TYPE_ACTION_REQUEST:
                        processActionRequest(pdu);
                        break;
                    case VDIS.PDU_TYPE_ACTION_RESPONSE:
                        processActionResponse(pdu);
                        break;
                    case VDIS.PDU_TYPE_TRANSMITTER:
                        processTransmitter(pdu);
                        break;
                    case VDIS.PDU_TYPE_FIRE:
                        processFire(pdu);
                        break;
                    case VDIS.PDU_TYPE_DETONATION:
                        processDetonation(pdu);
                        break;
                    case VDIS.PDU_TYPE_EM_EMISSION:
                        processEmission(pdu);
                        break;
                }
            }
        }
    }

    public static enum Column {

        MARKING("Marking", 120, String.class),
        IDENTIFIER("Identifier", 120, EntityId.class),
        TYPE("Type", 120, EntityType.class),
        FORCE("Force", 90, String.class),
        PORT("Port", 90, Integer.class),
        SOURCE("Source", 250, String.class);

        final String columnName;
        final Class<?> columnClass;
        final Comparator<?> columnComparator;
        final int columnWidth;

        private Column(
            String columnName,
            int columnWidth,
            Class<?> columnClass) {

            this.columnName = columnName;
            this.columnClass = columnClass;
            this.columnWidth = columnWidth;
            this.columnComparator = Utilities.getComparator(columnClass);
        }
    }

    @SuppressWarnings("serial")
    class TableModel extends AbstractTableModel {

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

            return false;
        }

        @Override
        public int getColumnCount() {

            return COLUMNS.length;
        }

        @Override
        public int getRowCount() {

            return getEntityCount();
        }

        @Override
        public Object getValueAt(int row, int column) {

            Entity entity = getEntity(row);

            switch(column) {
                case 0:
                    return entity.getMarking();
                case 1:
                    return entity.getEntityId();
                case 2:
                    return entity.getEntityType();
                case 3:
                    return entity.getForce();
                case 4:
                    return entity.getPort();
                case 5:
                    return entity.getSource();
                default:
                    return null;
            }
        }
    }
}
