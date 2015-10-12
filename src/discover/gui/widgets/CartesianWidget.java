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

/**
 * @author Tony Pinkston
 */
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

        return panel;
    }

    public String[] getLabels() {

        return new String[] { "X", "Y", "Z" };
    }

    public void clear() {

        for(int i = 0; i < fields.length; ++i) {

            fields[i].setValue(0.0f);
        }
    }

    public float getValue(int index) {

        return ((Float)fields[index].getValue()).floatValue();
    }

    public void getValue(float values[]) {

        values[0] = Utilities.getFloatValue(fields[0]);
        values[1] = Utilities.getFloatValue(fields[1]);
        values[2] = Utilities.getFloatValue(fields[2]);
    }

    public void getValue(Location12 location) {

        location.setX(Utilities.getFloatValue(fields[0]));
        location.setY(Utilities.getFloatValue(fields[1]));
        location.setZ(Utilities.getFloatValue(fields[2]));
    }

    public void getValue(Location24 location) {

        location.setX(Utilities.getFloatValue(fields[0]));
        location.setY(Utilities.getFloatValue(fields[1]));
        location.setZ(Utilities.getFloatValue(fields[2]));
    }

    public void getValue(Velocity velocity) {

        velocity.setX(Utilities.getFloatValue(fields[0]));
        velocity.setY(Utilities.getFloatValue(fields[1]));
        velocity.setZ(Utilities.getFloatValue(fields[2]));
    }

    public void getValue(Orientation orientation) {

        orientation.setPsi(Utilities.getFloatValue(fields[0]));
        orientation.setTheta(Utilities.getFloatValue(fields[1]));
        orientation.setPhi(Utilities.getFloatValue(fields[2]));
    }

    public void setValue(float values[]) {

        if (values != null) {

            fields[0].setValue(values[0]);
            fields[1].setValue(values[1]);
            fields[2].setValue(values[2]);
        }
        else for(int i = 0; i < fields.length; ++i) {

            fields[i].setValue(0.0f);
        }
    }

    public void setValue(Location12 location) {

        if (location != null) {

            fields[0].setValue(location.getX());
            fields[1].setValue(location.getY());
            fields[2].setValue(location.getZ());
        }
        else for(int i = 0; i < fields.length; ++i) {

            fields[i].setValue(0.0f);
        }
    }

    public void setValue(Location24 location) {

        if (location != null) {

            fields[0].setValue((float)location.getX());
            fields[1].setValue((float)location.getY());
            fields[2].setValue((float)location.getZ());
        }
        else for(int i = 0; i < fields.length; ++i) {

            fields[i].setValue(0.0f);
        }
    }

    public void setValue(Velocity velocity) {

        if (velocity != null) {

            fields[0].setValue(velocity.getX());
            fields[1].setValue(velocity.getY());
            fields[2].setValue(velocity.getZ());
        }
        else for(int i = 0; i < fields.length; ++i) {

            fields[i].setValue(0.0f);
        }
    }

    protected void fill(NumberFormat format) {

        super.fill();

        String labels[] = getLabels();
        Float value = new Float(0.0f);

        for(int i = 0; i < fields.length; ++i) {

            fields[i] = Utilities.getFloatField(value, format);
            fields[i].setValue(value);
            fields[i].setColumns(8);

            Utilities.addComponent(
                panel,
                new JLabel(labels[i]),
                Utilities.HORIZONTAL,
                i, 0,
                1, 1,
                0.3, 0.0,
                Utilities.getInsets(3, 7, 3, 3));
            Utilities.addComponent(
                panel,
                fields[i],
                Utilities.HORIZONTAL,
                i, 1,
                1, 1,
                0.3, 0.0,
                Utilities.getInsets(3, 3, 3, 3));
        }

    }
}
