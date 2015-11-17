package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.BurstDescriptor;
import discover.vdis.common.EntityId;
import discover.vdis.common.Location12;
import discover.vdis.common.Location24;
import discover.vdis.common.Velocity;
import discover.vdis.enums.DETONATION_RESULT;

/**
 * @author Tony Pinkston
 */
public class Detonation extends AbstractPDU {

    private EntityId shooter = new EntityId();
    private EntityId target = new EntityId();
    private EntityId munition = new EntityId();
    private EntityId event = new EntityId();
    private Location24 location = new Location24();
    private Velocity velocity = new Velocity();
    private Location12 offset = new Location12();
    private BurstDescriptor burst = new BurstDescriptor();
    private int result = 0;
    private int parameters = 0;
    private byte[] parameterData = null;

    public Detonation() {

    }

    public EntityId getShooter() { return shooter; }
    public EntityId getTarget() { return target; }
    public EntityId getMunition() { return munition; }
    public EntityId getEvent() { return event; }
    public Location24 getLocation() { return location; }
    public Velocity getVelocity() { return velocity; }
    public Location12 getOffset() { return offset; }
    public BurstDescriptor getBurst() { return burst; }
    public int getDetonationResult() { return result; }
    public int getParameters() { return parameters; }

    @Override
    public void clear() {

        shooter.clear();
        target.clear();
        munition.clear();
        event.clear();
        location.clear();
        velocity.clear();
        offset.clear();
        burst.clear();
        result = 0;
        parameters = 0;
        parameterData = null;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Shooter", shooter.toString());
        buffer.addAttribute("Target", target.toString());
        buffer.addAttribute("Munition", munition.toString());
        buffer.addAttribute("Event", event.toString());
        buffer.addBreak();

        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Velocity", velocity.toString());
        buffer.addAttribute("Location", location.toString());
        buffer.addAttribute("Offset", offset.toString());
        buffer.addBreak();

        buffer.addBuffer(burst);
        buffer.addAttribute("Result", result, DETONATION_RESULT.class);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        shooter.read(stream); // 6 bytes
        target.read(stream); // 6 bytes
        munition.read(stream); // 6 bytes
        event.read(stream); // 6 bytes
        velocity.read(stream); // 12 bytes
        location.read(stream); // 24 bytes
        burst.read(stream); // 16 bytes
        offset.read(stream); // 12 bytes
        result = stream.readUnsignedByte(); // 1 byte
        parameters = stream.readUnsignedByte(); // 1 byte

        stream.readShort(); // 2 bytes padding

        int count = stream.available();

        if (count == 0) {

            parameterData = null;
        }
        else {

            parameterData = new byte[count];

            stream.read(parameterData, 0, count);
        }
    }
}
