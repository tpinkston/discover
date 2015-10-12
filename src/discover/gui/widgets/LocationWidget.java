package discover.gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import discover.gui.Utilities;
import discover.vdis.common.Location24;
import geotransform.coords.Gcc_Coord_3d;
import geotransform.coords.Gdc_Coord_3d;
import geotransform.transforms.Gcc_To_Gdc_Converter;
import geotransform.transforms.Gdc_To_Gcc_Converter;

/**
 * @author Tony Pinkston
 */
public class LocationWidget extends ToggleWidget {

    private static final String GCC = "Geocentric";
    private static final String GDC = "Geodetic";
    private static final NumberFormat gccFormat = NumberFormat.getInstance();
    private static final NumberFormat gdcFormat = NumberFormat.getInstance();

    /** Location in Afghanistan */
    private static final float GDC_VALUE[] = {
        34.5148255f,
        70.0144137f,
        716.7790752f
    };

    static {

        gccFormat.setMinimumFractionDigits(1);
        gccFormat.setMaximumFractionDigits(3);

        gdcFormat.setMinimumFractionDigits(1);
        gdcFormat.setMaximumFractionDigits(7);
    }

    private final JPanel panel;

    public final CartesianWidget gcc = new CartesianWidget(GCC, gccFormat);

    public final CartesianWidget gdc = new CartesianWidget(GDC, gdcFormat) {

        @Override
        public String[] getLabels() {

            return new String[] { "Latitude", "Longitude", "Elevation" };
        }
    };

    private boolean updating = false;

    public LocationWidget(String title) {

        super(title);

        panel = Utilities.getGridBagPanel(null);

        for(int i = 0; i < 3; ++i) {

            gcc.fields[i].setName(GCC);
            gcc.fields[i].addActionListener(this);
            gcc.fields[i].addFocusListener(this);
            gdc.fields[i].setName(GDC);
            gdc.fields[i].setValue(GDC_VALUE[i]);
            gdc.fields[i].addActionListener(this);
            gdc.fields[i].addFocusListener(this);
        }

        this.update(GDC);
        fill();
    }

    @Override
    public JComponent getComponent() {

        return panel;
    }

    public void getValue(Location24 location) {

        gcc.getValue(location);
    }

    public void setValue(Location24 location) {

        gcc.setValue(location);
        updateGeodetic();
    }

    @Override
    public void focusGained(FocusEvent event) {

        if (!updating && (event.getSource() instanceof JTextField)) {

            this.update(((JTextField)event.getSource()).getName());
        }
    }

    @Override
    public void focusLost(FocusEvent event) {

        if (!updating && (event.getSource() instanceof JTextField)) {

            this.update(((JTextField)event.getSource()).getName());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (!updating && (event.getSource() instanceof JTextField)) {

            this.update(((JTextField)event.getSource()).getName());
        }
    }

    @Override
    protected void fill() {

        super.fill();

        Utilities.addComponent(
            panel,
            gcc.getPanel(),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(4, 6, 2, 0));
        Utilities.addComponent(
            panel,
            gdc.getPanel(),
            Utilities.HORIZONTAL,
            0, 1,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(2, 6, 4, 0));
    }

    private void update(String name) {

        if (name.equals(GCC)) {

            updateGeodetic();
        }
        else if (name.equals(GDC)) {

            updateGeocentric();
        }
    }

    private void updateGeocentric() {

        Gcc_Coord_3d GCC = new Gcc_Coord_3d();
        Gdc_Coord_3d GDC = new Gdc_Coord_3d();

        GDC.latitude = ((Float)gdc.fields[0].getValue()).doubleValue();
        GDC.longitude = ((Float)gdc.fields[1].getValue()).doubleValue();
        GDC.elevation = ((Float)gdc.fields[2].getValue()).doubleValue();

        Gdc_To_Gcc_Converter.Convert(GDC, GCC);

        updating = true;
        gcc.fields[0].setValue((float)GCC.x);
        gcc.fields[1].setValue((float)GCC.y);
        gcc.fields[2].setValue((float)GCC.z);
        updating = false;
    }

    private void updateGeodetic() {

        Gcc_Coord_3d GCC = new Gcc_Coord_3d();
        Gdc_Coord_3d GDC = new Gdc_Coord_3d();

        GCC.x = ((Float)gcc.fields[0].getValue()).doubleValue();
        GCC.y = ((Float)gcc.fields[1].getValue()).doubleValue();
        GCC.z = ((Float)gcc.fields[2].getValue()).doubleValue();

        Gcc_To_Gdc_Converter.Convert(GCC, GDC);

        updating = true;
        gdc.fields[0].setValue((float)GDC.latitude);
        gdc.fields[1].setValue((float)GDC.longitude);
        gdc.fields[2].setValue((float)GDC.elevation);
        updating = false;
    }
}
