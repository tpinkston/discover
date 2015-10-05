/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.common.ExtendedEquipmentLifeform;
import discover.vdis.common.ExtendedStatus;
import discover.vdis.common.LifeformAttributes;
import discover.vdis.enums.VDIS;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.LegacyExtendedLifeformAppearanceVPR;

public class LegacyExtendedLifeformAppearanceWidget extends AbstractVariableRecordWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");
    private final JComboBox<String> clothing = new JComboBox<>();
    private final JComboBox<String> primaryColor = new JComboBox<>();
    private final JComboBox<String> secondaryColor = new JComboBox<>();
    private final BitRecordWidget status;
    private final BitRecordWidget equipment;
    private final BitRecordWidget attributes;
    
    public LegacyExtendedLifeformAppearanceWidget() {
        
        super("Legacy Extended Lifeform Appearance");
        
        this.status = new BitRecordWidget(new ExtendedStatus());
        this.equipment = new BitRecordWidget(new ExtendedEquipmentLifeform());
        this.attributes = new BitRecordWidget(new LifeformAttributes());
        
        Utilities.configureComboBox(
            this.clothing, 
            VDIS.LF_CLOTH_SCHEME,
            false);
        Utilities.configureComboBox(
            this.primaryColor, 
            VDIS.COLORS,
            false);
        Utilities.configureComboBox(
            this.secondaryColor, 
            VDIS.COLORS,
            false);
        
        this.fill();
    }
    
    @Override
    public AbstractVPRecord getRecord() {
        
        LegacyExtendedLifeformAppearanceVPR record;
        
        record = new LegacyExtendedLifeformAppearanceVPR();
        
        record.setClothing(Utilities.getComboboxValue(
            this.clothing, 
            VDIS.LF_CLOTH_SCHEME));
        record.setPrimaryColor(Utilities.getComboboxValue(
            this.primaryColor, 
            VDIS.COLORS));
        record.setSecondaryColor(Utilities.getComboboxValue(
            this.secondaryColor, 
            VDIS.COLORS));
        
        this.status.applyValue(record.getStatus());
        this.equipment.applyValue(record.getEquipment());
        this.attributes.applyValue(record.getAttributes());
        
        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {
        
        LegacyExtendedLifeformAppearanceVPR appearance;
        
        if (record instanceof LegacyExtendedLifeformAppearanceVPR) {
            
            appearance = (LegacyExtendedLifeformAppearanceVPR)record;

            Utilities.setComboBoxValue(
                this.clothing, 
                VDIS.LF_CLOTH_SCHEME, 
                appearance.getClothing());
            Utilities.setComboBoxValue(
                this.primaryColor, 
                VDIS.COLORS, 
                appearance.getPrimaryColor());
            Utilities.setComboBoxValue(
                this.secondaryColor, 
                VDIS.COLORS, 
                appearance.getSecondaryColor());

            this.equipment.setValue(appearance.getEquipment());
            this.status.setValue(appearance.getStatus());
            this.attributes.setValue(appearance.getAttributes());
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
            new JLabel("Clothing Scheme:"), 
            Utilities.HORIZONTAL, 
            0, 0, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.clothing, 
            Utilities.HORIZONTAL, 
            1, 0, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel, 
            new JLabel("Primary Color:"), 
            Utilities.HORIZONTAL, 
            0, 1, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.primaryColor, 
            Utilities.HORIZONTAL, 
            1, 1, 
            2, 1, 
            0.0, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel, 
            new JLabel("Secondary Color:"), 
            Utilities.HORIZONTAL, 
            0, 2, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.secondaryColor, 
            Utilities.HORIZONTAL, 
            1, 2, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.status.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 3, 
            2, 1, 
            0.0, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.equipment.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 4, 
            2, 1, 
            0.0, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.attributes.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 5, 
            2, 1, 
            0.0, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel, 
            super.remove, 
            Utilities.HORIZONTAL, 
            0, 6, 
            2, 1, 
            0.5, 0.0, 
            Utilities.getInsets(8, 2, 2, 2));
    }
}
