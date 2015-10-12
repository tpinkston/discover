package discover.gui.widgets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.common.ExtendedEquipmentCulturalFeature;
import discover.vdis.common.ExtendedStatus;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.ExtendedCulturalFeatureAppearanceVPR;

/**
 * @author Tony Pinkston
 */
public class ExtendedCulturalFeatureAppearanceWidget extends AbstractVariableRecordWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");
    private final BitRecordWidget status;
    private final BitRecordWidget equipment;

    public ExtendedCulturalFeatureAppearanceWidget() {

        super("Extended Cultural Feature Appearance");

        status = new BitRecordWidget(new ExtendedStatus());
        equipment = new BitRecordWidget(new ExtendedEquipmentCulturalFeature());

        fill();
    }

    @Override
    public AbstractVPRecord getRecord() {

        ExtendedCulturalFeatureAppearanceVPR record;

        record = new ExtendedCulturalFeatureAppearanceVPR();

        status.applyValue(record.getStatus());
        equipment.applyValue(record.getEquipment());

        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {

        ExtendedCulturalFeatureAppearanceVPR appearance;

        if (record instanceof ExtendedCulturalFeatureAppearanceVPR) {

            appearance = (ExtendedCulturalFeatureAppearanceVPR)record;

            status.setValue(appearance.getStatus());
            equipment.setValue(appearance.getEquipment());
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
            status.getPanel(),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
        Utilities.addComponent(
            panel,
            equipment.getPanel(),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
        Utilities.addComponent(
            panel,
            remove,
            Utilities.HORIZONTAL,
            0, 2,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
    }
}
