/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import discover.gui.Utilities;
import discover.vdis.enums.VDIS;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.ArticulatedPartVPR;

public class ArticulatedPartWidget extends AbstractVariableRecordWidget {

    private static final String LABELS[] = {

        "Part Type: ",
        "Part Metric: ",
        "Change Indicator: ",
        "Attachment Id: ",
        "Value: "
    };

    private final JPanel panel = Utilities.getGridBagPanel("");
    private final JComboBox<String> type;
    private final JComboBox<String> metric;
    private final JFormattedTextField change;
    private final JFormattedTextField attachment;
    private final JFormattedTextField value;

    public ArticulatedPartWidget() {

        super("Articulated Part");

        this.type = new JComboBox<>();
        this.metric = new JComboBox<>();
        this.change = Utilities.getIntegerField(0);
        this.attachment = Utilities.getIntegerField(0);
        this.value = Utilities.getFloatField(0.0f, null);

        this.fill();
    }

    @Override
    public JComponent getComponent() {

        return this.panel;
    }

    @Override
    public AbstractVPRecord getRecord() {

        ArticulatedPartVPR record = new ArticulatedPartVPR();

        record.setType(Utilities.getComboboxValue(
            this.type,
            VDIS.ARTICULATED_PARTS));
        record.setMetric(Utilities.getComboboxValue(
            this.metric,
            VDIS.ARTICULATED_PARTS_METRIC));

        record.setChange(Utilities.getIntegerValue(this.change));
        record.setAttachmentId(Utilities.getIntegerValue(this.attachment));
        record.setValue(Utilities.getFloatValue(this.value));

        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {

        if (record instanceof ArticulatedPartVPR) {

            ArticulatedPartVPR part = (ArticulatedPartVPR)record;

            Utilities.setComboBoxValue(
                this.type,
                VDIS.ARTICULATED_PARTS,
                part.getType());
            Utilities.setComboBoxValue(
                this.metric,
                VDIS.ARTICULATED_PARTS_METRIC,
                part.getMetric());

            this.change.setValue(part.getChange());
            this.attachment.setValue(part.getAttachmentId());
            this.value.setValue(part.getValue());
        }
    }

    @Override
    protected void fill() {

        super.fill();

        Utilities.configureComboBox(
            this.type,
            VDIS.ARTICULATED_PARTS,
            false);
        Utilities.configureComboBox(
            this.metric,
            VDIS.ARTICULATED_PARTS_METRIC,
            false);

        this.change.setValue(0);
        this.change.setHorizontalAlignment(JTextField.RIGHT);
        this.attachment.setValue(0);
        this.attachment.setHorizontalAlignment(JTextField.RIGHT);
        this.value.setValue(0.0f);
        this.value.setHorizontalAlignment(JTextField.RIGHT);

        JComponent components[] = new JComponent[5];

        components[0] = this.type;
        components[1] = this.metric;
        components[2] = this.change;
        components[3] = this.attachment;
        components[4] = this.value;

        for(int i = 0; i < components.length; ++i) {

            Utilities.addComponent(
                this.panel,
                new JLabel(LABELS[i]),
                Utilities.HORIZONTAL,
                0, i,
                1, 1,
                0.0, 0.0,
                Utilities.getInsets(14, 6, 2, 4));
            Utilities.addComponent(
                this.panel,
                components[i],
                Utilities.HORIZONTAL,
                1, i,
                1, 1,
                0.5, 0.0,
                Utilities.getInsets(8, 2, 2, 2));
        }

        Utilities.addComponent(
            this.panel,
            super.getRemoveButton(),
            Utilities.HORIZONTAL,
            0, components.length,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
    }
}
