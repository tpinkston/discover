package discover.gui.widgets;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.enums.ENT_ASSOC_STATUS;
import discover.vdis.enums.GRP_MEM_TYPE;
import discover.vdis.enums.PHYS_ASSOC_TYPE;
import discover.vdis.enums.PHYS_CONN_TYPE;
import discover.vdis.enums.STATION_NAME;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.EntityAssociationVPR;

/**
 * @author Tony Pinkston
 */
public class EntityAssociationRecordWidget extends AbstractVariableRecordWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");

    private EntityIdWidget entity = new EntityIdWidget();
    private JComboBox<String> status = new JComboBox<>();
    private JComboBox<String> type = new JComboBox<>();
    private JComboBox<String> connection = new JComboBox<>();
    private JComboBox<String> station = new JComboBox<>();
    private JComboBox<String> membership = new JComboBox<>();
    private JFormattedTextField change = Utilities.getIntegerField(0);
    private JFormattedTextField group = Utilities.getIntegerField(0);

    public EntityAssociationRecordWidget() {

        super("Entity Association");

        Utilities.configureComboBox(
            type,
            PHYS_ASSOC_TYPE.class,
            false);
        Utilities.configureComboBox(
            status,
            ENT_ASSOC_STATUS.class,
            false);
        Utilities.configureComboBox(
            connection,
            PHYS_CONN_TYPE.class,
            false);
        Utilities.configureComboBox(
            station,
            STATION_NAME.class,
            false);
        Utilities.configureComboBox(
            membership,
            GRP_MEM_TYPE.class,
            false);

        fill();
    }

    @Override
    public JComponent getComponent() {

        return panel;
    }

    @Override
    public AbstractVPRecord getRecord() {

        EntityAssociationVPR record;

        record = new EntityAssociationVPR();

        record.setType(Utilities.getComboboxValue(
            type,
            PHYS_ASSOC_TYPE.class));
        record.setStatus(Utilities.getComboboxValue(
            status,
            ENT_ASSOC_STATUS.class));
        record.setConnection(Utilities.getComboboxValue(
            connection,
            PHYS_CONN_TYPE.class));
        record.setStation(Utilities.getComboboxValue(
            station,
            STATION_NAME.class));
        record.setMembership(Utilities.getComboboxValue(
            membership,
            GRP_MEM_TYPE.class));

        record.setChange(Utilities.getIntegerValue(change));
        record.setGroup(Utilities.getIntegerValue(group));

        entity.getValue(record.getEntityId());

        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {

        if (record instanceof EntityAssociationVPR) {

            EntityAssociationVPR association = (EntityAssociationVPR)record;

            Utilities.setComboBoxValue(
                type,
                PHYS_ASSOC_TYPE.class,
                association.getType());
            Utilities.setComboBoxValue(
                status,
                ENT_ASSOC_STATUS.class,
                association.getStatus());
            Utilities.setComboBoxValue(
                connection,
                PHYS_CONN_TYPE.class,
                association.getConnection());
            Utilities.setComboBoxValue(
                station,
                STATION_NAME.class,
                association.getStation());
            Utilities.setComboBoxValue(
                membership,
                GRP_MEM_TYPE.class,
                association.getMembership());

            change.setValue(association.getChange());
            group.setValue(association.getGroup());
        }
    }

    @Override
    protected void fill() {

        super.fill();

        change.setValue(0);
        change.setColumns(4);
        group.setValue(0);
        group.setColumns(4);

        Utilities.addComponent(
            panel,
            entity.getPanel(),
            Utilities.HORIZONTAL,
            0, 0,
            4, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 4, 2, 2));

        Utilities.addComponent(
            panel,
            new JLabel("Status:"),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            status,
            Utilities.HORIZONTAL,
            1, 1,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            panel,
            new JLabel("Type:"),
            Utilities.HORIZONTAL,
            0, 2,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            type,
            Utilities.HORIZONTAL,
            1, 2,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            panel,
            new JLabel("Connection:"),
            Utilities.HORIZONTAL,
            0, 3,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            connection,
            Utilities.HORIZONTAL,
            1, 3,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            panel,
            new JLabel("Station:"),
            Utilities.HORIZONTAL,
            0, 4,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            station,
            Utilities.HORIZONTAL,
            1, 4,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            panel,
            new JLabel("Membership:"),
            Utilities.HORIZONTAL,
            0, 5,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            membership,
            Utilities.HORIZONTAL,
            1, 5,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            panel,
            new JLabel("Change:"),
            Utilities.HORIZONTAL,
            0, 6,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            change,
            Utilities.HORIZONTAL,
            1, 6,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("Group:"),
            Utilities.HORIZONTAL,
            2, 6,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 10, 2, 2));
        Utilities.addComponent(
            panel,
            group,
            Utilities.HORIZONTAL,
            3, 6,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            panel,
            getRemoveButton(),
            Utilities.HORIZONTAL,
            0, 7,
            4, 1,
            1.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
    }
}
