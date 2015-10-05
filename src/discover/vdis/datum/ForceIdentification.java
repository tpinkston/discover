/**
 * @author Tony Pinkston
 */
package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.enums.VDIS;

public class ForceIdentification extends AbstractDatumRecord {

    private int forceId = 0;
    private String name = null;
    
    public ForceIdentification(int id) {
        
        super(id);
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Datum Id", super.getDatumId());
        buffer.addAttribute("Datum Length (bytes)", super.getValueSizeInBytes());
        buffer.addAttribute("Force Id", this.forceId, VDIS.FORCE_ID);
        buffer.addAttribute("Name", this.name);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)
        
        this.forceId = stream.readUnsignedShort();
        
        stream.skipBytes(2); // padding
        
        byte[] array = new byte[super.getValueSizeInBytes() - 4];
        
        stream.read(array, 0, array.length);

        this.name = new String(array);
        this.name = this.name.trim();
    }
}
