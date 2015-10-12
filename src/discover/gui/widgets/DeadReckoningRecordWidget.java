package discover.gui.widgets;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.enums.VDIS;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.DeadReckoningVPR;

/**
 * @author Tony Pinkston
 */
public class DeadReckoningRecordWidget extends AbstractVariableRecordWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");

    private JComboBox<String> type = new JComboBox<>();
    private JComboBox<String> algorithm = new JComboBox<>();
    private CartesianWidget parameter = new CartesianWidget("Parameter");

    public DeadReckoningRecordWidget() {

        super("Dead Reckoning");

        Utilities.configureComboBox(
            type,
            VDIS.DR_TYPE,
            false);
        Utilities.configureComboBox(
            algorithm,
            VDIS.DEAD_RECKONING,
            false);

        fill();
    }

    @Override
    public AbstractVPRecord getRecord() {

        DeadReckoningVPR record;

        record = new DeadReckoningVPR();

        record.setType(Utilities.getComboboxValue(
            type,
            VDIS.DR_TYPE));
        record.setAlgorithm(Utilities.getComboboxValue(
            algorithm,
            VDIS.DEAD_RECKONING));

        parameter.getValue(record.getParameter());

        return record;
    }

    @Override
    public void setRecord(AbstractVPRecord record) {

        if (record instanceof DeadReckoningVPR) {

            DeadReckoningVPR dead = (DeadReckoningVPR)record;

            Utilities.setComboBoxValue(
                type,
                VDIS.DR_TYPE,
                dead.getType());
            Utilities.setComboBoxValue(
                algorithm,
                VDIS.DEAD_RECKONING,
                dead.getAlgorithm());

            parameter.setValue(dead.getParameter());
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
            new JLabel("Type:"),
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
            new JLabel("Algorithm:"),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(10, 6, 2, 2));
        Utilities.addComponent(
            panel,
            algorithm,
            Utilities.HORIZONTAL,
            1, 1,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));

        Utilities.addComponent(
            panel,
            parameter.getPanel(),
            Utilities.HORIZONTAL,
            0, 2,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));

        Utilities.addComponent(
            panel,
            getRemoveButton(),
            Utilities.HORIZONTAL,
            0, 3,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
    }
}
