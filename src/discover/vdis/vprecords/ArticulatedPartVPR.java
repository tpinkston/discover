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

    public int getLength() {
        
        return LENGTH;
    }
    
    public int getType() { return this.type; }
    public int getMetric() { return this.metric; }
    public int getChange() { return this.change; }
    public int getAttachmentId() { return this.attachment; }
    public float getValue() { return this.value; }

    public void setType(int type) {
    
        this.type = type;
    }

    public void setMetric(int metric) {
    
        this.metric = metric;
    }

    public void setChange(int changeIndicator) {
    
        this.change = changeIndicator;
    }

    public void setAttachmentId(int attachmentId) {
    
        this.attachment = attachmentId;
    }

    public void setValue(float value) {
    
        this.value = value;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VDIS.getDescription(VDIS.VP_RECORD_TYPE, super.type);
        String type = null;
        
        buffer.addTitle(title.toUpperCase());
        
        type = VDIS.getDescription(VDIS.ARTICULATED_PARTS, this.type);
        type += " (";
        type += VDIS.getDescription(VDIS.ARTICULATED_PARTS_METRIC, this.metric);
        type += ")";

        buffer.addAttribute("Type", type);
        buffer.addLabel("Change");
        buffer.addItalic(Long.toString(this.change));
        buffer.addLabel(", Attachment");
        buffer.addItalic(Long.toString(this.attachment));
        buffer.addLabel(", Value");
        buffer.addItalic(formatter.format(this.value));
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.change = stream.readUnsignedByte();
        this.attachment = stream.readUnsignedShort();

        int parameter = stream.readInt();
        
        this.type = (parameter & ~MASK_5_BITS);
        this.metric = (parameter & MASK_5_BITS);

        this.value = stream.readFloat();

        stream.skipBytes(4); // padding
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        stream.writeByte(this.change);
        stream.writeShort(this.attachment);
        stream.writeInt(this.type | this.metric);
        stream.writeFloat(this.value);
        stream.writeInt(0x00); // padding
    }
}
