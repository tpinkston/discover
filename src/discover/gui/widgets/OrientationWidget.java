/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import java.text.NumberFormat;

import discover.vdis.common.Orientation;

public class OrientationWidget extends CartesianWidget {

    public OrientationWidget(String title) {

        super(title);
    }

    public OrientationWidget(String title, NumberFormat format) {

        super(title, format);
    }
    
    @Override
    public String[] getLabels() {
        
        return new String[] { "Psi", "Theta", "Phi" };
    }
    
    public void setValue(Orientation orientation) {
        
        super.fields[0].setValue(orientation.getPsi());
        super.fields[1].setValue(orientation.getTheta());
        super.fields[2].setValue(orientation.getPhi());
    }
}
