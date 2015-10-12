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

/**
 * @author Tony Pinkston
 */
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

        Arrays.fill(parameters, (byte)0x00);
    }

    public int getAlgorithm() { return algorithm; }
    public byte[] getParameters() { return parameters; }
    public float[] getAcceleration() { return acceleration; }
    public float[] getVelocity() { return velocity; }

    public void setAlgorithm(int algorithm) {

        this.algorithm = algorithm;
    }

    public void setParameters(byte array[]) {

        for(int i = 0; i < PARAMETER_BYTES; ++i) {

            parameters[0] = array[0];
        }
    }

    public void setAcceleration(float array[]) {

        acceleration[0] = array[0];
        acceleration[1] = array[1];
        acceleration[2] = array[2];
    }

    public void setVelocity(float array[]) {

        velocity[0] = array[0];
        velocity[1] = array[1];
        velocity[2] = array[2];
    }

    public void clear() {

        algorithm = 1;

        Arrays.fill(parameters, (byte)0x00);
        Arrays.fill(acceleration, 0.0f);
        Arrays.fill(velocity, 0.0f);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute(
            "Algorithm",
            algorithm,
            VDIS.DEAD_RECKONING);
        buffer.addAttribute("Linear Acceleration", toString(acceleration));
        buffer.addAttribute("Angular Velocity", toString(velocity));
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        algorithm = stream.readUnsignedByte();

        stream.read(parameters, 0, PARAMETER_BYTES);

        // Linear Acceleration
        acceleration[0] = stream.readFloat();
        acceleration[1] = stream.readFloat();
        acceleration[2] = stream.readFloat();

        // Angular Velocity
        velocity[0] = stream.readFloat();
        velocity[1] = stream.readFloat();
        velocity[2] = stream.readFloat();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeByte(algorithm);

        stream.write(parameters, 0, PARAMETER_BYTES);

        for(int i = 0; i < 3; ++i) {

            stream.writeFloat(acceleration[i]);
        }

        for(int i = 0; i < 3; ++i) {

            stream.writeFloat(velocity[i]);
        }
    }

    private static final String toString(float array[]) {

        return "(" + formatter.format(array[0]) +
               ", " + formatter.format(array[1]) +
               ", " + formatter.format(array[2]) + ")";
    }
}
