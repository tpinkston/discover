/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import java.text.NumberFormat;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.common.Location12;
import discover.vdis.common.Location24;
import discover.vdis.common.Orientation;
import discover.vdis.common.Velocity;

public class CartesianWidget extends ToggleWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");
    
    public final JFormattedTextField fields[] = new JFormattedTextField[3];
    
    public CartesianWidget(String title) {
        
        this(title, null);
    }
    
    public CartesianWidget(String title, NumberFormat format) {
        
        super(title);
        
        this.fill(format);
    }

    @Override
    public JComponent getComponent() {

        return this.panel;
    }

    public String[] getLabels() {
        
        return new String[] { "X", "Y", "Z" };
    }
    
    public void clear() {
        
        for(int i = 0; i < this.fields.length; ++i) {
            
            this.fields[i].setValue(0.0f);
        }
    }
    
    public float getValue(int index) {
        
        return ((Float)this.fields[index].getValue()).floatValue();
    }
    
    public void getValue(float values[]) {
        
        values[0] = Utilities.getFloatValue(this.fields[0]);
        values[1] = Utilities.getFloatValue(this.fields[1]);
        values[2] = Utilities.getFloatValue(this.fields[2]);
    }

    public void getValue(Location12 location) {

        location.setX(Utilities.getFloatValue(this.fields[0]));
        location.setY(Utilities.getFloatValue(this.fields[1]));
        location.setZ(Utilities.getFloatValue(this.fields[2]));
    }

    public void getValue(Location24 location) {

        location.setX(Utilities.getFloatValue(this.fields[0]));
        location.setY(Utilities.getFloatValue(this.fields[1]));
        location.setZ(Utilities.getFloatValue(this.fields[2]));
    }

    public void getValue(Velocity velocity) {

        velocity.setX(Utilities.getFloatValue(this.fields[0]));
        velocity.setY(Utilities.getFloatValue(this.fields[1]));
        velocity.setZ(Utilities.getFloatValue(this.fields[2]));
    }

    public void getValue(Orientation orientation) {

        orientation.setPsi(Utilities.getFloatValue(this.fields[0]));
        orientation.setTheta(Utilities.getFloatValue(this.fields[1]));
        orientation.setPhi(Utilities.getFloatValue(this.fields[2]));
    }
    
    public void setValue(float values[]) {
        
        if (values != null) {
            
            this.fields[0].setValue(values[0]);
            this.fields[1].setValue(values[1]);
            this.fields[2].setValue(values[2]);
        }
        else for(int i = 0; i < this.fields.length; ++i) {
            
            this.fields[i].setValue(0.0f);
        }
    }

    public void setValue(Location12 location) {

        if (location != null) {
            
            this.fields[0].setValue(location.getX());
            this.fields[1].setValue(location.getY());
            this.fields[2].setValue(location.getZ());
        }
        else for(int i = 0; i < this.fields.length; ++i) {
            
            this.fields[i].setValue(0.0f);
        }
    }

    public void setValue(Location24 location) {

        if (location != null) {
            
            this.fields[0].setValue((float)location.getX());
            this.fields[1].setValue((float)location.getY());
            this.fields[2].setValue((float)location.getZ());
        }
        else for(int i = 0; i < this.fields.length; ++i) {
            
            this.fields[i].setValue(0.0f);
        }
    }

    public void setValue(Velocity velocity) {

        if (velocity != null) {
            
            this.fields[0].setValue(velocity.getX());
            this.fields[1].setValue(velocity.getY());
            this.fields[2].setValue(velocity.getZ());
        }
        else for(int i = 0; i < this.fields.length; ++i) {
            
            this.fields[i].setValue(0.0f);
        }
    }
    
    protected void fill(NumberFormat format) {
        
        super.fill();
        
        String labels[] = this.getLabels();
        Float value = new Float(0.0f);
        
        for(int i = 0; i < this.fields.length; ++i) {
            
            this.fields[i] = Utilities.getFloatField(value, format);
            this.fields[i].setValue(value);
            this.fields[i].setColumns(8);

            Utilities.addComponent(
                this.panel, 
                new JLabel(labels[i]), 
                Utilities.HORIZONTAL, 
                i, 0, 
                1, 1, 
                0.3, 0.0, 
                Utilities.getInsets(3, 7, 3, 3));
            Utilities.addComponent(
                this.panel, 
                this.fields[i], 
                Utilities.HORIZONTAL, 
                i, 1, 
                1, 1, 
                0.3, 0.0, 
                Utilities.getInsets(3, 3, 3, 3));
        }
        
    }
}
