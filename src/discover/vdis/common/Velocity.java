package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.Readable;
import discover.common.Writable;

/**
 * @author Tony Pinkston
 */
public class Velocity implements Readable, Writable {

    public static final int LENGTH = 12;

    private static final NumberFormat formatter = NumberFormat.getInstance();

    static {

        formatter.setMinimumFractionDigits(1);
        formatter.setMaximumFractionDigits(2);
    }

    private float x = 0.0f;
    private float y = 0.0f;
    private float z = 0.0f;

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setZ(float z) { this.z = z; }

    public float getLength() {

        return (float)Math.sqrt((x * x) + (y * y) + (z * z));
    }

    public void clear() {

        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    @Override
    public String toString() {

        return "(" + formatter.format(x) +
               ", " + formatter.format(y) +
               ", " + formatter.format(z) + ")";
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        x = stream.readFloat();
        y = stream.readFloat();
        z = stream.readFloat();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeFloat(x);
        stream.writeFloat(y);
        stream.writeFloat(z);
    }
}
