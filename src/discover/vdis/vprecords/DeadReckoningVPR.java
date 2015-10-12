package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class DeadReckoningVPR extends AbstractVPRecord {

    public static final int LENGTH = 16;

    private static final NumberFormat formatter = NumberFormat.getInstance();

    static {

        formatter.setMaximumFractionDigits(2);
    }

    private int type = 0;
    private int algorithm = 0;
    private float parameter[] = new float[] { 0.0f, 0.0f, 0.0f };

    public DeadReckoningVPR() {

        super(26); // VP_RECORD_TYPE_DEAD_RECKONING
    }

    @Override
    public int getLength() {

        return LENGTH;
    }

    public int getType() { return type; }
    public int getAlgorithm() { return algorithm; }
    public float[] getParameter() { return parameter; }
    public float getParameter(int index) { return parameter[index]; }

    public void setType(int type) {

        this.type = type;
    }

    public void setAlgorithm(int algorithm) {

        this.algorithm = algorithm;
    }

    public void setParameter(float parameter[]) {

        this.parameter[0] = parameter[0];
        this.parameter[1] = parameter[1];
        this.parameter[2] = parameter[2];
    }

    public void setParameter(float parameter, int index) {

        this.parameter[index] = parameter;
    }

    public void clear() {

        type = 0;
        algorithm = 0;
        parameter[0] = 0.0f;
        parameter[1] = 0.0f;
        parameter[2] = 0.0f;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VDIS.getDescription(VDIS.VP_RECORD_TYPE, getRecordType());

        String value = "(" + formatter.format(parameter[0]) +
                       ", " + formatter.format(parameter[1]) +
                       ", " + formatter.format(parameter[2]) + ")";

        buffer.addTitle(title.toUpperCase());

        buffer.addAttribute(
            "Type",
            VDIS.getDescription(VDIS.DR_TYPE, type));
        buffer.addAttribute(
            "Algorithm",
            VDIS.getDescription(VDIS.DEAD_RECKONING, algorithm));

        buffer.addLabel("Parameter");
        buffer.addItalic(value);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        type = stream.readUnsignedByte();
        algorithm = stream.readUnsignedByte();

        stream.readByte(); // 1 byte padding.

        for(int i = 0; i < 3; ++i) {

            parameter[i] = stream.readFloat();
        }
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        stream.writeByte(type);
        stream.writeByte(algorithm);

        stream.writeByte(0x00); // 1 byte padding.

        for(int i = 0; i < 3; ++i) {

            stream.writeFloat(parameter[i]);
        }
    }
}
