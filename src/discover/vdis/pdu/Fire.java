/**
 * @author Tony Pinkston
 */
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

    public EntityId getShooter() { return this.shooter; }
    public EntityId getTarget() { return this.target; }
    public EntityId getMunition() { return this.munition; }
    public EntityId getEvent() { return this.event; }
    public Location24 getLocation() { return this.location; }
    public Velocity getVelocity() { return this.velocity; }
    public BurstDescriptor getBurst() { return this.burst; }
    public long getFireMission() { return this.fireMission; }
    public float getRange() { return this.range; }
    public Detonation getDetonation() { return detonation; }

    public void setDetonation(Detonation detonation) {
    
        this.detonation = detonation;
    }

    @Override
    public void clear() {
        
        this.shooter.clear();
        this.target.clear();
        this.munition.clear();
        this.event.clear();
        this.location.clear();
        this.velocity.clear();
        this.burst.clear();
        this.fireMission = 0L;
        this.range = 0.0f;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);
        
        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Shooter", this.shooter.toString());
        buffer.addAttribute("Target", this.target.toString());
        buffer.addAttribute("Munition", this.munition.toString());
        buffer.addAttribute("Event", this.event.toString());
        buffer.addAttribute("Fire Mission", this.fireMission);
        buffer.addBreak();
        
        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Velocity", this.velocity.toString());
        buffer.addAttribute("Location", this.location.toString());
        buffer.addAttribute("Range", this.range);
        buffer.addBreak();
        
        buffer.addBuffer(this.burst);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)
        
        this.shooter.read(stream); // 6 bytes
        this.target.read(stream); // 6 bytes
        this.munition.read(stream); // 6 bytes
        this.event.read(stream); // 6 bytes
        this.fireMission = Common.toUnsigned32(stream.readInt()); // 4 bytes
        this.location.read(stream); // 24 bytes
        this.burst.read(stream); // 16 bytes
        this.velocity.read(stream); // 12 bytes
        this.range = stream.readFloat(); // 4 bytes
    }
}
