/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import javax.swing.JButton;

import discover.vdis.vprecords.AbstractVPRecord;

public abstract class AbstractVariableRecordWidget extends ToggleWidget {

    public static final String REMOVE = "Remove ";
    
    protected final JButton remove = new JButton();

    protected AbstractVariableRecordWidget(String title) {
        
        super(title);
        
        this.remove.setText(REMOVE + title);
        this.remove.setActionCommand(REMOVE);
    }
    
    public abstract AbstractVPRecord getRecord();

    public abstract void setRecord(AbstractVPRecord record);

    JButton getRemoveButton() {
        
        return this.remove;
    }
}
