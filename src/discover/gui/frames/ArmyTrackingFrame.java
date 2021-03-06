package discover.gui.frames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import discover.vdis.marking.army.AbstractEchelon;
import discover.vdis.marking.army.ArmyBattalion;
import discover.vdis.marking.army.ArmyBrigade;
import discover.vdis.marking.army.ArmyCompany;
import discover.vdis.marking.army.ArmyDivision;
import discover.vdis.marking.army.ArmyPlatoon;
import discover.vdis.marking.army.ArmySection;
import discover.vdis.marking.army.ArmySquad;
import discover.vdis.marking.army.ArmyTeam;
import discover.vdis.marking.army.ArmyTracking;

/**
 * @author Tony Pinkston
 */
@SuppressWarnings("serial")
public class ArmyTrackingFrame extends JFrame {

    private static ArmyTrackingFrame instance = null;

    public static JFrame getFrame() {

        return instance;
    }

    public static void setVisible() {

        if (instance == null) {

            instance = new ArmyTrackingFrame();
        }

        if (!instance.isVisible()) {

            instance.setVisible(true);
        }
    }

    private ArmyTrackingFrame() {

        super("Army Tracking");

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("High Level Units", new JScrollPane(getTree()));
        tabs.add("Companies", new JScrollPane(getCompanyTable()));
        tabs.add("Platoons", new JScrollPane(getPlatoonTable()));
        tabs.add("Sections", new JScrollPane(getSectionTable()));
        tabs.add("Squads", new JScrollPane(getSquadTable()));
        tabs.add("Teams", new JScrollPane(getTeamTable()));

        getContentPane().add(tabs, BorderLayout.CENTER);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private JTable getCompanyTable() {

        Column columns[] = new Column[] {

            Column.VALUE,
            Column.NAME,
            Column.DESCRIPTION,
            Column.CV,
            Column.BUMPER
        };

        JTable table = new JTable(new TableModel(
            columns,
            ArmyTracking.getValues(ArmyCompany.class)));

        setTableColumnWidths(table, columns);

        return table;
    }

    private JTable getPlatoonTable() {

        Column columns[] = new Column[] {

            Column.VALUE,
            Column.NAME,
            Column.DESCRIPTION,
            Column.PV,
            Column.BUMPER
        };

        JTable table = new JTable(new TableModel(
            columns,
            ArmyTracking.getValues(ArmyPlatoon.class)));

        setTableColumnWidths(table, columns);

        return table;
    }

    private JTable getSectionTable() {

        Column columns[] = new Column[] {

            Column.VALUE,
            Column.NAME,
            Column.DESCRIPTION,
            Column.BUMPER
        };

        JTable table = new JTable(new TableModel(
            columns,
            ArmyTracking.getValues(ArmySection.class)));

        setTableColumnWidths(table, columns);

        return table;
    }

    private JTable getSquadTable() {

        Column columns[] = new Column[] {

            Column.VALUE,
            Column.NAME,
            Column.DESCRIPTION,
            Column.BUMPER
        };

        JTable table = new JTable(new TableModel(
            columns,
            ArmyTracking.getValues(ArmySquad.class)));

        setTableColumnWidths(table, columns);

        return table;
    }

    private JTable getTeamTable() {

        Column columns[] = new Column[] {

            Column.VALUE,
            Column.NAME,
            Column.DESCRIPTION,
            Column.BUMPER
        };

        JTable table = new JTable(new TableModel(
            columns,
            ArmyTracking.getValues(ArmyTeam.class)));

        setTableColumnWidths(table, columns);

        return table;
    }

    private JTree getTree() {

        JTree tree = new JTree();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
        Map<ArmyBrigade, DefaultMutableTreeNode> brigades = null;

        tree.setRootVisible(false);
        tree.setCellRenderer(new TreeCellRenderer());

        while(model.getChildCount(root) > 0) {

            model.removeNodeFromParent((MutableTreeNode)root.getChildAt(0));
        }

        AbstractEchelon divisions[] = ArmyDivision.getValues();

        if (divisions == null) {

            model.insertNodeInto(
                new DefaultMutableTreeNode("Data Not Available"),
                root,
                0);

            model.nodeStructureChanged(root);
        }
        else for(int i = 0; i < divisions.length; ++i) {

            ArmyDivision division = (ArmyDivision)divisions[i];
            ArmyBrigade brigade = null;
            ArmyBattalion battalion = null;
            AbstractEchelon battalions[] = division.getBattalions();

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(division);
            DefaultMutableTreeNode child = null;

            model.insertNodeInto(node, root, i);
            model.nodeStructureChanged(root);

            if (battalions != null) {

                brigades = new HashMap<ArmyBrigade, DefaultMutableTreeNode>();

                for(int j = 0; j < battalions.length; ++j) {

                    battalion = (ArmyBattalion)battalions[j];
                    brigade = battalion.getBrigade();

                    if (brigade != null) {

                        child = brigades.get(brigade);

                        if (child == null) {

                            child = new DefaultMutableTreeNode(brigade);

                            brigades.put(brigade, child);

                            model.insertNodeInto(
                                child,
                                node,
                                node.getChildCount());
                        }

                        model.insertNodeInto(
                            new DefaultMutableTreeNode(battalion),
                            child,
                            child.getChildCount());

                        model.nodeStructureChanged(root);
                    }
                }
            }
        }

        return tree;
    }

    private void setTableColumnWidths(JTable table, Column columns[]) {

        for(int i = 0; i < columns.length; ++i) {

            table.getColumnModel().getColumn(i).setPreferredWidth(
                columns[i].width);
        }
    }

    static enum Column {

        VALUE(Integer.class, 50),
        NAME(String.class, 250),
        DESCRIPTION(String.class, 250),
        BV(Integer.class, 50),
        CV(Integer.class, 50),
        PV(Integer.class, 50),
        PRV(Integer.class, 50),
        BUMPER(String.class, 100);

        final Class<?> type;
        final int width;

        private Column(Class<?> type, int width) {

            this.type = type;
            this.width = width;
        }
    }

    static class TableModel extends AbstractTableModel {

        private final Column columns[];
        private final AbstractEchelon data[];

        public TableModel(
            Column columns[],
            AbstractEchelon data[]) {

            this.columns = columns;

            if (data != null) {

                this.data = data;
            }
            else {

                this.data = new AbstractEchelon[0];
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {

            return false;
        }

        @Override
        public int getRowCount() {

            return data.length;
        }

        @Override
        public int getColumnCount() {

            return columns.length;
        }

        @Override
        public Class<?> getColumnClass(int column) {

            return columns[column].type;
        }

        @Override
        public String getColumnName(int column) {

            return columns[column].name();
        }

        @Override
        public Object getValueAt(int row, int column) {

            switch(columns[column]) {

                case VALUE:
                    return data[row].value;
                case NAME:
                    return data[row].name;
                case DESCRIPTION:
                    return data[row].description;
                case BV:
                    return data[row].getBV();
                case CV:
                    return data[row].getCV();
                case PV:
                    return data[row].getPV();
                case PRV:
                    return data[row].getPRV();
                case BUMPER:
                    return data[row].getBumper();
            }

            return null;
        }
    }

    static class TreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(
            JTree tree,
            Object object,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean focus) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode)object;

            JLabel label = (JLabel)super.getTreeCellRendererComponent(
                tree,
                object,
                selected,
                expanded,
                leaf,
                row,
                focus);

            if (node.getUserObject() instanceof AbstractEchelon) {

                AbstractEchelon echelon = (AbstractEchelon)node.getUserObject();
                StringBuffer buffer = new StringBuffer();

                buffer.append(echelon.description);
                buffer.append(" [value: " + echelon.value + "]");

                if (echelon instanceof ArmyBattalion) {

                    ArmyBattalion battalion = (ArmyBattalion)echelon;

                    buffer.append(" [prv: " + battalion.getPRV() + "]");
                    buffer.append(" [bv: " + battalion.getBV() + "]");
                }

                if (echelon.getBumper() != null) {

                    buffer.append(" [bumper: " + echelon.getBumper() + "]");
                }

                label.setText(buffer.toString());
            }

            return label;
        }
    }
}
