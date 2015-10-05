/**
 * @author Tony Pinkston
 */
package discover.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;

public class ReorderTabsDialog implements ActionListener, ListSelectionListener {

    @SuppressWarnings("serial")
    private final JDialog dialog = new JDialog(
        DiscoverFrame.getFrame(),
        "Reorder Tabs") {

            @Override
            public void dispose() {

                ReorderTabsDialog.this.disposing();

                super.dispose();
            }
    };

    private final JList<String> list;

    private final JButton up = new JButton("Move Up");
    private final JButton down = new JButton("Move Down");
    private final JButton okay = new JButton("Okay");
    private final JButton cancel = new JButton("Cancel");

    private String names[];
    private boolean reorder = false;

    public ReorderTabsDialog(String names[]) {

        this.names = names;

        this.list = new JList<>(this.names);
        this.list.setPrototypeCellValue("ABCDEFGHIJKLMNOPQR");
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.addListSelectionListener(this);

        this.up.addActionListener(this);
        this.up.setEnabled(false);

        this.down.addActionListener(this);
        this.down.setEnabled(false);

        this.okay.addActionListener(this);
        this.okay.setEnabled(false);

        this.cancel.addActionListener(this);

        this.fill();

        this.dialog.pack();
        this.dialog.setModal(true);
        this.dialog.setResizable(true);
        this.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Utilities.center(DiscoverFrame.getFrame(), this.dialog);

        this.dialog.setVisible(true);
    }

    public boolean reorderTabs() {

        return this.reorder;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        final int selection = this.list.getSelectedIndex();

        if (event.getSource() == this.up) {

            this.move(selection, (selection - 1));
            this.okay.setEnabled(true);
        }
        else if (event.getSource() == this.down) {

            this.move(selection, (selection + 1));
            this.okay.setEnabled(true);
        }
        else if (event.getSource() == this.okay) {

            this.reorder = true;
            this.dialog.dispose();
        }
        else if (event.getSource() == this.cancel) {

            this.dialog.dispose();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {

        if (!event.getValueIsAdjusting()) {

            final int selection = this.list.getSelectedIndex();

            if (selection == 0) {

                this.up.setEnabled(false);
                this.down.setEnabled(true);
            }
            else if (selection == (this.names.length - 1)) {

                this.up.setEnabled(true);
                this.down.setEnabled(false);
            }
            else {

                this.up.setEnabled(true);
                this.down.setEnabled(true);
            }
        }
    }

    private void disposing() {

        this.up.removeActionListener(this);
        this.down.removeActionListener(this);
        this.okay.removeActionListener(this);
        this.cancel.removeActionListener(this);
        this.list.removeListSelectionListener(this);
    }

    private void move(int source, int target) {

        String temp = this.names[source];

        this.names[source] = this.names[target];
        this.names[target] = temp;

        this.list.setListData(this.names);
        this.list.setSelectedIndex(target);
    }

    private void fill() {

        this.dialog.add(this.getCenterPanel(), BorderLayout.CENTER);
        this.dialog.add(this.getSouthPanel(), BorderLayout.SOUTH);
    }

    private JPanel getCenterPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        JScrollPane scroller = new JScrollPane(
            this.list,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        Utilities.addComponent(
            panel,
            scroller,
            Utilities.VERTICAL,
            0, 0,
            1, 4,
            0.5, 1.0,
            Utilities.getInsets(15, 15, 15, 2));

        Utilities.addComponent(
            panel,
            this.up,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(15, 10, 2, 15));

        Utilities.addComponent(
            panel,
            this.down,
            Utilities.HORIZONTAL,
            1, 2,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(2, 10, 2, 15));

        return panel;
    }

    private JPanel getSouthPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 2));

        panel.add(this.okay);
        panel.add(this.cancel);

        return panel;
    }
}
