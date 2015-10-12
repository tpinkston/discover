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

/**
 * @author Tony Pinkston
 */
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

        type = new JComboBox<>();
        metric = new JComboBox<>();
        change = Utilities.getIntegerField(0);
        attachment = Utilities.getIntegerField(0);
        value = Utilities.getFloatField(0.0f, null);

        fill();
    }

    @Override
    public JComponent getComponent() {

        return panel;
    }

    @Override
    public AbstractVPRecord getRecord() {

        ArticulatedPartVPR record = new ArticulatedPartVPR();

        record.setType(Utilities.getComboboxValue(
            type,
            VDIS.ARTICULATED_PARTS));
        record.setMetric(Utilities.getComboboxValue(
            metric,
            VDIS.ARTICULATED_PARTS_METRIC));

        record.setChange(Utilities.getIntegerValue(change));
        record.setAttachmentId(Utilities.getIntegerValue(attachment));
        record.setValue(Utilities.getFloatValue(value));

        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {

        if (record instanceof ArticulatedPartVPR) {

            ArticulatedPartVPR part = (ArticulatedPartVPR)record;

            Utilities.setComboBoxValue(
                type,
                VDIS.ARTICULATED_PARTS,
                part.getType());
            Utilities.setComboBoxValue(
                metric,
                VDIS.ARTICULATED_PARTS_METRIC,
                part.getMetric());

            change.setValue(part.getChange());
            attachment.setValue(part.getAttachmentId());
            value.setValue(part.getValue());
        }
    }

    @Override
    protected void fill() {

        super.fill();

        Utilities.configureComboBox(
            type,
            VDIS.ARTICULATED_PARTS,
            false);
        Utilities.configureComboBox(
            metric,
            VDIS.ARTICULATED_PARTS_METRIC,
            false);

        change.setValue(0);
        change.setHorizontalAlignment(JTextField.RIGHT);
        attachment.setValue(0);
        attachment.setHorizontalAlignment(JTextField.RIGHT);
        value.setValue(0.0f);
        value.setHorizontalAlignment(JTextField.RIGHT);

        JComponent components[] = new JComponent[5];

        components[0] = type;
        components[1] = metric;
        components[2] = change;
        components[3] = attachment;
        components[4] = value;

        for(int i = 0; i < components.length; ++i) {

            Utilities.addComponent(
                panel,
                new JLabel(LABELS[i]),
                Utilities.HORIZONTAL,
                0, i,
                1, 1,
                0.0, 0.0,
                Utilities.getInsets(14, 6, 2, 4));
            Utilities.addComponent(
                panel,
                components[i],
                Utilities.HORIZONTAL,
                1, i,
                1, 1,
                0.5, 0.0,
                Utilities.getInsets(8, 2, 2, 2));
        }

        Utilities.addComponent(
            panel,
            getRemoveButton(),
            Utilities.HORIZONTAL,
            0, components.length,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
    }
}
