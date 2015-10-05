/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.BurstDescriptor;
import discover.vdis.common.EntityId;
import discover.vdis.common.Location12;
import discover.vdis.common.Location24;
import discover.vdis.common.Velocity;
import discover.vdis.enums.VDIS;

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

    public EntityId getShooter() { return this.shooter; }
    public EntityId getTarget() { return this.target; }
    public EntityId getMunition() { return this.munition; }
    public EntityId getEvent() { return this.event; }
    public Location24 getLocation() { return this.location; }
    public Velocity getVelocity() { return this.velocity; }
    public Location12 getOffset() { return this.offset; }
    public BurstDescriptor getBurst() { return this.burst; }
    public int getDetonationResult() { return this.result; }
    public int getParameters() { return this.parameters; }

    @Override
    public void clear() {

        this.shooter.clear();
        this.target.clear();
        this.munition.clear();
        this.event.clear();
        this.location.clear();
        this.velocity.clear();
        this.offset.clear();
        this.burst.clear();
        this.result = 0;
        this.parameters = 0;
        this.parameterData = null;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Shooter", this.shooter.toString());
        buffer.addAttribute("Target", this.target.toString());
        buffer.addAttribute("Munition", this.munition.toString());
        buffer.addAttribute("Event", this.event.toString());
        buffer.addBreak();

        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Velocity", this.velocity.toString());
        buffer.addAttribute("Location", this.location.toString());
        buffer.addAttribute("Offset", this.offset.toString());
        buffer.addBreak();

        buffer.addBuffer(this.burst);
        buffer.addAttribute(
            "Result",
            this.result,
            VDIS.DETONATION_RESULT);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        this.shooter.read(stream); // 6 bytes
        this.target.read(stream); // 6 bytes
        this.munition.read(stream); // 6 bytes
        this.event.read(stream); // 6 bytes
        this.velocity.read(stream); // 12 bytes
        this.location.read(stream); // 24 bytes
        this.burst.read(stream); // 16 bytes
        this.offset.read(stream); // 12 bytes
        this.result = stream.readUnsignedByte(); // 1 byte
        this.parameters = stream.readUnsignedByte(); // 1 byte

        stream.readShort(); // 2 bytes padding

        int count = stream.available();

        if (count == 0) {

            this.parameterData = null;
        }
        else {

            this.parameterData = new byte[count];

            stream.read(this.parameterData, 0, count);
        }
    }
}
