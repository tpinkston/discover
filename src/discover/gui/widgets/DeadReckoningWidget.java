package discover.gui.widgets;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.common.DeadReckoning;
import discover.vdis.enums.DEAD_RECKONING;

/**
 * @author Tony Pinkston
 */
public class DeadReckoningWidget extends ToggleWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");

    public final JComboBox<String> algorithm = new JComboBox<>();
    public final CartesianWidget acceleration = new CartesianWidget("Acceleration");
    public final CartesianWidget velocity = new CartesianWidget("Velocity");

    public DeadReckoningWidget() {

        super("Dead Reckoning");

        fill();
    }

    @Override
    public JComponent getComponent() {

        return panel;
    }

    public void getValue(DeadReckoning record) {

        record.setAlgorithm(Utilities.getComboboxValue(
            algorithm,
            DEAD_RECKONING.class));

        acceleration.getValue(record.getAcceleration());
        velocity.getValue(record.getVelocity());
    }

    public void setValue(DeadReckoning record) {


        Utilities.setComboBoxValue(
            algorithm,
            DEAD_RECKONING.class,
            record.getAlgorithm());

        acceleration.setValue(record.getAcceleration());
        velocity.setValue(record.getVelocity());
    }

    @Override
    protected void fill() {

        super.fill();

        Utilities.configureComboBox(algorithm, DEAD_RECKONING.class, false);
        Utilities.setComboBoxValue(algorithm, DEAD_RECKONING.class, 1);

        Utilities.addComponent(
            panel,
            new JLabel("Algorithm:"),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 6, 2, 2));
        Utilities.addComponent(
            panel,
            algorithm,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 2, 2, 4));
        Utilities.addComponent(
            panel,
            acceleration.getPanel(),
            Utilities.HORIZONTAL,
            0, 1,
            2, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 6, 2, 4));
        Utilities.addComponent(
            panel,
            velocity.getPanel(),
            Utilities.HORIZONTAL,
            0, 2,
            2, 1,
            0.5, 0.0,
            Utilities.getInsets(2, 6, 4, 4));
    }
}
