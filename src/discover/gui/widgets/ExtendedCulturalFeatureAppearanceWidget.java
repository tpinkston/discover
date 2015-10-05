/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.common.ExtendedEquipmentCulturalFeature;
import discover.vdis.common.ExtendedStatus;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.ExtendedCulturalFeatureAppearanceVPR;

public class ExtendedCulturalFeatureAppearanceWidget extends AbstractVariableRecordWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");
    private final BitRecordWidget status;
    private final BitRecordWidget equipment;

    public ExtendedCulturalFeatureAppearanceWidget() {

        super("Extended Cultural Feature Appearance");

        this.status = new BitRecordWidget(new ExtendedStatus());
        this.equipment = new BitRecordWidget(new ExtendedEquipmentCulturalFeature());

        this.fill();
    }

    @Override
    public AbstractVPRecord getRecord() {

        ExtendedCulturalFeatureAppearanceVPR record;

        record = new ExtendedCulturalFeatureAppearanceVPR();

        this.status.applyValue(record.getStatus());
        this.equipment.applyValue(record.getEquipment());

        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {

        ExtendedCulturalFeatureAppearanceVPR appearance;

        if (record instanceof ExtendedCulturalFeatureAppearanceVPR) {

            appearance = (ExtendedCulturalFeatureAppearanceVPR)record;

            this.status.setValue(appearance.getStatus());
            this.equipment.setValue(appearance.getEquipment());
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
            this.status.getPanel(),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.equipment.getPanel(),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
        Utilities.addComponent(
            this.panel,
            super.remove,
            Utilities.HORIZONTAL,
            0, 2,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
    }
}
