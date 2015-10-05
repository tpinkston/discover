/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import discover.gui.Utilities;
import discover.vdis.bits.Abstract16Bits;
import discover.vdis.bits.Abstract32Bits;
import discover.vdis.common.ConditionMaterial;
import discover.vdis.common.ExtendedEquipmentAir;
import discover.vdis.common.ExtendedEquipmentLand;
import discover.vdis.common.ExtendedLightsAir;
import discover.vdis.common.ExtendedLightsLand;
import discover.vdis.common.ExtendedStatus;
import discover.vdis.common.ThermalIndicators;
import discover.vdis.enums.VDIS;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.ExtendedPlatformAppearanceVPR;

public class ExtendedPlatformAppearanceWidget extends AbstractVariableRecordWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");
    private final JComboBox<String> paint = new JComboBox<>();
    private final JComboBox<String> decal = new JComboBox<>();
    private final JComboBox<String> primaryColor = new JComboBox<>();
    private final JComboBox<String> secondaryColor = new JComboBox<>();
    private final JRadioButton air = new JRadioButton("Air");
    private final JRadioButton land = new JRadioButton("Land");
    private final BitRecordWidget status;
    private final BitRecordWidget lights;
    private final BitRecordWidget equipment;
    private final BitRecordWidget primaryCondition;
    private final BitRecordWidget secondaryCondition;
    private final BitRecordWidget thermalIndicators;
    
    public ExtendedPlatformAppearanceWidget() {

        super("Extended Platform Appearance");
        
        ButtonGroup group = new ButtonGroup();
        
        group.add(this.air);
        group.add(this.land);
        
        this.air.addActionListener(this);
        this.land.addActionListener(this);
        this.land.setSelected(true);
        
        this.status = new BitRecordWidget(new ExtendedStatus());
        this.lights = new BitRecordWidget(new ExtendedLightsLand());
        this.equipment = new BitRecordWidget(new ExtendedEquipmentLand());
        this.primaryCondition = new BitRecordWidget(new ConditionMaterial());
        this.secondaryCondition = new BitRecordWidget(new ConditionMaterial());
        this.thermalIndicators = new BitRecordWidget(new ThermalIndicators());
        
        this.primaryCondition.setText("Primary Condition");
        this.secondaryCondition.setText("Secondary Condition");

        Utilities.configureComboBox(
            this.paint, 
            VDIS.PL_PAINT_SCHEME,
            false);
        Utilities.configureComboBox(
            this.decal, 
            VDIS.PL_DECAL_SCHEME,
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
    public JComponent getComponent() {

        return this.panel;
    }

    @Override
    public void removed() {

        this.air.removeActionListener(this);
        this.land.removeActionListener(this);

        super.removed();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        
        this.updateDomain();
    }

    @Override
    public AbstractVPRecord getRecord() {

        ExtendedPlatformAppearanceVPR record;
        
        record = new ExtendedPlatformAppearanceVPR();
        
        if (this.land.isSelected()) {
            
            record.setDomain(1); // DOMAIN_LAND
        }
        else {
            
            record.setDomain(2); // DOMAIN_AIR
        }

        record.setPaintScheme(Utilities.getComboboxValue(
            this.paint, 
            VDIS.PL_PAINT_SCHEME));
        record.setDecalScheme(Utilities.getComboboxValue(
            this.decal, 
            VDIS.PL_DECAL_SCHEME));
        record.setPrimaryColor(Utilities.getComboboxValue(
            this.primaryColor, 
            VDIS.COLORS));
        record.setSecondaryColor(Utilities.getComboboxValue(
            this.secondaryColor, 
            VDIS.COLORS));
        
        this.equipment.applyValue(record.getEquipment());
        this.lights.applyValue(record.getLights());
        this.primaryCondition.applyValue(record.getPrimaryCondition());
        this.secondaryCondition.applyValue(record.getSecondaryCondition());
        this.status.applyValue(record.getStatus());
        this.thermalIndicators.applyValue(record.getThermalIndicators());

        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {
        
        ExtendedPlatformAppearanceVPR appearance;
        
        if (record instanceof ExtendedPlatformAppearanceVPR) {
            
            appearance = (ExtendedPlatformAppearanceVPR)record;
            
            if (record.getDomain() == 1) {
                
                this.land.setSelected(true);
            }
            else {
                
                this.air.setSelected(true);
            }

            Utilities.setComboBoxValue(
                this.paint, 
                VDIS.PL_PAINT_SCHEME, 
                appearance.getPaintScheme());
            Utilities.setComboBoxValue(
                this.decal, 
                VDIS.PL_DECAL_SCHEME, 
                appearance.getDecalScheme());
            Utilities.setComboBoxValue(
                this.primaryColor, 
                VDIS.COLORS, 
                appearance.getPrimaryColor());
            Utilities.setComboBoxValue(
                this.secondaryColor, 
                VDIS.COLORS, 
                appearance.getSecondaryColor());

            this.equipment.setValue(appearance.getEquipment());
            this.lights.setValue(appearance.getLights());
            this.primaryCondition.setValue(appearance.getPrimaryCondition());
            this.secondaryCondition.setValue(appearance.getSecondaryCondition());
            this.status.setValue(appearance.getStatus());
            this.thermalIndicators.setValue(appearance.getThermalIndicators());
        }
    }

    protected void updateDomain() {
        
        Abstract32Bits lights;
        Abstract16Bits equipment;
        
        lights = (Abstract32Bits)this.lights.getValue();
        equipment = (Abstract16Bits)this.equipment.getValue();
        
        if (this.air.isSelected()) {
            
            lights = new ExtendedLightsAir(lights.get());
            equipment = new ExtendedEquipmentAir(equipment.get());
        }
        else {
            
            lights = new ExtendedLightsLand(lights.get());
            equipment = new ExtendedEquipmentLand(equipment.get());
        }
        
        this.lights.setValue(lights);
        this.equipment.setValue(equipment);
    }

    @Override
    protected void fill() {
        
        super.fill();
        
        Utilities.addComponent(
            this.panel, 
            new JLabel("Paint Scheme:"), 
            Utilities.HORIZONTAL, 
            0, 0, 
            2, 1, 
            0.5, 0.0, 
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.paint, 
            Utilities.HORIZONTAL, 
            2, 0, 
            1, 1, 
            0.5, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel, 
            new JLabel("Decal Scheme:"), 
            Utilities.HORIZONTAL, 
            0, 1, 
            2, 1, 
            0.5, 0.0, 
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.decal, 
            Utilities.HORIZONTAL, 
            2, 1, 
            1, 1, 
            0.5, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel, 
            new JLabel("Primary Color:"), 
            Utilities.HORIZONTAL, 
            0, 2, 
            2, 1, 
            0.5, 0.0, 
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.primaryColor, 
            Utilities.HORIZONTAL, 
            2, 2, 
            2, 1, 
            0.5, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.primaryCondition.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 3, 
            3, 1, 
            1.0, 0.0, 
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            this.panel, 
            new JLabel("Secondary Color:"), 
            Utilities.HORIZONTAL, 
            0, 4, 
            2, 1, 
            0.5, 0.0, 
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.secondaryColor, 
            Utilities.HORIZONTAL, 
            2, 4, 
            1, 1, 
            0.5, 0.0, 
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.secondaryCondition.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 5, 
            3, 1, 
            1.0, 0.0, 
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.air, 
            Utilities.HORIZONTAL, 
            0, 6, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(6, 6, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.land, 
            Utilities.HORIZONTAL, 
            1, 6, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(6, 6, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.lights.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 7, 
            3, 1, 
            0.0, 0.0, 
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.equipment.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 8, 
            3, 1, 
            0.0, 0.0, 
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.status.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 9, 
            3, 1, 
            0.0, 0.0, 
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.thermalIndicators.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 10, 
            3, 1, 
            0.0, 0.0, 
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            this.panel, 
            super.getRemoveButton(), 
            Utilities.HORIZONTAL, 
            0, 11, 
            3, 1, 
            0.0, 0.0, 
            Utilities.getInsets(8, 4, 2, 2));
    }
}
