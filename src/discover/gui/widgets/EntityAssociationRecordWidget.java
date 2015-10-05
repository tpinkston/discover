/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.enums.VDIS;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.EntityAssociationVPR;

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
            this.type,
            VDIS.PHYS_ASSOC_TYPE,
            false);
        Utilities.configureComboBox(
            this.status,
            VDIS.ENT_ASSOC_STATUS,
            false);
        Utilities.configureComboBox(
            this.connection,
            VDIS.PHYS_CONN_TYPE,
            false);
        Utilities.configureComboBox(
            this.station,
            VDIS.STATION_NAME,
            false);
        Utilities.configureComboBox(
            this.membership,
            VDIS.GRP_MEM_TYPE,
            false);

        this.fill();
    }

    @Override
    public JComponent getComponent() {

        return this.panel;
    }

    @Override
    public AbstractVPRecord getRecord() {

        EntityAssociationVPR record;

        record = new EntityAssociationVPR();

        record.setType(Utilities.getComboboxValue(
            this.type,
            VDIS.PHYS_ASSOC_TYPE));
        record.setStatus(Utilities.getComboboxValue(
            this.status,
            VDIS.ENT_ASSOC_STATUS));
        record.setConnection(Utilities.getComboboxValue(
            this.connection,
            VDIS.PHYS_CONN_TYPE));
        record.setStation(Utilities.getComboboxValue(
            this.station,
            VDIS.STATION_NAME));
        record.setMembership(Utilities.getComboboxValue(
            this.membership,
            VDIS.GRP_MEM_TYPE));

        record.setChange(Utilities.getIntegerValue(this.change));
        record.setGroup(Utilities.getIntegerValue(this.group));

        this.entity.getValue(record.getEntityId());

        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {

        if (record instanceof EntityAssociationVPR) {

            EntityAssociationVPR association = (EntityAssociationVPR)record;

            Utilities.setComboBoxValue(
                this.type,
                VDIS.PHYS_ASSOC_TYPE,
                association.getType());
            Utilities.setComboBoxValue(
                this.status,
                VDIS.ENT_ASSOC_STATUS,
                association.getStatus());
            Utilities.setComboBoxValue(
                this.connection,
                VDIS.PHYS_CONN_TYPE,
                association.getConnection());
            Utilities.setComboBoxValue(
                this.station,
                VDIS.STATION_NAME,
                association.getStation());
            Utilities.setComboBoxValue(
                this.membership,
                VDIS.GRP_MEM_TYPE,
                association.getMembership());

            this.change.setValue(association.getChange());
            this.group.setValue(association.getGroup());
        }
    }

    protected void fill() {

        super.fill();

        this.change.setValue(0);
        this.change.setColumns(4);
        this.group.setValue(0);
        this.group.setColumns(4);

        Utilities.addComponent(
            this.panel,
            this.entity.getPanel(),
            Utilities.HORIZONTAL,
            0, 0,
            4, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 4, 2, 2));

        Utilities.addComponent(
            this.panel,
            new JLabel("Status:"),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.status,
            Utilities.HORIZONTAL,
            1, 1,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            this.panel,
            new JLabel("Type:"),
            Utilities.HORIZONTAL,
            0, 2,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.type,
            Utilities.HORIZONTAL,
            1, 2,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            this.panel,
            new JLabel("Connection:"),
            Utilities.HORIZONTAL,
            0, 3,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.connection,
            Utilities.HORIZONTAL,
            1, 3,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            this.panel,
            new JLabel("Station:"),
            Utilities.HORIZONTAL,
            0, 4,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.station,
            Utilities.HORIZONTAL,
            1, 4,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            this.panel,
            new JLabel("Membership:"),
            Utilities.HORIZONTAL,
            0, 5,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.membership,
            Utilities.HORIZONTAL,
            1, 5,
            3, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            this.panel,
            new JLabel("Change:"),
            Utilities.HORIZONTAL,
            0, 6,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.change,
            Utilities.HORIZONTAL,
            1, 6,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel,
            new JLabel("Group:"),
            Utilities.HORIZONTAL,
            2, 6,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 10, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.group,
            Utilities.HORIZONTAL,
            3, 6,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            this.panel,
            super.getRemoveButton(),
            Utilities.HORIZONTAL,
            0, 7,
            4, 1,
            1.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
    }
}
