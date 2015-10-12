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

/**
 * @author Tony Pinkston
 */
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

        list = new JList<>(names);
        list.setPrototypeCellValue("ABCDEFGHIJKLMNOPQR");
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);

        up.addActionListener(this);
        up.setEnabled(false);

        down.addActionListener(this);
        down.setEnabled(false);

        okay.addActionListener(this);
        okay.setEnabled(false);

        cancel.addActionListener(this);

        fill();

        dialog.pack();
        dialog.setModal(true);
        dialog.setResizable(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Utilities.center(DiscoverFrame.getFrame(), dialog);

        dialog.setVisible(true);
    }

    public boolean reorderTabs() {

        return reorder;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        final int selection = list.getSelectedIndex();

        if (event.getSource() == up) {

            move(selection, (selection - 1));
            okay.setEnabled(true);
        }
        else if (event.getSource() == down) {

            move(selection, (selection + 1));
            okay.setEnabled(true);
        }
        else if (event.getSource() == okay) {

            reorder = true;
            dialog.dispose();
        }
        else if (event.getSource() == cancel) {

            dialog.dispose();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {

        if (!event.getValueIsAdjusting()) {

            final int selection = list.getSelectedIndex();

            if (selection == 0) {

                up.setEnabled(false);
                down.setEnabled(true);
            }
            else if (selection == (names.length - 1)) {

                up.setEnabled(true);
                down.setEnabled(false);
            }
            else {

                up.setEnabled(true);
                down.setEnabled(true);
            }
        }
    }

    private void disposing() {

        up.removeActionListener(this);
        down.removeActionListener(this);
        okay.removeActionListener(this);
        cancel.removeActionListener(this);
        list.removeListSelectionListener(this);
    }

    private void move(int source, int target) {

        String temp = names[source];

        names[source] = names[target];
        names[target] = temp;

        list.setListData(names);
        list.setSelectedIndex(target);
    }

    private void fill() {

        dialog.add(getCenterPanel(), BorderLayout.CENTER);
        dialog.add(getSouthPanel(), BorderLayout.SOUTH);
    }

    private JPanel getCenterPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        JScrollPane scroller = new JScrollPane(
            list,
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
            up,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(15, 10, 2, 15));

        Utilities.addComponent(
            panel,
            down,
            Utilities.HORIZONTAL,
            1, 2,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(2, 10, 2, 15));

        return panel;
    }

    private JPanel getSouthPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 2));

        panel.add(okay);
        panel.add(cancel);

        return panel;
    }
}
