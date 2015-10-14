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
@SuppressWarnings("serial")
public class EntityTrackerFrame
        extends JFrame
        implements ActionListener, KeyListener, ListSelectionListener {

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

        sorter = new TableRowSorter<TableModel>(model);

        timer = new Timer(500, this);
        timer.start();

        setThreshold = new JMenuItem("Set Threshold");
        setThreshold.addActionListener(this);
        setThreshold.setEnabled(false);
        setThreshold.setToolTipText(
            "Set the time duration at which entities expire.");

        toggleExpiration = new JCheckBoxMenuItem("Expiration");
        toggleExpiration.setSelected(false);
        toggleExpiration.addActionListener(this);
        toggleExpiration.setToolTipText(
            "Select to automatically remove expired entities " +
            "(no longer publishing entity states).");

        trackTransmitters = new JCheckBoxMenuItem("Track Transmitters");
        trackTransmitters.setSelected(trackingTransmitters);
        trackTransmitters.addActionListener(this);

        trackEmissions = new JCheckBoxMenuItem("Track Emissions");
        trackEmissions.setSelected(trackingEmissions);
        trackEmissions.addActionListener(this);

        panes.add(general);
        panes.add(associations);
        panes.add(articulations);
        panes.add(appearance);
        panes.add(transmitters);
        panes.add(warfare);
        panes.add(emissions);

        for(int i = 0, size = panes.size(); i < size; ++i) {

            panes.get(i).setEditable(false);
            panes.get(i).setContentType("text/html");
        }

        table.addKeyListener(this);
        table.setModel(model);
        table.setRowSorter(sorter);
        table.getSelectionModel().addListSelectionListener(this);

        TableColumnModel columnModel = table.getColumnModel();

        for(Column column : Column.values()) {

            TableColumn tableColumn = columnModel.getColumn(column.ordinal());

            tableColumn.setResizable(true);
            tableColumn.setPreferredWidth(column.columnWidth);

            sorter.setComparator(
                column.ordinal(),
                column.columnComparator);
        }

        fill();
        setTitle(title);

        setMinimumSize(new Dimension(700, 500));
        setPreferredSize(new Dimension(900, 700));
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
    }

    public void destroy() {

        timer.stop();
        timer.removeActionListener(this);
        setThreshold.removeActionListener(this);
        toggleExpiration.removeActionListener(this);
        trackTransmitters.removeActionListener(this);
        trackEmissions.removeActionListener(this);
        table.removeKeyListener(this);
        table.getSelectionModel().removeListSelectionListener(this);

        dispose();
    }

    @Override
    public void setTitle(String title) {

        super.setTitle("Entity Tracker [" + title + "]");
    }

    public void clearAll() {

        list.clear();
        model.fireTableDataChanged();
        this.show(null);
    }

    /**
     * Timer callback only, no user actions.
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == timer) {

            final long current = System.currentTimeMillis();

            date.setTime(current);
            time.setText(dateFormat.format(date));

            if (purgeEntities) {

                if ((current - lastPurge) >= PURGE_DURATION) {

                    purgeExpirations(current);
                }
            }
        }
        else if (event.getSource() == setThreshold) {

            setExpirationThreshold();
        }
        else if (event.getSource() == toggleExpiration) {

            toggleEntityExpiration();
        }
        else if (event.getSource() == trackTransmitters) {

            trackingTransmitters = trackTransmitters.isSelected();
        }
        else if (event.getSource() == trackEmissions) {

            trackingEmissions = trackEmissions.isSelected();
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

            int rows[] = table.getSelectedRows();

            if ((rows != null) && (rows.length > 0)) {

                for(int i = 0; i < rows.length; ++i) {

                    rows[i] = sorter.convertRowIndexToModel(rows[i]);
                }
            }

            if ((rows != null) && (rows.length > 0)) {

                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Delete " + rows.length + " selected entity(s)?",
                    TITLE,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (choice == JOptionPane.YES_OPTION) {

                    Arrays.sort(rows);

                    deleteEntities(rows);
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

            int row = table.getSelectedRow();

            if (row > -1) {

                int index = sorter.convertRowIndexToModel(row);

                this.show(this.getEntity(index));
            }
            else for(int i = 0, size = panes.size(); i < size; ++i) {

                panes.get(i).setText("");
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

                model.fireTableDataChanged();
            }

            if (current == entity) {

                this.show(current);
            }
        }
        else {

            logger.info("New entity: " + pdu.getEntityId());

            entity = new Entity();
            entity.update(pdu);

            list.add(entity);
            model.fireTableDataChanged();
            count.setText(ENTITY_COUNT + list.size());
        }
    }

    private void processActionRequest(PDU pdu) {

        Entity entity1 = this.getEntity(pdu.getId());
        Entity entity2 = this.getEntity(pdu.getRecipient());

        if ((entity1 != null) && entity1.update(pdu)) {

            this.show(current);
        }

        if ((entity2 != null) && entity2.update(pdu)) {

            this.show(current);
        }
    }

    private void processActionResponse(PDU pdu) {

        Entity entity1 = this.getEntity(pdu.getId());
        Entity entity2 = this.getEntity(pdu.getRecipient());

        if ((entity1 != null) && entity1.update(pdu)) {

            this.show(current);
        }

        if ((entity2 != null) && entity2.update(pdu)) {

            this.show(current);
        }
    }

    private void processTransmitter(PDU pdu) {

        if (trackingTransmitters) {

            Entity entity = this.getEntity(pdu.getId());

            if ((entity != null) && entity.update(pdu)) {

                if (current == entity) {

                    this.show(current);
                }
            }
        }
    }

    private void processDetonation(PDU pdu) {

        Entity entity1 = this.getEntity(pdu.getId());
        Entity entity2 = this.getEntity(pdu.getRecipient());

        if ((entity1 != null) && entity1.update(pdu)) {

            this.show(current);
        }

        if ((entity2 != null) && entity2.update(pdu)) {

            this.show(current);
        }
    }


    private void processFire(PDU pdu) {

        Entity entity1 = this.getEntity(pdu.getId());
        Entity entity2 = this.getEntity(pdu.getRecipient());

        if ((entity1 != null) && entity1.update(pdu)) {

            this.show(current);
        }

        if ((entity2 != null) && entity2.update(pdu)) {

            this.show(current);
        }
    }

    private void processEmission(PDU pdu) {

        if (trackingEmissions) {

            Entity entity = this.getEntity(pdu.getId());

            if ((entity != null) && entity.update(pdu)) {

                if (current == entity) {

                    this.show(current);
                }
            }
        }
    }

    private String getSeconds() {

        double seconds = (purgeThreshold / 1000.0);

        return numberFormatter.format(seconds);
    }

    private int getEntityCount() {

        return list.size();
    }

    private Entity getEntity(int index) {

        return list.get(index);
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

            Entity entity = list.remove(indexes[i]);

            if (current == entity) {

                this.show(null);
            }
        }

        model.fireTableDataChanged();
    }

    private void setExpirationThreshold() {

        String value = JOptionPane.showInputDialog(
            this,
            "Enter expiration time in seconds,\n" +
            "current time is " + getSeconds() + ":",
            ENTITY_EXPIRATION,
            JOptionPane.QUESTION_MESSAGE);

        if (value != null) {

            try {

                double seconds = Double.parseDouble(value);

                if (seconds < 1) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Threshold must be at least one second!",
                        ENTITY_EXPIRATION,
                        JOptionPane.ERROR_MESSAGE);
                }
                else {

                    purgeThreshold = (long)(seconds * 1000.0);
                }
            }
            catch(NumberFormatException exception) {

                JOptionPane.showMessageDialog(
                    this,
                    "Not a valid number: " + value,
                    ENTITY_EXPIRATION,
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void toggleEntityExpiration() {

        if (purgeEntities) {

            purgeEntities = false;
        }
        else {

            int choice = JOptionPane.showConfirmDialog(
                this,
                "Turn on expiration?  This will actively and permanently\n" +
                "remove entities for which Entity State PDUs are no longer\n" +
                "being received after current duration of " +
                getSeconds() + " seconds...",
                ENTITY_EXPIRATION,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            purgeEntities = (choice == JOptionPane.YES_OPTION);
        }

        setThreshold.setEnabled(purgeEntities);
        toggleExpiration.setSelected(purgeEntities);
    }

    private void show(Entity entity) {

        if (entity == null) {

            for(JEditorPane pane : panes) {

                pane.setText("");
            }
        }
        else {

            general.setText(entity.getGeneralHTML());
            associations.setText(entity.getAssociationsHTML());
            articulations.setText(entity.getArticulationsHTML());
            appearance.setText(entity.getAppearanceHTML());
            warfare.setText(entity.getWarfareHTML());

            if (trackingTransmitters) {

                transmitters.setText(entity.getTransmittersHTML());
            }
            else {

                transmitters.setText("Transitters Disabled.");
            }

            if (trackingEmissions) {

                emissions.setText(entity.getEmissionsHTML());
            }
            else {

                emissions.setText("Emissions Disabled.");
            }
       }

        current = entity;
    }

    private void purgeExpirations(long current) {

        Iterator<Entity> iterator = list.iterator();
        boolean purged = false;

        while(iterator.hasNext()) {

            Entity entity = iterator.next();

            long elapsed = (current - entity.getTime());

            if (elapsed >= purgeThreshold) {

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

        lastPurge = current;

        if (purged) {

            model.fireTableDataChanged();
        }
    }

    /**
     * Fills frame with GUI components.
     */
    private void fill() {

        JScrollPane scroller = new JScrollPane(table);
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

        tabs.add("General", new JScrollPane(general));
        tabs.add("Associations", new JScrollPane(associations));
        tabs.add("Articulations", new JScrollPane(articulations));
        tabs.add("Appearance", new JScrollPane(appearance));
        tabs.add("Transmitters", new JScrollPane(transmitters));
        tabs.add("Warfare", new JScrollPane(warfare));
        tabs.add("Emissions", new JScrollPane(emissions));
        tabs.setMinimumSize(minimum);
        tabs.setPreferredSize(preferred);

        status.add(time);
        status.addSeparator();
        status.add(count);
        status.setFloatable(false);

        split.setContinuousLayout(true);
        split.setLeftComponent(scroller);
        split.setRightComponent(tabs);

        expiration.add(toggleExpiration);
        expiration.add(setThreshold);

        options.add(trackTransmitters);
        options.add(trackEmissions);

        menus.add(expiration);
        menus.add(options);

        setJMenuBar(menus);
        add(split, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
    }

    class PDUProcessor implements Runnable {

        final List<PDU> list;

        public PDUProcessor(List<PDU> list) {

            this.list = new ArrayList<PDU>(list);
        }

        @Override
        public void run() {

            for(PDU pdu : list) {

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
            columnComparator = Utilities.getComparator(columnClass);
        }
    }

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
