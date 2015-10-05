/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.appearance.GenericObjectAppearance;
import discover.vdis.appearance.SpecificObjectAppearance;
import discover.vdis.common.EntityId;
import discover.vdis.common.Location24;
import discover.vdis.common.ObjectId;
import discover.vdis.common.Orientation;
import discover.vdis.enums.VDIS;
import discover.vdis.types.ObjectType;
import discover.vdis.types.ObjectTypes;
import discover.vdis.types.ObjectType.Geometry;

public class PointObjectState extends AbstractPDU {

    private static final Geometry POINT = Geometry.POINT;
    
    private ObjectId objectId = new ObjectId();
    private ObjectId referencedObjectId = new ObjectId();
    private ObjectType objectType = null;
    private EntityId requestor = new EntityId();
    private EntityId receiver = new EntityId();
    private Location24 location = new Location24();
    private Orientation orientation = new Orientation();
    private GenericObjectAppearance generic = new GenericObjectAppearance();
    private SpecificObjectAppearance specific = new SpecificObjectAppearance();
    private int force = 0;
    private int update = 0;
    private int modifications = 0;
    
    public PointObjectState() {

    }

    @Override
    public void clear() {

        this.objectId.clear();
        this.referencedObjectId.clear();
        this.objectType = null;
        this.requestor.clear();
        this.receiver.clear();
        this.location.clear();
        this.orientation.clear();
        this.generic.clear();
        this.specific.set((byte)0);
        this.force = 0;
        this.update = 0;
        this.modifications = 0;
   }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);
        
        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Object", this.objectId.toString());
        buffer.addAttribute("Referenced Object", this.referencedObjectId.toString());
        buffer.addAttribute(
            "Force", 
            VDIS.getDescription(VDIS.FORCE_ID, this.force));
        buffer.addAttribute("Requestor", this.requestor.toString());
        buffer.addAttribute("Receiver", this.receiver.toString());
        buffer.addAttribute("Update Number", this.update);
        buffer.addAttribute("Modifications", this.modifications);
        buffer.addBreak();

        buffer.addTitle("TYPE");
        this.objectType.toBuffer(buffer);
        buffer.addBreak();

        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Location", this.location.toString());
        buffer.addAttribute("Orientation", this.orientation.toString());
        buffer.addBreak();

        buffer.addTitle("GENERIC APPEARANCE");
        buffer.addBuffer(this.generic);
        buffer.addBreak();

        buffer.addTitle("SPECIFIC APPEARANCE");
        buffer.addBuffer(this.specific);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)
        
        this.objectId.read(stream);
        this.referencedObjectId.read(stream);
        this.update = stream.readUnsignedShort();
        this.force = stream.readUnsignedByte();
        this.modifications = stream.readUnsignedByte();
        this.objectType = ObjectTypes.getObjectType(POINT, stream.readInt());
        this.location.read(stream);
        this.orientation.read(stream);
        this.generic.read(stream);
        this.specific.read(stream);
        
        stream.readShort(); // 16 bits padding

        this.requestor.readPartial(stream);
        this.receiver.readPartial(stream);
        
        stream.readInt(); // 32 bits padding
   }
}
