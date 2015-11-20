package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.enums.ARTICULATED_PARTS;
import discover.vdis.enums.ARTICULATED_PARTS_METRIC;
import discover.vdis.enums.VP_RECORD_TYPE;

/**
 * @author Tony Pinkston
 */
public class ArticulatedPartVPR extends AbstractVPRecord {

    public static final int LENGTH = 16;

    private static final int MASK_5_BITS = 0x1F; // (Decimal 31)

    private static final NumberFormat formatter = NumberFormat.getInstance();

    static {

        formatter.setMaximumFractionDigits(3);
    }

    private int type = 0;
    private int metric = 0;
    private int change = 0;
    private int attachment = 0;
    private float value = 0.0f;

    public ArticulatedPartVPR() {

        super(0); // VP_RECORD_TYPE_ARTICULATED_PART
    }

    @Override
    public int getLength() {

        return LENGTH;
    }

    public int getType() { return type; }
    public int getMetric() { return metric; }
    public int getChange() { return change; }
    public int getAttachmentId() { return attachment; }
    public float getValue() { return value; }

    public void setType(int type) {

        this.type = type;
    }

    public void setMetric(int metric) {

        this.metric = metric;
    }

    public void setChange(int changeIndicator) {

        change = changeIndicator;
    }

    public void setAttachmentId(int attachmentId) {

        attachment = attachmentId;
    }

    public void setValue(float value) {

        this.value = value;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VP_RECORD_TYPE.get(getRecordType()).description;
        String typeString = null;

        buffer.addTitle(title.toUpperCase());

        typeString = ARTICULATED_PARTS.get(type).description;
        typeString += " (";
        typeString += ARTICULATED_PARTS_METRIC.get(metric).description;
        typeString += ")";

        buffer.addAttribute("Type", typeString);
        buffer.addLabel("Change");
        buffer.addItalic(Long.toString(change));
        buffer.addLabel(", Attachment");
        buffer.addItalic(Long.toString(attachment));
        buffer.addLabel(", Value");
        buffer.addItalic(formatter.format(value));
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        change = stream.readUnsignedByte();
        attachment = stream.readUnsignedShort();

        int parameter = stream.readInt();

        type = (parameter & ~MASK_5_BITS);
        metric = (parameter & MASK_5_BITS);

        value = stream.readFloat();

        stream.skipBytes(4); // padding
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        stream.writeByte(change);
        stream.writeShort(attachment);
        stream.writeInt(type | metric);
        stream.writeFloat(value);
        stream.writeInt(0x00); // padding
    }
}
