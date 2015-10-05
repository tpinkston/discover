/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;

import discover.common.Readable;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

public class DeadReckoning implements Bufferable, Readable, Writable {

    public static final int LENGTH = 40;

    private static final int PARAMETER_BYTES = 15;

    private static final NumberFormat formatter = NumberFormat.getInstance();

    static {

        formatter.setMinimumFractionDigits(1);
        formatter.setMaximumFractionDigits(5);
    }

    private int algorithm = 1;
    private final byte[] parameters = new byte[PARAMETER_BYTES];
    private final float[] acceleration = new float[] { 0.0f, 0.0f, 0.0f };
    private final float[] velocity = new float[] { 0.0f, 0.0f, 0.0f };

    public DeadReckoning() {

        Arrays.fill(this.parameters, (byte)0x00);
    }

    public int getAlgorithm() { return this.algorithm; }
    public byte[] getParameters() { return this.parameters; }
    public float[] getAcceleration() { return this.acceleration; }
    public float[] getVelocity() { return this.velocity; }

    public void setAlgorithm(int algorithm) {

        this.algorithm = algorithm;
    }

    public void setParameters(byte array[]) {

        for(int i = 0; i < PARAMETER_BYTES; ++i) {

            this.parameters[0] = array[0];
        }
    }

    public void setAcceleration(float array[]) {

        this.acceleration[0] = array[0];
        this.acceleration[1] = array[1];
        this.acceleration[2] = array[2];
    }

    public void setVelocity(float array[]) {

        this.velocity[0] = array[0];
        this.velocity[1] = array[1];
        this.velocity[2] = array[2];
    }

    public void clear() {

        this.algorithm = 1;

        Arrays.fill(this.parameters, (byte)0x00);
        Arrays.fill(this.acceleration, 0.0f);
        Arrays.fill(this.velocity, 0.0f);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute(
            "Algorithm",
            this.algorithm,
            VDIS.DEAD_RECKONING);
        buffer.addAttribute("Linear Acceleration", toString(this.acceleration));
        buffer.addAttribute("Angular Velocity", toString(this.velocity));
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.algorithm = stream.readUnsignedByte();

        stream.read(this.parameters, 0, PARAMETER_BYTES);

        // Linear Acceleration
        this.acceleration[0] = stream.readFloat();
        this.acceleration[1] = stream.readFloat();
        this.acceleration[2] = stream.readFloat();

        // Angular Velocity
        this.velocity[0] = stream.readFloat();
        this.velocity[1] = stream.readFloat();
        this.velocity[2] = stream.readFloat();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeByte(this.algorithm);

        stream.write(this.parameters, 0, PARAMETER_BYTES);

        for(int i = 0; i < 3; ++i) {

            stream.writeFloat(this.acceleration[i]);
        }

        for(int i = 0; i < 3; ++i) {

            stream.writeFloat(this.velocity[i]);
        }
    }

    private static final String toString(float array[]) {

        return "(" + formatter.format(array[0]) +
               ", " + formatter.format(array[1]) +
               ", " + formatter.format(array[2]) + ")";
    }
}
