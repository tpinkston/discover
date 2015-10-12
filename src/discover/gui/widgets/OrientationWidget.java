package discover.gui.widgets;

import java.text.NumberFormat;

import discover.vdis.common.Orientation;

/**
 * @author Tony Pinkston
 */
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

        fields[0].setValue(orientation.getPsi());
        fields[1].setValue(orientation.getTheta());
        fields[2].setValue(orientation.getPhi());
    }
}
