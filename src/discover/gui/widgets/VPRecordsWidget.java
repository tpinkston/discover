/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import discover.gui.Utilities;
import discover.gui.dialogs.GetRecordTypeDialog;
import discover.gui.frames.DiscoverFrame;
import discover.vdis.vprecords.AbstractVPRecord;

public class VPRecordsWidget extends Widget {

    private static final String ADD = "Add Record";

    private final JButton add = new JButton(ADD);
    private final List<AbstractVariableRecordWidget> widgets;

    private int y = 0;

    public VPRecordsWidget() {

        super("Variable Parameter Records");

        this.widgets = new ArrayList<AbstractVariableRecordWidget>();

        this.add.setActionCommand(ADD);
        this.add.addActionListener(this);

        Utilities.addComponent(
            super.getPanel(),
            this.add,
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(4, 2, 4, 2));
    }

    public void getRecords(List<AbstractVPRecord> list) {

        list.clear();

        for(AbstractVariableRecordWidget widget : this.widgets) {

            list.add(widget.getRecord());
        }
    }

    public void setRecords(List<AbstractVPRecord> list) {

        for(AbstractVPRecord record : list) {

            this.addRecord(record);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();

        if (command == ADD) {

            this.addRecord(null);
        }
        else if (command == AbstractVariableRecordWidget.REMOVE) {

            this.removeRecord(event.getSource());
        }
    }

    private void addRecord(AbstractVPRecord record) {

        AbstractVariableRecordWidget widget = null;

        if (record == null) {

            GetRecordTypeDialog dialog = new GetRecordTypeDialog();

            widget = this.getRecord(dialog.getRecordType());
        }
        else {

            widget = this.getRecord(record.getRecordType());
        }

        if (widget != null) {

            if (record != null) {

                widget.setRecord(record);
            }

            widget.getRemoveButton().addActionListener(this);

            Utilities.addComponent(
                super.getPanel(),
                widget.getPanel(),
                Utilities.HORIZONTAL,
                0, ++this.y,
                1, 1,
                1.0, 0.0,
                Utilities.getInsets(4, 2, 2, 2));

            super.getPanel().revalidate();
            this.widgets.add(widget);
        }
    }

    private AbstractVariableRecordWidget getRecord(int type) {

        AbstractVariableRecordWidget widget = null;

        switch(type) {

            case 0:  // VP_RECORD_TYPE_ARTICULATED_PART
                widget = new ArticulatedPartWidget();
                break;
            case 4:  // VP_RECORD_TYPE_ENTITY_ASSOC
                widget = new EntityAssociationRecordWidget();
                break;
            case 20: // VP_RECORD_TYPE_EXT_PLATFORM_APP
                widget = new ExtendedPlatformAppearanceWidget();
                break;
            case 25: // VP_RECORD_TYPE_ENTITY_OFFSET
                widget = new EntityOffsetRecordWidget();
                break;
            case 26: // VP_RECORD_TYPE_DEAD_RECKONING
                widget = new DeadReckoningRecordWidget();
                break;
            case 30: // VP_RECORD_TYPE_LEGACY_EXT_LIFEFORM_APP
                widget = new LegacyExtendedLifeformAppearanceWidget();
                break;
            case 31: // VP_RECORD_TYPE_EXT_CULT_FEAT_APP
                widget = new ExtendedCulturalFeatureAppearanceWidget();
                break;
            case 32: // VP_RECORD_TYPE_EXT_SUPPLY_APP
                widget = new ExtendedSupplyAppearanceWidget();
                break;
        }

        return widget;
    }

    private void removeRecord(Object source) {

        AbstractVariableRecordWidget widget = null;
        int size = this.widgets.size();

        for(int i = 0; (i < size) && (widget == null); ++i) {

            widget = this.widgets.get(i);

            if (source != widget.getRemoveButton()) {

                widget = null;
            }
        }

        if (widget != null) {

            String title = widget.getLabel().getText();

            int choice = JOptionPane.showConfirmDialog(
                DiscoverFrame.getFrame(),
                "Remove: " + title + "?\n" +
                "This cannot be undone...",
                "Remove Record",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {

                widget.removed();
                widget.getRemoveButton().removeActionListener(this);

                this.widgets.remove(widget);
                super.getPanel().remove(widget.getPanel());
                super.getPanel().revalidate();
            }
        }
    }
}
