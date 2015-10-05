/**
 * @author Tony Pinkston
 */
package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.enums.VDIS;

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

    public int getType() { return this.type; }
    public int getAlgorithm() { return this.algorithm; }
    public float[] getParameter() { return this.parameter; }
    public float getParameter(int index) { return this.parameter[index]; }

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

        this.type = 0;
        this.algorithm = 0;
        this.parameter[0] = 0.0f;
        this.parameter[1] = 0.0f;
        this.parameter[2] = 0.0f;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VDIS.getDescription(VDIS.VP_RECORD_TYPE, super.type);

        String value = "(" + formatter.format(this.parameter[0]) +
                       ", " + formatter.format(this.parameter[1]) +
                       ", " + formatter.format(this.parameter[2]) + ")";

        buffer.addTitle(title.toUpperCase());

        buffer.addAttribute(
            "Type",
            VDIS.getDescription(VDIS.DR_TYPE, this.type));
        buffer.addAttribute(
            "Algorithm",
            VDIS.getDescription(VDIS.DEAD_RECKONING, this.algorithm));

        buffer.addLabel("Parameter");
        buffer.addItalic(value);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.type = stream.readUnsignedByte();
        this.algorithm = stream.readUnsignedByte();

        stream.readByte(); // 1 byte padding.

        for(int i = 0; i < 3; ++i) {

            this.parameter[i] = stream.readFloat();
        }
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        stream.writeByte(this.type);
        stream.writeByte(this.algorithm);

        stream.writeByte(0x00); // 1 byte padding.

        for(int i = 0; i < 3; ++i) {

            stream.writeFloat(this.parameter[i]);
        }
    }
}
