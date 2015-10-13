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

/**
 * @author Tony Pinkston
 */
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

        status = new BitRecordWidget(new ExtendedStatus());
        equipment = new BitRecordWidget(new ExtendedEquipmentLifeform());
        attributes = new BitRecordWidget(new LifeformAttributes());

        Utilities.configureComboBox(
            clothing,
            VDIS.LF_CLOTH_SCHEME,
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
    public AbstractVPRecord getRecord() {

        LegacyExtendedLifeformAppearanceVPR record;

        record = new LegacyExtendedLifeformAppearanceVPR();

        record.setClothing(Utilities.getComboboxValue(
            clothing,
            VDIS.LF_CLOTH_SCHEME));
        record.setPrimaryColor(Utilities.getComboboxValue(
            primaryColor,
            VDIS.COLORS));
        record.setSecondaryColor(Utilities.getComboboxValue(
            secondaryColor,
            VDIS.COLORS));

        status.applyValue(record.getStatus());
        equipment.applyValue(record.getEquipment());
        attributes.applyValue(record.getAttributes());

        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {

        LegacyExtendedLifeformAppearanceVPR appearance;

        if (record instanceof LegacyExtendedLifeformAppearanceVPR) {

            appearance = (LegacyExtendedLifeformAppearanceVPR)record;

            Utilities.setComboBoxValue(
                clothing,
                VDIS.LF_CLOTH_SCHEME,
                appearance.getClothing());
            Utilities.setComboBoxValue(
                primaryColor,
                VDIS.COLORS,
                appearance.getPrimaryColor());
            Utilities.setComboBoxValue(
                secondaryColor,
                VDIS.COLORS,
                appearance.getSecondaryColor());

            equipment.setValue(appearance.getEquipment());
            status.setValue(appearance.getStatus());
            attributes.setValue(appearance.getAttributes());
        }
    }

    @Override
    public JComponent getComponent() {

        return panel;
    }

    @Override
    protected void fill() {

        super.fill();

        Utilities.addComponent(
            panel,
            new JLabel("Clothing Scheme:"),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            clothing,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("Primary Color:"),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            primaryColor,
            Utilities.HORIZONTAL,
            1, 1,
            2, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("Secondary Color:"),
            Utilities.HORIZONTAL,
            0, 2,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            secondaryColor,
            Utilities.HORIZONTAL,
            1, 2,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            status.getPanel(),
            Utilities.HORIZONTAL,
            0, 3,
            2, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            equipment.getPanel(),
            Utilities.HORIZONTAL,
            0, 4,
            2, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            attributes.getPanel(),
            Utilities.HORIZONTAL,
            0, 5,
            2, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            getRemoveButton(),
            Utilities.HORIZONTAL,
            0, 6,
            2, 1,
            0.5, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
    }
}
