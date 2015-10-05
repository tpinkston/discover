/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.Readable;
import discover.common.Writable;

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

    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getZ() { return this.z; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setZ(float z) { this.z = z; }

    public float getLength() {

        return (float)Math.sqrt((x * x) + (y * y) + (z * z));
    }

    public void clear() {

        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    @Override
    public String toString() {

        return "(" + formatter.format(this.x) +
               ", " + formatter.format(this.y) +
               ", " + formatter.format(this.z) + ")";
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.x = stream.readFloat();
        this.y = stream.readFloat();
        this.z = stream.readFloat();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeFloat(this.x);
        stream.writeFloat(this.y);
        stream.writeFloat(this.z);
    }
}
