/**
 * @author Tony Pinkston
 */
package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.IPAddress;
import discover.vdis.enums.VDIS;

public class CDTSpecificConnectionRecord 
    extends AbstractDatumRecord 
    implements Writable {

    /** DID_CDT_SPECIFIC_CONNECTION (not in V-DIS specification) */
    public static final int DATUM_ID = 400200;
    
    /** Length of entire record minus datum ID and length fields. */
    public static final int DATUM_LENGTH = (320 - 64);

    public static int PORTS_LENGTH = 8;
    public static int PADDING_LENGTH = 6;
    
    public final IPAddress senderIPAddress = new IPAddress();
    public final IPAddress receiverIPAddress = new IPAddress();
    public int stsNumber = 0;
    public int repeaterNumber = 0;
    public final int ports[] = new int[PORTS_LENGTH];
    
    public CDTSpecificConnectionRecord() {
        
        this(DATUM_ID);
    }
    
    public CDTSpecificConnectionRecord(int id) {
        
        super(id);
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("Specific Connection");
        buffer.addAttribute("Datum Id", super.getDatumId(), VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Length (bytes)", Integer.toString(super.getValueSizeInBytes()));
        buffer.addAttribute("Sender IP", this.senderIPAddress.toString());
        buffer.addAttribute("Receiver IP", this.receiverIPAddress.toString());
        buffer.addAttribute("STS", this.stsNumber);
        buffer.addAttribute("Repeater", this.repeaterNumber);
        
        for(int i = 0; i < PORTS_LENGTH; ++i) {
            
            buffer.addAttribute(
                ("Port " + Integer.toString(i)),
                Integer.toString(this.ports[i]));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)
        
        this.senderIPAddress.read(stream);
        this.stsNumber = stream.readUnsignedByte();
        this.repeaterNumber = stream.readUnsignedByte();
        this.receiverIPAddress.read(stream);
        
        for(int i = 0; i < PORTS_LENGTH; ++i) {
            
            this.ports[i] = stream.readUnsignedShort();
        }

        stream.skipBytes(PADDING_LENGTH);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        
        stream.writeInt(DATUM_ID); // 4 bytes - (4)
        stream.writeInt(DATUM_LENGTH); // 4 bytes - (8)
        
        this.senderIPAddress.write(stream); // 4 bytes - (12)
        
        stream.writeByte(this.stsNumber); // 1 byte - (13)
        stream.writeByte(this.repeaterNumber); // 1 byte - (14)
        
        this.receiverIPAddress.write(stream); // 4 bytes - (18)
        
        // 8 x 2 bytes = 16 bytes - (34)
        for(int i = 0; i < PORTS_LENGTH; ++i) {
            
            stream.writeShort(this.ports[i]);
        }

        // 6 bytes - (40)
        for(int i = 0; i < PADDING_LENGTH; ++i) {
            
            stream.writeByte(0);
        }
    }
}
