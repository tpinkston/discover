package discover.gui.dialogs;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;

/**
 * @author Tony Pinkston
 */
public class GetPortDialog implements ActionListener {

    @SuppressWarnings("serial")
    private final JDialog dialog = new JDialog(DiscoverFrame.getFrame(), "") {

            @Override
            public void dispose() {

                GetPortDialog.this.disposing();

                super.dispose();
            }
    };

    private final JTextField text = new JTextField(10);
    private final JButton port3000 = new JButton("3000");
    private final JButton port4000 = new JButton("4000");
    private final JButton port5000 = new JButton("5000");
    private final JButton okay = new JButton("Okay");
    private final JButton cancel = new JButton("Cancel");

    private Integer port = null;

    /**
     * @param title - Dialog title
     * @param ports - List of ports already in use by caller.
     */
    public GetPortDialog(String title, Set<Integer> ports) {

        dialog.setTitle(title);

        if (ports != null) {

            port3000.setEnabled(!ports.contains(new Integer(3000)));
            port4000.setEnabled(!ports.contains(new Integer(4000)));
            port5000.setEnabled(!ports.contains(new Integer(5000)));
        }

        fill();

        text.addActionListener(this);
        port3000.addActionListener(this);
        port4000.addActionListener(this);
        port5000.addActionListener(this);
        okay.addActionListener(this);
        cancel.addActionListener(this);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setModal(true);

        Utilities.center(DiscoverFrame.getFrame(), dialog);

        dialog.setVisible(true);
    }

    public Integer getPort() {

        return port;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == text) {

            addPort(null);
        }
        else if (event.getSource() == port3000) {

            addPort(3000);
        }
        else if (event.getSource() == port4000) {

            addPort(4000);
        }
        else if (event.getSource() == port5000) {

            addPort(5000);
        }
        else if (event.getSource() == okay) {

            addPort(null);
        }
        else if (event.getSource() == cancel) {

            dialog.dispose();
        }
    }

    private void disposing() {

        text.removeActionListener(this);
        port3000.removeActionListener(this);
        port4000.removeActionListener(this);
        port5000.removeActionListener(this);
        okay.removeActionListener(this);
        cancel.removeActionListener(this);
    }

    private void addPort(Integer integer) {

        port = integer;

        if (port == null) {

            try {

                port = Integer.parseInt(text.getText());
            }
            catch(NumberFormatException exception) {

                JOptionPane.showMessageDialog(
                    dialog,
                    "\"" + text.getText() + "\"",
                    "Invalid Port",
                    JOptionPane.ERROR_MESSAGE);

                port = null;
                text.setText("");
            }
        }

        if (port != null) {

            dialog.dispose();
        }
    }

    private void fill() {

        Utilities.setGridBagLayout(dialog.getContentPane());

        Utilities.addComponent(
            dialog.getContentPane(),
            getNorthPanel(),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 1.0,
            Utilities.getInsets(0, 0, 0, 0));

        Utilities.addComponent(
            dialog.getContentPane(),
            getSouthPanel(),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            1.0, 1.0,
            Utilities.getInsets(10, 5, 2, 5));
    }

    private JPanel getNorthPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        Utilities.addComponent(
            panel,
            text,
            Utilities.HORIZONTAL,
            0, 0,
            3, 1,
            1.0, 0.0,
            Utilities.getInsets(5, 5, 2, 5));

        Utilities.addComponent(
            panel,
            port3000,
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.3, 0.0,
            Utilities.getInsets(2, 5, 5, 2));

        Utilities.addComponent(
            panel,
            port4000,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            0.3, 0.0,
            Utilities.getInsets(2, 2, 5, 2));

        Utilities.addComponent(
            panel,
            port5000,
            Utilities.HORIZONTAL,
            2, 1,
            1, 1,
            0.3, 0.0,
            Utilities.getInsets(2, 2, 5, 5));

        return panel;
    }

    private JPanel getSouthPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 2));

        panel.add(okay);
        panel.add(cancel);

        return panel;
    }
}
