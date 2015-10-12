package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.Readable;
import discover.common.Writable;
import geotransform.coords.Gcc_Coord_3d;
import geotransform.coords.Gdc_Coord_3d;
import geotransform.transforms.Gcc_To_Gdc_Converter;
import geotransform.transforms.Gdc_To_Gcc_Converter;

/**
 * Record of size 192 bits (24 bytes)
 *
 * @author Tony Pinkston
 */
public class Location24 implements Readable, Writable {

    public static final int LENGTH = 24;

    private static final Gcc_Coord_3d GCC = new Gcc_Coord_3d();
    private static final Gdc_Coord_3d GDC = new Gdc_Coord_3d();

    private static final NumberFormat formatterGCC = NumberFormat.getInstance();
    private static final NumberFormat formatterGDC = NumberFormat.getInstance();

    static {

        formatterGCC.setMinimumFractionDigits(1);
        formatterGCC.setMaximumFractionDigits(2);

        formatterGDC.setMinimumFractionDigits(1);
        formatterGDC.setMaximumFractionDigits(6);
    }

    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setZ(double z) { this.z = z; }

    public void set(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setGDC(double latitude, double longitude, double elevation) {

        GDC.latitude = latitude;
        GDC.longitude = longitude;
        GDC.elevation = elevation;

        Gdc_To_Gcc_Converter.Convert(GDC, GCC);

        x = GCC.x;
        y = GCC.y;
        z = GCC.z;
    }

    public void clear() {

        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    /**
     * Returns distance from center of Earth (origin), assuming this
     * location is geocentric.
     */
    public double getLength() {

        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    @Override
    public String toString() {

        return toStringGCC();
    }

    /**
     * @return Location in GCC format: (x, y, z)
     */
    public String toStringGCC() {

        return "(" + formatterGCC.format(x) +
               ", " + formatterGCC.format(y) +
               ", " + formatterGCC.format(z) + ")";
    }

    /**
     * @return Location in GDC format: (latitude, longitude, elevation)
     */
    public String toStringGDC() {

        StringBuffer buffer = new StringBuffer();

        GCC.x = getX();
        GCC.y = getY();
        GCC.z = getZ();

        Gcc_To_Gdc_Converter.Convert(GCC, GDC);

        buffer.append("(");
        buffer.append(formatterGDC.format(GDC.latitude));
        buffer.append(" lat, ");
        buffer.append(formatterGDC.format(GDC.longitude));
        buffer.append(" long, ");
        buffer.append(formatterGDC.format(GDC.elevation));
        buffer.append(" meters)");

        return buffer.toString();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        x = stream.readDouble();
        y = stream.readDouble();
        z = stream.readDouble();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeDouble(x);
        stream.writeDouble(y);
        stream.writeDouble(z);
    }
}
