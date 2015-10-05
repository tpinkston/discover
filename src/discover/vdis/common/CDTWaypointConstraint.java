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

public class CDTWaypointConstraint implements Bufferable, Readable, Writable {

    public final EntityId entity = new EntityId();
    public int trigger = 0;
    public float constraint = 0.0f;
    public final Location24 location = new Location24();
    
    public CDTWaypointConstraint() {

    }
    
    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeByte(this.trigger); // 1 byte
        stream.writeByte(0); // 1 byte padding
        this.entity.write(stream); // 6 bytes
        stream.writeInt(0); // 4 bytes padding
        stream.writeFloat(this.constraint); // 4 bytes
        
        this.location.write(stream); // 24 bytes
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.trigger = stream.readUnsignedByte(); // 1 byte
        
        stream.skipBytes(1); // 1 byte padding
        
        this.entity.read(stream); // 6 bytes
        
        stream.skipBytes(4); // 4 bytes padding
        
        this.constraint = stream.readFloat(); // 4 bytes
        this.location.read(stream); // 24 bytes
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute(
            "Trigger Type", 
            this.trigger, 
            VDIS.CDT_WAYPOINT_TRIGGER_TYPE);
        
        buffer.addAttribute("Entity Id", this.entity.toString());
        buffer.addAttribute("Constraint Value", this.constraint);
        buffer.addAttribute("Location (GCC)", this.location.toStringGCC());
        buffer.addAttribute("Location (GDC)", this.location.toStringGDC());
    }
}
