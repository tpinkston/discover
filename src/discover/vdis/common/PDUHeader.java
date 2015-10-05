/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

public class PDUHeader implements Bufferable, Readable, Writable {
    
    public static final int LENGTH = 12;
    
    private int type = 0;
    private int family = 0;
    private int protocol = 0;
    private int exercise = -1;
    private int length = -1;
    private int padding = 0;
    private Timestamp timestamp = new Timestamp();
    private PDUStatus status = new PDUStatus();

    public int getType() { return this.type; }
    public int getFamily() { return this.family; }
    public int getProtocol() { return this.protocol; }
    public int getExercise() { return this.exercise; }
    public int getLength() { return this.length; }
    public int getPadding() { return this.padding; }
    public Timestamp getTimestamp() { return this.timestamp; }
    public PDUStatus getStatus() { return this.status; }

    public void setType(int type) {
    
        this.type = type;
    }
    
    public void setFamily(int family) {
    
        this.family = family;
    }
    
    public void setProtocol(int protocol) {
    
        this.protocol = protocol;
    }
    
    public void setTimestamp(Timestamp timestamp) {
    
        this.timestamp = timestamp.clone();
    }
    
    public void setStatus(PDUStatus status) {
    
        this.status = status;
    }
    
    public void setExercise(int exercise) {
    
        this.exercise = exercise;
    }
    
    public void setLength(int length) {
    
        this.length = length;
    }
    
    public void setPadding(int padding) {
    
        this.padding = padding;
    }

    /**
     * Reads 12 bytes from stream
     */
    @Override
    public void read(DataInputStream stream) throws IOException {

        this.protocol = stream.readUnsignedByte();
        this.exercise = stream.readUnsignedByte();
        this.type = stream.readUnsignedByte();
        this.family = stream.readUnsignedByte();
        this.timestamp.read(stream); // 4 bytes
        this.length = stream.readUnsignedShort();
        this.status.read(stream);
        this.padding = stream.readUnsignedByte();
        this.status.setEnumValues(this.type);
    }
    
    /**
     * Writes 12 bytes to stream
     */
    @Override
    public void write(DataOutputStream stream) throws IOException {
        
        stream.writeByte(this.protocol);
        stream.writeByte(this.exercise);
        stream.writeByte(this.type);
        stream.writeByte(this.family);
        stream.writeInt(this.timestamp.getValue());
        stream.writeShort(this.length);
        stream.writeByte(this.status.getValue()); // status
        stream.writeByte(this.padding);
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {
    
        buffer.addTitle("HEADER");
        buffer.addAttribute("Protocol", this.protocol, VDIS.PROTOCOL_VERSION);
        buffer.addAttribute("Exercise", this.exercise);
        buffer.addAttribute("Type", this.type, VDIS.PDU_TYPE);
        buffer.addAttribute("Family", this.family, VDIS.PDU_FAMILY);
        buffer.addAttribute("Length", this.length);
        buffer.addAttribute("Timestamp", this.timestamp.toString());
        buffer.addBuffer(this.status);
        
        if (this.padding != 0) {
            
            buffer.addAttribute("Padding", this.padding);
        }
        
        buffer.addBreak();
    }
}
