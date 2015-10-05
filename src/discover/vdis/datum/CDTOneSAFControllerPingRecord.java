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

public class CDTOneSAFControllerPingRecord 
    extends AbstractDatumRecord 
    implements Writable {

    /** DID_CDT_CONTROLLER_PING (not in V-DIS specification) */
    public static final int DATUM_ID = 986881;
    
    /** Length of entire record minus datum ID and length fields. */
    public static final int DATUM_LENGTH = (64 * 8);
    
    public static final int HOSTNAME_LENGTH = 60;

    public final IPAddress address = new IPAddress();
    public String hostname = "null";
    
    public CDTOneSAFControllerPingRecord() {
        
        this(DATUM_ID);
    }
    
    public CDTOneSAFControllerPingRecord(int id) {
        
        super(id);
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("OneSAF Controller Ping Response");
        buffer.addAttribute("Datum Id", super.getDatumId(), VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Length (bytes)", Integer.toString(super.getValueSizeInBytes()));
        buffer.addAttribute("Controller IP", this.address.toString());
        buffer.addAttribute("Host Name", this.hostname);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)
        
        this.address.read(stream);
        
        byte bytes[] = new byte[HOSTNAME_LENGTH];
        
        stream.read(bytes, 0, HOSTNAME_LENGTH);
        
        this.hostname = new String(bytes).trim();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        
        stream.writeInt(DATUM_ID); // 4 bytes
        stream.writeInt(DATUM_LENGTH); // 4 bytes
        
        this.address.write(stream);
        
        if ((this.hostname == null) || this.hostname.isEmpty()) {
            
            for(int i = 0; i < HOSTNAME_LENGTH; ++i) {
                
                stream.writeByte(0);
            }
        }
        else {
            
            char chars[] = this.hostname.toCharArray();
            
            for(int i = 0; i < HOSTNAME_LENGTH; ++i) {
                
                if (i < chars.length) {
                    
                    stream.writeByte(chars[i]);
                }
                else {
                    
                    stream.writeByte(0);
                }
            }
        }
    }
}
