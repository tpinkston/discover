package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.BurstDescriptor;
import discover.vdis.common.EntityId;
import discover.vdis.common.Location24;
import discover.vdis.common.Velocity;

/**
 * Always 96 bytes
 *
 * @author Tony Pinkston
 */
public class Fire extends AbstractPDU {

    private EntityId shooter = new EntityId();
    private EntityId target = new EntityId();
    private EntityId munition = new EntityId();
    private EntityId event = new EntityId();
    private Location24 location = new Location24();
    private Velocity velocity = new Velocity();
    private BurstDescriptor burst = new BurstDescriptor();
    private long fireMission = 0L;
    private float range = 0.0f;

    /** Resulting detonation PDU, event Id will be identical. */
    private Detonation detonation = null;

    public Fire() {

    }

    public EntityId getShooter() { return shooter; }
    public EntityId getTarget() { return target; }
    public EntityId getMunition() { return munition; }
    public EntityId getEvent() { return event; }
    public Location24 getLocation() { return location; }
    public Velocity getVelocity() { return velocity; }
    public BurstDescriptor getBurst() { return burst; }
    public long getFireMission() { return fireMission; }
    public float getRange() { return range; }
    public Detonation getDetonation() { return detonation; }

    public void setDetonation(Detonation detonation) {

        this.detonation = detonation;
    }

    @Override
    public void clear() {

        shooter.clear();
        target.clear();
        munition.clear();
        event.clear();
        location.clear();
        velocity.clear();
        burst.clear();
        fireMission = 0L;
        range = 0.0f;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Shooter", shooter.toString());
        buffer.addAttribute("Target", target.toString());
        buffer.addAttribute("Munition", munition.toString());
        buffer.addAttribute("Event", event.toString());
        buffer.addAttribute("Fire Mission", fireMission);
        buffer.addBreak();

        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Velocity", velocity.toString());
        buffer.addAttribute("Location", location.toString());
        buffer.addAttribute("Range", range);
        buffer.addBreak();

        buffer.addBuffer(burst);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        shooter.read(stream); // 6 bytes
        target.read(stream); // 6 bytes
        munition.read(stream); // 6 bytes
        event.read(stream); // 6 bytes
        fireMission = Common.toUnsigned32(stream.readInt()); // 4 bytes
        location.read(stream); // 24 bytes
        burst.read(stream); // 16 bytes
        velocity.read(stream); // 12 bytes
        range = stream.readFloat(); // 4 bytes
    }
}
