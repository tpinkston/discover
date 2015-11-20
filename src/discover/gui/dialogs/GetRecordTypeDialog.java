package discover.gui.dialogs;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;
import discover.vdis.enums.VP_RECORD_TYPE;

/**
 * @author Tony Pinkston
 */
public class GetRecordTypeDialog implements ActionListener {

    private static final List<VP_RECORD_TYPE> PDU_TYPES = VP_RECORD_TYPE.values(true);

    @SuppressWarnings("serial")
    private final JDialog dialog = new JDialog(
        DiscoverFrame.getFrame(),
        "Variable Parameter Records") {

        @Override
        public void dispose() {

            okay.removeActionListener(GetRecordTypeDialog.this);
            cancel.removeActionListener(GetRecordTypeDialog.this);

            super.dispose();
        }
    };

    private final ButtonGroup group = new ButtonGroup();
    private final JButton okay = new JButton("Okay");
    private final JButton cancel = new JButton("Cancel");
    private final List<JRadioButton> buttons = new ArrayList<JRadioButton>();

    private int type = -1;

    public GetRecordTypeDialog() {

        fill();

        okay.addActionListener(this);
        cancel.addActionListener(this);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setModal(true);

        Utilities.center(DiscoverFrame.getFrame(), dialog);

        dialog.setVisible(true);
    }

    public Integer getRecordType() {

        return type;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == okay) {

            for(int i = 0; i < buttons.size(); ++i) {

                if (buttons.get(i).isSelected()) {

                    type = PDU_TYPES.get(i).value;
                }
            }
        }

        dialog.dispose();
    }

    private void fill() {


        Utilities.setGridBagLayout(dialog.getContentPane());

        Utilities.addComponent(
            dialog.getContentPane(),
            new JLabel("Select record type:"),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 5, 5, 5));

        int count = 0;

        for(VP_RECORD_TYPE type : PDU_TYPES) {

            JRadioButton button = new JRadioButton(type.description);

            if (type.value == 0) {

                button.setSelected(true);
            }

            group.add(button);

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
            0, (PDU_TYPES.size() + 1),
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
