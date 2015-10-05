/**
 * @author Tony Pinkston
 */
package discover.gui.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import discover.common.buffer.HypertextBuffer;
import discover.gui.Utilities;
import discover.gui.panels.TextPanel;
import discover.vdis.enums.VDIS;
import discover.vdis.types.ObjectType;
import discover.vdis.types.ObjectTypes;

public class ObjectTypesFrame implements ActionListener, ListSelectionListener {

    private static ObjectTypesFrame instance = null;

    private static final Column COLUMNS[] = Column.values();

    private final JFrame frame = new JFrame("Object Types");
    private final JComboBox<String> domains = new JComboBox<>();
    private final JComboBox<String> kinds = new JComboBox<>();
    private final JComboBox<String> geometries = new JComboBox<>();
    private final JTable table = new JTable();
    private final JLabel visible = new JLabel();
    private final TextPanel text = new TextPanel("text/html", null);
    private final TableModel model = new TableModel();
    private final TableFilter filter = new TableFilter();
    private final TableRowSorter<TableModel> sorter;
    private final List<ObjectType> types;

    public static JFrame getFrame() {

        if (instance == null) {

            return null;
        }
        else {

            return instance.frame;
        }
    }

    public static void setVisible() {

        if (instance == null) {

            instance = new ObjectTypesFrame();
        }

        if (!instance.frame.isVisible()) {

            instance.frame.setVisible(true);
        }
    }

    private ObjectTypesFrame() {

        this.types = ObjectTypes.getValues();

        Utilities.configureComboBox(this.geometries, VDIS.OBJECT_GEOMETRY, true);
        Utilities.configureComboBox(this.kinds, VDIS.OBJECT_KIND, true);
        Utilities.configureComboBox(this.domains, VDIS.DOMAIN, true);

        this.domains.addActionListener(this);
        this.kinds.addActionListener(this);
        this.geometries.addActionListener(this);

        this.sorter = new TableRowSorter<TableModel>(this.model);
        this.sorter.setRowFilter(this.filter);

        this.table.setModel(this.model);
        this.table.setRowSorter(this.sorter);
        this.table.getSelectionModel().addListSelectionListener(this);
        this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumnModel columnModel = this.table.getColumnModel();

        for (Column column : COLUMNS) {

            TableColumn tableColumn = columnModel.getColumn(column.ordinal());

            tableColumn.setResizable(true);
            tableColumn.setPreferredWidth(column.width);

            this.sorter.setComparator(column.ordinal(), column.comparator);
        }

        this.fill();
        this.setVisibleObjectCount();

        this.frame.setPreferredSize(new Dimension(800, 600));
        this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.frame.pack();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        this.filter.domain = null;
        this.filter.kind = null;
        this.filter.geometry = null;

        int domain = this.domains.getSelectedIndex();
        int kind = this.kinds.getSelectedIndex();
        int geometry = this.geometries.getSelectedIndex();

        if (domain > 0) {

            this.filter.domain = new Integer(domain - 1);
        }

        if (kind > 0) {

            this.filter.kind = new Integer(kind - 1);
        }

        if (geometry > 0) {

            this.filter.geometry = new Integer(geometry - 1);
        }

        this.model.fireTableDataChanged();
        this.setVisibleObjectCount();
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {

        if (!event.getValueIsAdjusting()) {

            int row = this.table.getSelectedRow();

            if (row > -1) {

                int index = this.sorter.convertRowIndexToModel(row);

                ObjectType type = this.types.get(index);

                HypertextBuffer buffer = new HypertextBuffer();

                buffer.addText("<html><body>");

                buffer.addAttribute("Name", type.name);
                buffer.addAttribute("Value", type.tuple);
                buffer.addAttribute("Geometry", type.getGeometry());
                buffer.addAttribute("Description", type.description);
                buffer.addAttribute("Kind", type.getKind());
                buffer.addAttribute("Domain", type.getDomain());

                buffer.addText("</body></html>");

                this.text.setText(buffer.toString());
            }
        }
    }

    private void setVisibleObjectCount() {

        this.visible.setText(
            "Visible rows: " + this.sorter.getViewRowCount() +
            " of " + this.types.size());
    }

    private void fill() {

        JToolBar filter = new JToolBar();
        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        JScrollPane scroller = new JScrollPane(this.table);

        filter.add(new JLabel(" Domain: "));
        filter.add(this.domains);
        filter.add(new JLabel("   Kind: "));
        filter.add(this.kinds);
        filter.add(new JLabel("   Geometry: "));
        filter.add(this.geometries);
        filter.setFloatable(false);

        splitter.setTopComponent(scroller);
        splitter.setBottomComponent(this.text.getPanel());

        this.frame.add(filter, BorderLayout.NORTH);
        this.frame.add(splitter, BorderLayout.CENTER);
        this.frame.add(this.visible, BorderLayout.SOUTH);
    }

    public static enum Column {

        NAME("NAME", 300, Utilities.getComparator(String.class)),
        VALUE("Value", 50, Utilities.getComparator(ObjectType.class)),
        DOMAIN("Domain", 50, Utilities.getComparator(String.class)),
        KIND("Kind", 100, Utilities.getComparator(String.class)),
        GEOMETRY("Geometry", 50, Utilities.getComparator(String.class));

        final String name;
        final Comparator<?> comparator;
        final int width;

        private Column(String name, int width, Comparator<?> comparator) {

            this.name = name;
            this.comparator = comparator;
            this.width = width;
        }
    }

    @SuppressWarnings("serial")
    class TableModel extends AbstractTableModel {

        @Override
        public Class<?> getColumnClass(int column) {

            if (column == Column.VALUE.ordinal()) {

                return ObjectType.class;
            }
            else {

                return String.class;
            }
        }

        @Override
        public String getColumnName(int column) {

            return COLUMNS[column].name;
        }

        @Override
        public boolean isCellEditable(int row, int column) { return false; }

        @Override
        public int getColumnCount() { return COLUMNS.length; }

        @Override
        public int getRowCount() { return types.size(); }

        @Override
        public Object getValueAt(int row, int column) {

            Column c = COLUMNS[column];
            ObjectType type = types.get(row);

            Object object = null;

            switch(c) {

                case NAME:
                    object = type.name;
                    break;
                case VALUE:
                    object = type;
                    break;
                case DOMAIN:
                    object = type.getDomain();
                    break;
                case KIND:
                    object = type.getKind();
                    break;
                case GEOMETRY:
                    object = type.getGeometry();
                    break;
            }

            return object;
        }
    }

    class TableFilter extends RowFilter<TableModel, Integer> {

        Integer kind = null;
        Integer domain = null;
        Integer geometry = null;

        @Override
        public boolean include( RowFilter.Entry<
            ? extends TableModel,
            ? extends Integer> entry) {

            ObjectType type = types.get(entry.getIdentifier().intValue());

            if (type == null) {

                return false;
            }
            else if ((this.domain != null) &&
                     (this.domain != type.domain)) {

                return false;
            }
            else if ((this.kind != null) &&
                     (this.kind != type.kind)) {

                return false;
            }
            else if ((this.geometry != null) &&
                     (this.geometry != type.geometry.ordinal())) {

                return false;
            }
            else {

                return true;
            }
        }
    }
}
