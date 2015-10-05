/**
 * @author Tony Pinkston
 */
package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.CDTWaypointConstraint;
import discover.vdis.common.CDTWaypointDescription;
import discover.vdis.common.Location24;
import discover.vdis.enums.VDIS;

public class CDTWaypoint extends AbstractDatumRecord implements Writable {

    /** DID_CDT_WAYPOINT (not in V-DIS specification) */
    public static final int DATUM_ID = 405100;
    
    /** Length of entire record minus datum ID and length fields. */
    public static final int DATUM_LENGTH = (576 - 64);
    
    private static final NumberFormat formatter;

    static {
        
        formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(4);
    }

    public final CDTWaypointConstraint constraint;
    public final CDTWaypointDescription description;
    public final Location24 location;
    public float speed = 0.0f;
    
    public CDTWaypoint() {
        
        this(DATUM_ID);
    }
    
    public CDTWaypoint(int id) {
        
        super(id);
        
        this.constraint = new CDTWaypointConstraint();
        this.description = new CDTWaypointDescription();
        this.location = new Location24();
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("CDT Waypoint");
        buffer.addAttribute("Datum Id", super.getDatumId(), VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Length (bytes)", Integer.toString(super.getValueSizeInBytes()));
        buffer.addAttribute("Location (GCC)", this.location.toStringGCC());
        buffer.addAttribute("Location (GDC)", this.location.toStringGDC());
        buffer.addAttribute("Speed (m/s)", this.speed);
        buffer.addAttribute("Speed (kmph)", (this.speed * 3.6f));
        buffer.addAttribute("Speed (mph)", (this.speed * 2.237f));
        buffer.addTitle("Description");
        buffer.addBuffer(this.description);
        buffer.addTitle("Constraint");
        buffer.addBuffer(this.constraint);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)
        
        this.location.read(stream); // 24 bytes
        this.speed = stream.readFloat(); // 4 bytes
        this.description.read(stream); // 4 bytes
        this.constraint.read(stream); // 32 bytes
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        
        stream.writeInt(DATUM_ID); // 4 bytes
        stream.writeInt(DATUM_LENGTH); // 4 bytes
        this.location.write(stream); // 24 bytes
        stream.writeFloat(this.speed); // 4 bytes
        this.description.write(stream); // 4 bytes
        this.constraint.write(stream); // 32 bytes
    }
}
