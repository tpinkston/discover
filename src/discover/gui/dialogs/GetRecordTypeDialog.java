/**
 * @author Tony Pinkston
 */
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
import discover.vdis.enums.VDIS;

public class GetRecordTypeDialog implements ActionListener {

    private static final int TYPES[] = {

        0,  // VP_RECORD_TYPE_ARTICULATED_PART
        4,  // VP_RECORD_TYPE_ENTITY_ASSOC
        20, // VP_RECORD_TYPE_EXT_PLATFORM_APP
        25, // VP_RECORD_TYPE_ENTITY_OFFSET
        26, // VP_RECORD_TYPE_DEAD_RECKONING
        30, // VP_RECORD_TYPE_LEGACY_EXT_LIFEFORM_APP
        31, // VP_RECORD_TYPE_EXT_CULT_FEAT_APP
        32  // VP_RECORD_TYPE_EXT_SUPPLY_APP
    };

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

        this.fill();

        this.okay.addActionListener(this);
        this.cancel.addActionListener(this);

        this.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.dialog.pack();
        this.dialog.setModal(true);

        Utilities.center(DiscoverFrame.getFrame(), this.dialog);

        this.dialog.setVisible(true);
    }

    public Integer getRecordType() {

        return this.type;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == this.okay) {

            for(int i = 0; i < this.buttons.size(); ++i) {

                if (this.buttons.get(i).isSelected()) {

                    this.type = TYPES[i];
                }
            }
        }

        this.dialog.dispose();
    }

    private void fill() {


        Utilities.setGridBagLayout(this.dialog.getContentPane());

        Utilities.addComponent(
            this.dialog.getContentPane(),
            new JLabel("Select record type:"),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 5, 5, 5));

        int count = 0;

        for(int type : TYPES) {

            String name = VDIS.getDescription(VDIS.VP_RECORD_TYPE, type);
            JRadioButton button = new JRadioButton(name);

            if (type == 0) {

                button.setSelected(true);
            }

            this.group.add(button);

            this.buttons.add(button);

            Utilities.addComponent(
                this.dialog.getContentPane(),
                button,
                Utilities.HORIZONTAL,
                0, (count + 1),
                1, 1,
                0.0, 0.0,
                Utilities.getInsets(0, 20, 0, 5));

            count++;
        }

        Utilities.addComponent(
            this.dialog.getContentPane(),
            this.getButtonPanel(),
            Utilities.HORIZONTAL,
            0, (TYPES.length + 1),
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 5, 2, 5));
    }

    private JPanel getButtonPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 2));

        panel.add(this.okay);
        panel.add(this.cancel);

        return panel;
    }
}
