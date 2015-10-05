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

public class CDTOneSAFControllerNodeRecord 
    extends AbstractDatumRecord 
    implements Writable {

    /** DID_CDT_ONESAF_NODE (not in V-DIS specification) */
    public static final int DATUM_ID = 986883;
    
    /** Length of entire record minus datum ID and length fields. */
    public static final int DATUM_LENGTH = (152 * 8);
    
    public static final int TERRAIN_LENGTH = 64;

    public final IPAddress ios = new IPAddress();
    public String vctsTerrain = "null";
    public String onesafTerrain = "null";
    public int number = 0; // 16-bit unsigned integer
    public int application = 0; // 16-bit unsigned integer
    public int status = 0; // // 16-bit enumeration (CDT_ONESAF_STATUS)
    public int sts = 0; // 16-bit unsigned integer
    public int requestID = 0; // 32-bit unsigned integer
    public int port = 0; // 32-bit unsigned integer
    public int exercise = 0; // 32-bit unsigned integer
    
    public CDTOneSAFControllerNodeRecord() {
        
        this(DATUM_ID);
    }
    
    public CDTOneSAFControllerNodeRecord(int id) {
        
        super(id);
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("OneSAF Controller Node");
        buffer.addAttribute("Datum Id", super.getDatumId(), VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Length (bytes)", super.getValueSizeInBytes());
        buffer.addAttribute("Node Number", this.number);
        buffer.addAttribute("Status", this.status, VDIS.CDT_ONESAF_STATUS);
        buffer.addAttribute("Application ID", this.application);
        buffer.addAttribute("STS Number", this.sts);
        buffer.addAttribute("Request ID", this.requestID);
        buffer.addAttribute("Port", this.port);
        buffer.addAttribute("Exercise", this.exercise);
        buffer.addAttribute("IOS IP", this.ios.toString());
        buffer.addAttribute("CDT Terrain", new String(this.vctsTerrain));
        buffer.addAttribute("OneSAF Terrain", new String(this.onesafTerrain));
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)
        
        this.ios.read(stream); // 4 (4)
        this.number = stream.readUnsignedShort(); // 2 (6)
        this.application = stream.readUnsignedShort(); // 2 (8)
        this.status = stream.readUnsignedShort(); // 2 (10)
        this.sts = stream.readUnsignedShort(); // 2 (12)
        this.requestID = stream.readInt(); // 4 (16)
        this.port = stream.readInt(); // 4 (20)
        this.exercise = stream.readInt(); // 4 (24)
        
        byte buffer[] = new byte[TERRAIN_LENGTH];
        
        stream.read(buffer, 0, TERRAIN_LENGTH);
        
        this.vctsTerrain = new String(buffer).trim();
        
        stream.read(buffer, 0, TERRAIN_LENGTH);
        
        this.onesafTerrain = new String(buffer).trim();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        
        stream.writeInt(DATUM_ID); // 4 bytes
        stream.writeInt(DATUM_LENGTH); // 4 bytes
        
        this.ios.write(stream);
        
        stream.writeShort(this.number);
        stream.writeShort(this.application);
        stream.writeShort(this.status);
        stream.writeShort(this.sts);
        stream.writeInt(this.requestID);
        stream.writeInt(this.port);
        stream.writeInt(this.exercise);
        
        if ((this.vctsTerrain == null) || this.vctsTerrain.isEmpty()) {
            
            for(int i = 0; i < TERRAIN_LENGTH; ++i) {
                
                stream.writeByte(0);
            }
        }
        else {
            
            char chars[] = this.vctsTerrain.toCharArray();
            
            for(int i = 0; i < TERRAIN_LENGTH; ++i) {
                
                if (i < chars.length) {
                    
                    stream.writeByte(chars[i]);
                }
                else {
                    
                    stream.writeByte(0);
                }
            }
        }
        
        if ((this.onesafTerrain == null) || this.onesafTerrain.isEmpty()) {
            
            for(int i = 0; i < TERRAIN_LENGTH; ++i) {
                
                stream.writeByte(0);
            }
        }
        else {
            
            char chars[] = this.onesafTerrain.toCharArray();
            
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
