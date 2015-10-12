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

/**
 * @author Tony Pinkston
 */
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

        group.add(air);
        group.add(land);

        air.addActionListener(this);
        land.addActionListener(this);
        land.setSelected(true);

        status = new BitRecordWidget(new ExtendedStatus());
        lights = new BitRecordWidget(new ExtendedLightsLand());
        equipment = new BitRecordWidget(new ExtendedEquipmentLand());
        primaryCondition = new BitRecordWidget(new ConditionMaterial());
        secondaryCondition = new BitRecordWidget(new ConditionMaterial());
        thermalIndicators = new BitRecordWidget(new ThermalIndicators());

        primaryCondition.setText("Primary Condition");
        secondaryCondition.setText("Secondary Condition");

        Utilities.configureComboBox(
            paint,
            VDIS.PL_PAINT_SCHEME,
            false);
        Utilities.configureComboBox(
            decal,
            VDIS.PL_DECAL_SCHEME,
            false);
        Utilities.configureComboBox(
            primaryColor,
            VDIS.COLORS,
            false);
        Utilities.configureComboBox(
            secondaryColor,
            VDIS.COLORS,
            false);

        fill();
    }

    @Override
    public JComponent getComponent() {

        return panel;
    }

    @Override
    public void removed() {

        air.removeActionListener(this);
        land.removeActionListener(this);

        super.removed();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        updateDomain();
    }

    @Override
    public AbstractVPRecord getRecord() {

        ExtendedPlatformAppearanceVPR record;

        record = new ExtendedPlatformAppearanceVPR();

        if (land.isSelected()) {

            record.setDomain(1); // DOMAIN_LAND
        }
        else {

            record.setDomain(2); // DOMAIN_AIR
        }

        record.setPaintScheme(Utilities.getComboboxValue(
            paint,
            VDIS.PL_PAINT_SCHEME));
        record.setDecalScheme(Utilities.getComboboxValue(
            decal,
            VDIS.PL_DECAL_SCHEME));
        record.setPrimaryColor(Utilities.getComboboxValue(
            primaryColor,
            VDIS.COLORS));
        record.setSecondaryColor(Utilities.getComboboxValue(
            secondaryColor,
            VDIS.COLORS));

        equipment.applyValue(record.getEquipment());
        lights.applyValue(record.getLights());
        primaryCondition.applyValue(record.getPrimaryCondition());
        secondaryCondition.applyValue(record.getSecondaryCondition());
        status.applyValue(record.getStatus());
        thermalIndicators.applyValue(record.getThermalIndicators());

        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {

        ExtendedPlatformAppearanceVPR appearance;

        if (record instanceof ExtendedPlatformAppearanceVPR) {

            appearance = (ExtendedPlatformAppearanceVPR)record;

            if (record.getDomain() == 1) {

                land.setSelected(true);
            }
            else {

                air.setSelected(true);
            }

            Utilities.setComboBoxValue(
                paint,
                VDIS.PL_PAINT_SCHEME,
                appearance.getPaintScheme());
            Utilities.setComboBoxValue(
                decal,
                VDIS.PL_DECAL_SCHEME,
                appearance.getDecalScheme());
            Utilities.setComboBoxValue(
                primaryColor,
                VDIS.COLORS,
                appearance.getPrimaryColor());
            Utilities.setComboBoxValue(
                secondaryColor,
                VDIS.COLORS,
                appearance.getSecondaryColor());

            equipment.setValue(appearance.getEquipment());
            lights.setValue(appearance.getLights());
            primaryCondition.setValue(appearance.getPrimaryCondition());
            secondaryCondition.setValue(appearance.getSecondaryCondition());
            status.setValue(appearance.getStatus());
            thermalIndicators.setValue(appearance.getThermalIndicators());
        }
    }

    protected void updateDomain() {

        Abstract32Bits lights;
        Abstract16Bits equipment;

        lights = (Abstract32Bits)this.lights.getValue();
        equipment = (Abstract16Bits)this.equipment.getValue();

        if (air.isSelected()) {

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
            panel,
            new JLabel("Paint Scheme:"),
            Utilities.HORIZONTAL,
            0, 0,
            2, 1,
            0.5, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            paint,
            Utilities.HORIZONTAL,
            2, 0,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("Decal Scheme:"),
            Utilities.HORIZONTAL,
            0, 1,
            2, 1,
            0.5, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            decal,
            Utilities.HORIZONTAL,
            2, 1,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("Primary Color:"),
            Utilities.HORIZONTAL,
            0, 2,
            2, 1,
            0.5, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            primaryColor,
            Utilities.HORIZONTAL,
            2, 2,
            2, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            primaryCondition.getPanel(),
            Utilities.HORIZONTAL,
            0, 3,
            3, 1,
            1.0, 0.0,
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("Secondary Color:"),
            Utilities.HORIZONTAL,
            0, 4,
            2, 1,
            0.5, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            secondaryColor,
            Utilities.HORIZONTAL,
            2, 4,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            secondaryCondition.getPanel(),
            Utilities.HORIZONTAL,
            0, 5,
            3, 1,
            1.0, 0.0,
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            panel,
            air,
            Utilities.HORIZONTAL,
            0, 6,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 6, 2, 2));
        Utilities.addComponent(
            panel,
            land,
            Utilities.HORIZONTAL,
            1, 6,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 6, 2, 2));
        Utilities.addComponent(
            panel,
            lights.getPanel(),
            Utilities.HORIZONTAL,
            0, 7,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            panel,
            equipment.getPanel(),
            Utilities.HORIZONTAL,
            0, 8,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            panel,
            status.getPanel(),
            Utilities.HORIZONTAL,
            0, 9,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            panel,
            thermalIndicators.getPanel(),
            Utilities.HORIZONTAL,
            0, 10,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 4, 2, 2));
        Utilities.addComponent(
            panel,
            getRemoveButton(),
            Utilities.HORIZONTAL,
            0, 11,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 4, 2, 2));
    }
}
