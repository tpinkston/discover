/**
 * @author Tony Pinkston
 */
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

        this.dialog.setTitle(title);

        if (ports != null) {

            this.port3000.setEnabled(!ports.contains(new Integer(3000)));
            this.port4000.setEnabled(!ports.contains(new Integer(4000)));
            this.port5000.setEnabled(!ports.contains(new Integer(5000)));
        }

        this.fill();

        this.text.addActionListener(this);
        this.port3000.addActionListener(this);
        this.port4000.addActionListener(this);
        this.port5000.addActionListener(this);
        this.okay.addActionListener(this);
        this.cancel.addActionListener(this);

        this.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.dialog.pack();
        this.dialog.setModal(true);

        Utilities.center(DiscoverFrame.getFrame(), this.dialog);

        this.dialog.setVisible(true);
    }

    public Integer getPort() {

        return this.port;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == this.text) {

            this.addPort(null);
        }
        else if (event.getSource() == this.port3000) {

            this.addPort(3000);
        }
        else if (event.getSource() == this.port4000) {

            this.addPort(4000);
        }
        else if (event.getSource() == this.port5000) {

            this.addPort(5000);
        }
        else if (event.getSource() == this.okay) {

            this.addPort(null);
        }
        else if (event.getSource() == this.cancel) {

            this.dialog.dispose();
        }
    }

    private void disposing() {

        this.text.removeActionListener(this);
        this.port3000.removeActionListener(this);
        this.port4000.removeActionListener(this);
        this.port5000.removeActionListener(this);
        this.okay.removeActionListener(this);
        this.cancel.removeActionListener(this);
    }

    private void addPort(Integer integer) {

        this.port = integer;

        if (this.port == null) {

            try {

                this.port = Integer.parseInt(this.text.getText());
            }
            catch(NumberFormatException exception) {

                JOptionPane.showMessageDialog(
                    this.dialog,
                    "\"" + this.text.getText() + "\"",
                    "Invalid Port",
                    JOptionPane.ERROR_MESSAGE);

                this.port = null;
                this.text.setText("");
            }
        }

        if (this.port != null) {

            this.dialog.dispose();
        }
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

        Utilities.addComponent(
            panel,
            this.text,
            Utilities.HORIZONTAL,
            0, 0,
            3, 1,
            1.0, 0.0,
            Utilities.getInsets(5, 5, 2, 5));

        Utilities.addComponent(
            panel,
            this.port3000,
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.3, 0.0,
            Utilities.getInsets(2, 5, 5, 2));

        Utilities.addComponent(
            panel,
            this.port4000,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            0.3, 0.0,
            Utilities.getInsets(2, 2, 5, 2));

        Utilities.addComponent(
            panel,
            this.port5000,
            Utilities.HORIZONTAL,
            2, 1,
            1, 1,
            0.3, 0.0,
            Utilities.getInsets(2, 2, 5, 5));

        return panel;
    }

    private JPanel getSouthPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 2));

        panel.add(this.okay);
        panel.add(this.cancel);

        return panel;
    }
}
