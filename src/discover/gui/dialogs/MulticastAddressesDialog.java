package discover.gui.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;
import discover.system.Network;

/**
 * @author Tony Pinkston
 */
public class MulticastAddressesDialog implements ActionListener, ListSelectionListener {

    public static final String TITLE = "Multicast Addresses";

    private static final Dimension LIST_SIZE = new Dimension(200, 200);
    private static final Dimension BUTTON_SIZE = new Dimension(100, 22);

    private static final String ADD = "Add";
    private static final String DELETE = "Delete";
    private static final String DEFAULT = "Default";

    @SuppressWarnings("serial")
    private final JDialog dialog = new JDialog(
        DiscoverFrame.getFrame(),
        TITLE) {

            @Override
            public void dispose() {

                MulticastAddressesDialog.this.disposing();

                super.dispose();
            }
    };

    private final JList<String> list;
    private final JButton addAddress;
    private final JButton deleteAddresses;
    private final JButton defaultAddresses;

    public MulticastAddressesDialog() {

        list = new JList<>();
        list.getSelectionModel().addListSelectionListener(this);

        addAddress = new JButton(ADD);
        addAddress.addActionListener(this);

        deleteAddresses = new JButton(DELETE);
        deleteAddresses.addActionListener(this);
        deleteAddresses.setEnabled(false);

        defaultAddresses = new JButton(DEFAULT);
        defaultAddresses.addActionListener(this);

        refreshList();
        fill();

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setModal(true);

        Utilities.center(DiscoverFrame.getFrame(), dialog);

        dialog.setVisible(true);
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {

        if (!event.getValueIsAdjusting()) {

        	int size = list.getSelectedValuesList().size();

            deleteAddresses.setEnabled(size > 0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getActionCommand().equals(ADD)) {

            String address = JOptionPane.showInputDialog(
                dialog,
                "Enter IP address:",
                TITLE,
                JOptionPane.PLAIN_MESSAGE);

            if (address != null) {

                address = address.toLowerCase();

                String result = Network.addMulticastAddress(address);

                if (result == null) {

                    refreshList();
                }
                else {

                    JOptionPane.showMessageDialog(
                        dialog,
                        ("Error with address " + address + "\n" + result),
                        TITLE,
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (event.getActionCommand().equals(DELETE)) {

            List<String> values = list.getSelectedValuesList();

            if ((values != null) && (values.size() > 0)) {

                StringBuffer buffer;

                if (values.size() == 1) {

                    buffer = new StringBuffer("Remove address:");
                }
                else {

                    buffer = new StringBuffer("Remove addresses:");
                }

                for(Object value : values) {

                    buffer.append("\n" + value.toString());
                }

                int result = JOptionPane.showConfirmDialog(
                    dialog,
                    buffer.toString(),
                    TITLE,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {

                    for(Object value : values) {

                        Network.removeMulticastAddress(value.toString());
                    }

                    refreshList();
                }
            }
        }
        else if (event.getActionCommand().equals(DEFAULT)) {

            int result = JOptionPane.showConfirmDialog(
                dialog,
                "Use default addresses?  This will remove any non-default\n" +
                "addresses currently listed and cannot be undone.",
                TITLE,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {

                Network.defaultMulticastAddress();
                refreshList();
            }
        }
    }

    private void disposing() {

        list.getSelectionModel().removeListSelectionListener(this);
        addAddress.removeActionListener(this);
        deleteAddresses.removeActionListener(this);
        defaultAddresses.removeActionListener(this);
    }

    private void refreshList() {

        list.setListData(Network.getMulticastAddresses());
        list.getSelectionModel().setSelectionInterval(-1, -1);
    }

    private void fill() {

        list.setPreferredSize(LIST_SIZE);
        addAddress.setPreferredSize(BUTTON_SIZE);
        deleteAddresses.setPreferredSize(BUTTON_SIZE);
        defaultAddresses.setPreferredSize(BUTTON_SIZE);

        Utilities.setGridBagLayout(dialog.getContentPane());

        Utilities.addComponent(
            dialog.getContentPane(),
            new JScrollPane(list),
            Utilities.BOTH,
            0, 0,
            3, 1,
            1.0, 1.0,
            Utilities.getInsets(10, 10, 3, 10));
        Utilities.addComponent(
            dialog.getContentPane(),
            this.addAddress,
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(1, 10, 10, 2));
        Utilities.addComponent(
            dialog.getContentPane(),
            deleteAddresses,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(1, 2, 10, 2));
        Utilities.addComponent(
            dialog.getContentPane(),
            defaultAddresses,
            Utilities.HORIZONTAL,
            2, 1,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(1, 2, 10, 10));
    }
}
