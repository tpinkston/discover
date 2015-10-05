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

public class CDTGeneralDiscoveryRecord 
    extends AbstractDatumRecord 
    implements Writable {

    /** DID_CDT_GENERAL_DISCOVERY (not in V-DIS specification) */
    public static final int DATUM_ID = 400100;
    
    /** Length of entire record minus datum ID and length fields. */
    public static final int DATUM_LENGTH = (384 - 64);
    
    public static final int CAB_VARIANT_LENGTH = 20;
    public static final int PADDING_LENGTH = 3;

    public final IPAddress senderIPAddress = new IPAddress();
    public int stsNumber = 0;
    public int repeaterNumber = 0;
    public int cabType = 0;
    public String cabVariant = null;
    public int cabConfiguration = 0;
    public int atsioType = 0;
    public int onesafStatus = 0;
    public int actionId = 0;
    public final byte padding[] = new byte[PADDING_LENGTH];
    
    public CDTGeneralDiscoveryRecord() {
        
        this(DATUM_ID);
    }
    
    public CDTGeneralDiscoveryRecord(int id) {
        
        super(id);
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("General Discovery");
        buffer.addAttribute("Datum Id", super.getDatumId(), VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Length (bytes)", Integer.toString(super.getValueSizeInBytes()));
        buffer.addAttribute("Sender IP", this.senderIPAddress.toString());
        buffer.addAttribute("STS", Integer.toString(this.stsNumber));
        buffer.addAttribute("Repeater", Integer.toString(this.repeaterNumber));
        buffer.addAttribute("Cab Type", Integer.toString(this.cabType));
        buffer.addAttribute("Cab Configuration", Integer.toString(this.cabConfiguration));
        buffer.addAttribute("Cab Variant", new String(this.cabVariant));
        buffer.addAttribute("ATSIO Type", Integer.toString(this.atsioType));
        buffer.addAttribute("OneSAF Status", this.onesafStatus, VDIS.CDT_ONESAF_STATUS);
        buffer.addAttribute("Action Id", this.actionId, VDIS.ACTREQ_ACTION_IDS);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)
        
        this.senderIPAddress.read(stream);
        this.stsNumber = stream.readUnsignedByte();
        this.repeaterNumber = stream.readUnsignedByte();
        this.cabType = stream.readUnsignedByte();
        
        byte bytes[] = new byte[CAB_VARIANT_LENGTH];
        
        stream.read(bytes, 0, CAB_VARIANT_LENGTH);

        this.cabVariant = new String(bytes).trim();
        this.cabConfiguration = stream.readInt();
        this.atsioType = stream.readUnsignedByte();
        this.onesafStatus = stream.readUnsignedByte();
        this.actionId = stream.readInt();
        
        stream.read(this.padding, 0, PADDING_LENGTH);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        
        stream.writeInt(DATUM_ID); // 4 bytes
        stream.writeInt(DATUM_LENGTH); // 4 bytes
        this.senderIPAddress.write(stream);
        
        stream.writeByte(this.stsNumber);
        stream.writeByte(this.repeaterNumber);
        stream.writeByte(this.cabType);

        if ((this.cabVariant == null) || this.cabVariant.isEmpty()) {
            
            stream.writeByte('n');
            stream.writeByte('u');
            stream.writeByte('l');
            stream.writeByte('l');
            
            for(int i = 4; i < CAB_VARIANT_LENGTH; ++i) {
                
                stream.writeByte(0);
            }
        }
        else {
            
            char chars[] = this.cabVariant.toCharArray();
            
            for(int i = 0; i < CAB_VARIANT_LENGTH; ++i) {
                
                if (i < chars.length) {
                    
                    stream.writeByte(chars[i]);
                }
                else {
                    
                    stream.writeByte(0);
                }
            }
        }

        stream.writeInt(this.cabConfiguration);
        stream.writeByte(this.atsioType);
        stream.writeByte(this.onesafStatus);
        stream.writeInt(this.actionId);
        stream.write(this.padding, 0, PADDING_LENGTH);
    }
}
