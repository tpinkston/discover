package discover.gui.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;
import discover.vdis.enums.PDU_TYPE;

/**
 * @author Tony Pinkston
 */
public class PDUTypeDialog implements ActionListener {

    private static final String CLOSE = "Close";
    private static final String SELECT_ALL = "Select All";
    private static final String DESELECT_ALL = "Deselect All";

    @SuppressWarnings("serial")
    private final JDialog dialog = new JDialog(DiscoverFrame.getFrame(), "") {

            @Override
            public void dispose() {

                PDUTypeDialog.this.disposing();

                super.dispose();
            }
    };

    private final TableModel model = new TableModel();

    private final JButton select = new JButton(SELECT_ALL);
    private final JButton deselect = new JButton(DESELECT_ALL);
    private final JButton close = new JButton(CLOSE);

    public PDUTypeDialog(
        String title,
        JDialog parent,
        List<Integer> list) {

        dialog.setTitle(title);

        JTable table = new JTable(model);
        JScrollPane scroller = new JScrollPane(table);

        select.setActionCommand(SELECT_ALL);
        select.addActionListener(model);

        deselect.setActionCommand(DESELECT_ALL);
        deselect.addActionListener(model);

        close.setActionCommand(CLOSE);
        close.addActionListener(this);

        for(PDU_TYPE type : PDU_TYPE.values()) {

            TableRow row = new TableRow(type);

            row.selected = Boolean.valueOf(list.contains(type));

            model.rows.add(row);
        }

        TableColumn column0 = table.getColumnModel().getColumn(0);
        TableColumn column1 = table.getColumnModel().getColumn(1);

        column0.setResizable(true);
        column1.setMinWidth(18);
        column1.setMaxWidth(18);
        column1.setResizable(false);

        scroller.setPreferredSize(new Dimension(250, 300));

        Utilities.setGridBagLayout(dialog.getContentPane());

        Utilities.addComponent(
            dialog.getContentPane(),
            scroller,
            Utilities.BOTH,
            0, 0,
            2, 1,
            1.0, 1.0,
            Utilities.getInsets(10, 10, 10, 10));
        Utilities.addComponent(
            dialog.getContentPane(),
            select,
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(10, 10, 10, 5));
        Utilities.addComponent(
            dialog.getContentPane(),
            deselect,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(10, 5, 10, 10));
        Utilities.addComponent(
            dialog.getContentPane(),
            close,
            Utilities.HORIZONTAL,
            0, 2,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 10, 10, 10));

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setModal(true);
        dialog.setResizable(true);
        dialog.setVisible(true);
    }

    /**
     * TODO: Change list to List<PDU_TYPE>
     *
     * @param list
     */
    public void apply(List<Integer> list) {

        list.clear();

        for(TableRow row : model.rows) {

            if (row.selected) {

                list.add(row.type.value);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getActionCommand() == CLOSE) {

            dialog.dispose();
        }
    }

    private void disposing() {

        select.removeActionListener(model);
        deselect.removeActionListener(model);
        close.removeActionListener(this);
    }

    class TableRow {

        public final PDU_TYPE type;
        public Boolean selected;

        public TableRow(PDU_TYPE type) {

            this.type = type;
        }
    }

    @SuppressWarnings("serial")
    class TableModel extends AbstractTableModel implements ActionListener {

        private final List<TableRow> rows = new ArrayList<TableRow>();

        @Override
        public int getColumnCount() { return 2; }

        @Override
        public int getRowCount() { return rows.size(); }

        @Override
        public String getColumnName(int column) {

            return null;
        }

        @Override
        public Class<?> getColumnClass(int column) {

            return ((column == 0) ? String.class : Boolean.class);
        }

        @Override
        public boolean isCellEditable(int row, int column) {

            return (column == 1);
        }

        public void setAllRows(boolean selected) {

            for(TableRow row : rows) {

                row.selected = Boolean.valueOf(selected);
            }

            super.fireTableDataChanged();
        }

        @Override
        public void setValueAt(Object object, int row, int column) {

            if (object instanceof Boolean) {

                rows.get(row).selected = (Boolean)object;
            }
        }

        @Override
        public Object getValueAt(int row, int column) {

            TableRow tableRow = rows.get(row);

            if (column == 0) {

               return tableRow.type.description;
            }
            else {

                return tableRow.selected;
            }
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (event.getActionCommand() == SELECT_ALL) {

                setAllRows(true);
            }
            else if (event.getActionCommand() == DESELECT_ALL) {

                setAllRows(false);
            }
        }
    }
}
