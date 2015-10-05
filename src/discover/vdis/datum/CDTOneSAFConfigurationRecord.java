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

public class CDTOneSAFConfigurationRecord 
    extends AbstractDatumRecord 
    implements Writable {

    /** DID_CDT_ONESAF_CONFIGURATION (not in V-DIS specification) */
    public static final int DATUM_ID = 400400;
    
    /** Length of entire record minus datum ID and length fields. */
    public static final int DATUM_LENGTH = (512 - 64);

    /** Length of terrain string in bytes. */ 
    public static int TERRAIN_LENGTH = 48;
    
    public final IPAddress stsIPAddress = new IPAddress();
    public int port = 0;
    public String terrain = null;
    
    public CDTOneSAFConfigurationRecord() {
        
        this(DATUM_ID);
    }
    
    public CDTOneSAFConfigurationRecord(int id) {
        
        super(id);
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("OneSAF Configuration");
        buffer.addAttribute("Datum Id", super.getDatumId(), VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Length (bytes)", super.getValueSizeInBytes());
        buffer.addAttribute("Terrain Name", this.terrain);
        buffer.addAttribute("VDIS Port", Integer.toString(this.port));
        buffer.addAttribute("STS IP", this.stsIPAddress.toString());
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)
        
        this.port = stream.readInt();
        this.stsIPAddress.read(stream);

        byte bytes[] = new byte[TERRAIN_LENGTH];
        
        stream.read(bytes, 0, TERRAIN_LENGTH);

        this.terrain = new String(bytes).trim();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        
        stream.writeInt(DATUM_ID); // 4 bytes
        stream.writeInt(DATUM_LENGTH); // 4 bytes
        stream.writeInt(this.port);
        this.stsIPAddress.write(stream);

        if ((this.terrain == null) || this.terrain.isEmpty()) {
            
            stream.writeByte('n');
            stream.writeByte('u');
            stream.writeByte('l');
            stream.writeByte('l');
            
            for(int i = 4; i < TERRAIN_LENGTH; ++i) {
                
                stream.writeByte(0);
            }
        }
        else {
            
            char chars[] = this.terrain.toCharArray();
            
            for(int i = 0; i < TERRAIN_LENGTH; ++i) {
                
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
