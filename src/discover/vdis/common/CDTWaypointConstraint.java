package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class CDTWaypointConstraint implements Bufferable, Readable, Writable {

    public final EntityId entity = new EntityId();
    public int trigger = 0;
    public float constraint = 0.0f;
    public final Location24 location = new Location24();

    public CDTWaypointConstraint() {

    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeByte(trigger); // 1 byte
        stream.writeByte(0); // 1 byte padding
        entity.write(stream); // 6 bytes
        stream.writeInt(0); // 4 bytes padding
        stream.writeFloat(constraint); // 4 bytes

        location.write(stream); // 24 bytes
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        trigger = stream.readUnsignedByte(); // 1 byte

        stream.skipBytes(1); // 1 byte padding

        entity.read(stream); // 6 bytes

        stream.skipBytes(4); // 4 bytes padding

        constraint = stream.readFloat(); // 4 bytes
        location.read(stream); // 24 bytes
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute(
            "Trigger Type",
            trigger,
            VDIS.CDT_WAYPOINT_TRIGGER_TYPE);

        buffer.addAttribute("Entity Id", entity.toString());
        buffer.addAttribute("Constraint Value", constraint);
        buffer.addAttribute("Location (GCC)", location.toStringGCC());
        buffer.addAttribute("Location (GDC)", location.toStringGDC());
    }
}
