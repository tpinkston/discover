package discover.gui.dialogs;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;

/**
 * @author Tony Pinkston
 */
public class RemovePortDialog implements ActionListener {

    @SuppressWarnings("serial")
    private final JDialog dialog = new JDialog(
        DiscoverFrame.getFrame(),
        "Remove Port") {

            @Override
            public void dispose() {

                RemovePortDialog.this.disposing();

                super.dispose();
            }
    };

    private final JButton okay = new JButton("Okay");
    private final JButton cancel = new JButton("Cancel");
    private final List<JRadioButton> buttons = new ArrayList<JRadioButton>();

    private final List<Integer> ports = new ArrayList<Integer>();

    public RemovePortDialog(Set<Integer> choices) {

        fill(choices);

        okay.addActionListener(this);
        cancel.addActionListener(this);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setModal(true);

        Utilities.center(DiscoverFrame.getFrame(), dialog);

        dialog.setVisible(true);
    }

    public List<Integer> getPorts() {

        return ports;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == okay) {

            removePorts();
        }
        else if (event.getSource() == cancel) {

            dialog.dispose();
        }
    }

    private void disposing() {

        okay.removeActionListener(this);
        cancel.removeActionListener(this);
    }

    private void removePorts() {

        for(JRadioButton button : buttons) {

            if (button.isSelected()) {

                try {

                    Integer port = Integer.parseInt(button.getText());

                    if (port != null) {

                        ports.add(port);
                    }
                }
                catch(NumberFormatException exception) {

                }
            }
        }

        dialog.dispose();
    }

    private void fill(Set<Integer> choices) {

        Utilities.setGridBagLayout(dialog.getContentPane());

        Utilities.addComponent(
            dialog.getContentPane(),
            new JLabel("Select ports to remove:"),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 5, 5, 5));

        int count = 0;

        for(Integer integer : choices) {

            String name = Integer.toString(integer);
            JRadioButton button = new JRadioButton(name);

            button.setActionCommand(name);

            buttons.add(button);

            Utilities.addComponent(
                dialog.getContentPane(),
                button,
                Utilities.HORIZONTAL,
                0, (count + 1),
                1, 1,
                0.0, 0.0,
                Utilities.getInsets(0, 20, 0, 5));

            count++;
        }

        Utilities.addComponent(
            dialog.getContentPane(),
            getButtonPanel(),
            Utilities.HORIZONTAL,
            0, (choices.size() + 1),
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 5, 2, 5));
    }

    private JPanel getButtonPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 2));

        panel.add(okay);
        panel.add(cancel);

        return panel;
    }
}
