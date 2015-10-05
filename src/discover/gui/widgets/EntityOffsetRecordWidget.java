/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.enums.VDIS;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.EntityAssociationVPR;
import discover.vdis.vprecords.EntityOffsetVPR;

public class EntityOffsetRecordWidget extends AbstractVariableRecordWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");

    private JComboBox<String> type = new JComboBox<>();
    private CartesianWidget offset = new CartesianWidget("Offset");

    public EntityOffsetRecordWidget() {
        
        super("Entity Offset");

        Utilities.configureComboBox(
            this.type, 
            VDIS.OFFSET_TYPE,
            false);
        
        this.fill();
    }
    
    @Override
    public AbstractVPRecord getRecord() {
        
        EntityOffsetVPR record;
        
        record = new EntityOffsetVPR();
        
        record.setType(Utilities.getComboboxValue(
            this.type, 
            VDIS.OFFSET_TYPE));
        
        this.offset.getValue(record.getOffset());
        
        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {
        
        if (record instanceof EntityAssociationVPR) {
            
            EntityOffsetVPR offset = (EntityOffsetVPR)record;

            Utilities.setComboBoxValue(
                this.type, 
                VDIS.OFFSET_TYPE, 
                offset.getType());

            this.offset.setValue(offset.getOffset());
        }
    }

    @Override
    public JComponent getComponent() {

        return this.panel;
    }
    
    protected void fill() {
        
        super.fill();
        
        Utilities.addComponent(
            this.panel, 
            new JLabel("Offset Type:"), 
            Utilities.HORIZONTAL, 
            0, 0, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.type, 
            Utilities.HORIZONTAL, 
            1, 0, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            this.panel, 
            this.offset.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 1, 
            2, 1, 
            1.0, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            this.panel, 
            super.getRemoveButton(), 
            Utilities.HORIZONTAL, 
            0, 2, 
            2, 1, 
            1.0, 0.0, 
            Utilities.getInsets(8, 2, 2, 2));
    }
}
