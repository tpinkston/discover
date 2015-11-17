package discover.gui.widgets;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.enums.OFFSET_TYPE;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.EntityAssociationVPR;
import discover.vdis.vprecords.EntityOffsetVPR;

/**
 * @author Tony Pinkston
 */
public class EntityOffsetRecordWidget extends AbstractVariableRecordWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");

    private JComboBox<String> type = new JComboBox<>();
    private CartesianWidget offset = new CartesianWidget("Offset");

    public EntityOffsetRecordWidget() {

        super("Entity Offset");

        Utilities.configureComboBox(type, OFFSET_TYPE.class, false);

        fill();
    }

    @Override
    public AbstractVPRecord getRecord() {

        EntityOffsetVPR record;

        record = new EntityOffsetVPR();

        record.setType(Utilities.getComboboxValue(type, OFFSET_TYPE.class));

        offset.getValue(record.getOffset());

        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {

        if (record instanceof EntityAssociationVPR) {

            EntityOffsetVPR offset = (EntityOffsetVPR)record;

            Utilities.setComboBoxValue(type, OFFSET_TYPE.class, offset.getType());

            this.offset.setValue(offset.getOffset());
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
            new JLabel("Offset Type:"),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            type,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            panel,
            offset.getPanel(),
            Utilities.HORIZONTAL,
            0, 1,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            panel,
            getRemoveButton(),
            Utilities.HORIZONTAL,
            0, 2,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
    }
}
