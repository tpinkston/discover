/**
 * @author Tony Pinkston
 */
package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.Location12;
import discover.vdis.enums.VDIS;

public class EntityOffsetVPR extends AbstractVPRecord {
    
    public static final int LENGTH = 16;

    private static final NumberFormat formatter = NumberFormat.getInstance();
    
    static {
        
        formatter.setMaximumFractionDigits(2);
    }
    
    private int type = 0;
    private final Location12 offset = new Location12();

    public EntityOffsetVPR() {
        
        super(25); // VP_RECORD_TYPE_ENTITY_OFFSET
    }

    @Override
    public int getLength() {
        
        return LENGTH;
    }
    
    public int getType() { return this.type; }
    public Location12 getOffset() { return this.offset; }
    
    public void setType(int type) {
        
        this.type = type;
    }

    @Override
    public String toString() {
        
        return this.offset.toString();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VDIS.getDescription(VDIS.VP_RECORD_TYPE, super.type);
        
        buffer.addTitle(title.toUpperCase());
        buffer.addAttribute(
            "Type", 
            VDIS.getDescription(VDIS.OFFSET_TYPE, this.type));
        buffer.addAttribute("Offset", this.toString());
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.type = stream.readUnsignedByte();
        
        // Padding of 2 bytes
        stream.skipBytes(2);

        this.offset.read(stream);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        stream.writeByte(this.type);
        
        stream.writeByte(0x00); // 1 byte padding.
        stream.writeByte(0x00); // 1 byte padding.

        this.offset.write(stream);
    }
}
