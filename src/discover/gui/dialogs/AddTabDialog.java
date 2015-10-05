/**
 * @author Tony Pinkston
 */
package discover.gui.dialogs;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;
import discover.gui.tabs.TabType;

/**
 * Dialog allowing user to add additional tabs.
 */
public class AddTabDialog implements ActionListener {

    @SuppressWarnings("serial")
    private final JDialog dialog = new JDialog(
        DiscoverFrame.getFrame(),
        "Add Tab") {

            @Override
            public void dispose() {

                AddTabDialog.this.disposing();

                super.dispose();
            }
    };

    private final TreeMap<TabType, JRadioButton> buttons;
    private final JTextField name = new JTextField(15);
    private final JTextField port = new JTextField(15);
    private final JButton okay = new JButton("Okay");
    private final JButton cancel = new JButton("Cancel");

    private TabType selectedType = null;

    public AddTabDialog() {

        this.buttons = new TreeMap<TabType, JRadioButton>();

        DiscoverFrame frame = DiscoverFrame.getInstance();
        ButtonGroup group = new ButtonGroup();

        for(TabType type : TabType.values()) {

            JRadioButton button = new JRadioButton(type.getLabel());

            button.addActionListener(this);
            button.setToolTipText(type.getDescription());

            group.add(button);

            this.buttons.put(type, button);
        }

        this.buttons.get(TabType.CAPTURE).setSelected(true);


        this.name.addActionListener(this);
        this.port.addActionListener(this);
        this.okay.addActionListener(this);
        this.cancel.addActionListener(this);

        this.fill();

        this.dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.dialog.pack();
        this.dialog.setResizable(false);
        this.dialog.setModal(true);

        Utilities.center(DiscoverFrame.getFrame(), this.dialog);

        this.name.setText(frame.getNextTabName(TabType.CAPTURE));
        this.name.requestFocusInWindow();
        this.dialog.setVisible(true);
    }

    public TabType getTabType() {

        return this.selectedType;
    }

    public String getTabName() {

        return this.name.getText().trim();
    }

    public Integer getPort() {

        String text = this.port.getText();
        Integer integer = null;

        if ((text != null) && !text.isEmpty()) {

            try {

                integer = Integer.parseInt(text);
            }
            catch(NumberFormatException exception) {

                JOptionPane.showMessageDialog(
                    DiscoverFrame.getFrame(),
                    ("Not a valid port number: " + text),
                    "Add Tab",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        return integer;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == this.cancel) {

            this.dialog.dispose();
        }
        else if ((event.getSource() instanceof JTextField) ||
                 (event.getSource() == this.okay)) {

            this.selectedType = this.getSelectedTabType();

            this.dialog.dispose();
        }
        else if (event.getSource() instanceof JRadioButton) {

            DiscoverFrame frame = DiscoverFrame.getInstance();

            this.name.setText(frame.getNextTabName(this.getSelectedTabType()));

            if (this.buttons.get(TabType.CFS) == event.getSource()) {

                this.port.setText("");
                this.port.setEnabled(false);
            }
            else {

                this.port.setEnabled(true);
            }
        }
    }

    private TabType getSelectedTabType() {

        Iterator<TabType> iterator = this.buttons.keySet().iterator();

        while(iterator.hasNext() && (this.selectedType == null)) {

            TabType type = iterator.next();
            JRadioButton button = this.buttons.get(type);

            if (button.isSelected()) {

                return type;
            }
        }

        return null;
    }

    private void disposing() {

        this.name.removeActionListener(this);
        this.port.removeActionListener(this);
        this.okay.removeActionListener(this);
        this.cancel.removeActionListener(this);

        for(JRadioButton button : this.buttons.values()) {

            button.removeActionListener(this);
        }

        this.buttons.clear();
    }

    private void fill() {

        Utilities.setGridBagLayout(this.dialog.getContentPane());

        Utilities.addComponent(
            this.dialog.getContentPane(),
            this.getNorthPanel(),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 1.0,
            Utilities.getInsets(0, 0, 0, 0));

        Utilities.addComponent(
            this.dialog.getContentPane(),
            this.getSouthPanel(),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            1.0, 1.0,
            Utilities.getInsets(10, 5, 2, 5));
    }

    private JPanel getNorthPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        int row = 0;

        Utilities.addComponent(
            panel,
            new JLabel("Select tabe type:"),
            Utilities.HORIZONTAL,
            0, row++,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(5, 5, 12, 5));

        for(JRadioButton button : this.buttons.values()) {

            Utilities.addComponent(
                panel,
                button,
                Utilities.HORIZONTAL,
                0, row,
                1, 1,
                1.0, 0.0,
                Utilities.getInsets(2, 20, 2, 5));

            ++row;
        }

        Utilities.addComponent(
            panel,
            new JLabel("Enter name (optional):"),
            Utilities.HORIZONTAL,
            0, row++,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(15, 5, 2, 5));

        Utilities.addComponent(
            panel,
            this.name,
            Utilities.HORIZONTAL,
            0, row++,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(2, 5, 5, 5));

        Utilities.addComponent(
            panel,
            new JLabel("Enter port (optional):"),
            Utilities.HORIZONTAL,
            0, row++,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(15, 5, 2, 5));

        Utilities.addComponent(
            panel,
            this.port,
            Utilities.HORIZONTAL,
            0, row++,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(2, 5, 5, 5));

        return panel;
    }

    private JPanel getSouthPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 2));

        panel.add(this.okay);
        panel.add(this.cancel);

        return panel;
    }
}
