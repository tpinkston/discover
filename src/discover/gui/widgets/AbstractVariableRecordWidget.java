package discover.gui.widgets;

import javax.swing.JButton;

import discover.vdis.vprecords.AbstractVPRecord;

/**
 * @author Tony Pinkston
 */
public abstract class AbstractVariableRecordWidget extends ToggleWidget {

    public static final String REMOVE = "Remove ";

    private final JButton remove = new JButton();

    protected AbstractVariableRecordWidget(String title) {

        super(title);

        remove.setText(REMOVE + title);
        remove.setActionCommand(REMOVE);
    }

    public abstract AbstractVPRecord getRecord();

    public abstract void setRecord(AbstractVPRecord record);

    JButton getRemoveButton() {

        return remove;
    }
}
