/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.common.DeadReckoning;
import discover.vdis.enums.VDIS;

public class DeadReckoningWidget extends ToggleWidget {
    
    private final JPanel panel = Utilities.getGridBagPanel("");
    
    public final JComboBox<String> algorithm = new JComboBox<>();
    public final CartesianWidget acceleration = new CartesianWidget("Acceleration");
    public final CartesianWidget velocity = new CartesianWidget("Velocity");
    
    public DeadReckoningWidget() {
        
        super("Dead Reckoning");

        this.fill();
    }

    @Override
    public JComponent getComponent() {

        return this.panel;
    }
    
    public void getValue(DeadReckoning record) {
        
        record.setAlgorithm(Utilities.getComboboxValue(
            this.algorithm, 
            VDIS.DEAD_RECKONING));

        this.acceleration.getValue(record.getAcceleration());
        this.velocity.getValue(record.getVelocity());
    }

    public void setValue(DeadReckoning record) {
        
        
        Utilities.setComboBoxValue(
            this.algorithm, 
            VDIS.DEAD_RECKONING,
            record.getAlgorithm());
        
        this.acceleration.setValue(record.getAcceleration());
        this.velocity.setValue(record.getVelocity());
    }

    protected void fill() {
        
        super.fill();
        
        Utilities.configureComboBox(
            this.algorithm, 
            VDIS.DEAD_RECKONING,
            false);
        Utilities.setComboBoxValue(
            this.algorithm, 
            VDIS.DEAD_RECKONING, 
            1);
        
        Utilities.addComponent(
            this.panel, 
            new JLabel("Algorithm:"), 
            Utilities.HORIZONTAL, 
            0, 0, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(8, 6, 2, 2));
        Utilities.addComponent(
            this.panel, 
            this.algorithm, 
            Utilities.HORIZONTAL, 
            1, 0, 
            1, 1, 
            0.5, 0.0, 
            Utilities.getInsets(4, 2, 2, 4));
        Utilities.addComponent(
            this.panel, 
            this.acceleration.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 1, 
            2, 1, 
            0.5, 0.0, 
            Utilities.getInsets(4, 6, 2, 4));
        Utilities.addComponent(
            this.panel, 
            this.velocity.getPanel(), 
            Utilities.HORIZONTAL, 
            0, 2, 
            2, 1, 
            0.5, 0.0, 
            Utilities.getInsets(2, 6, 4, 4));
    }
}
