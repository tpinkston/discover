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
import discover.vdis.types.ObjectType.Geometry;
import discover.vdis.types.ObjectTypes;

/**
 * @author Tony Pinkston
 */
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

        objectId.clear();
        referencedObjectId.clear();
        objectType = null;
        requestor.clear();
        receiver.clear();
        location.clear();
        orientation.clear();
        generic.clear();
        specific.set((byte)0);
        force = 0;
        update = 0;
        modifications = 0;
   }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Object", objectId.toString());
        buffer.addAttribute("Referenced Object", referencedObjectId.toString());
        buffer.addAttribute(
            "Force",
            VDIS.getDescription(VDIS.FORCE_ID, force));
        buffer.addAttribute("Requestor", requestor.toString());
        buffer.addAttribute("Receiver", receiver.toString());
        buffer.addAttribute("Update Number", update);
        buffer.addAttribute("Modifications", modifications);
        buffer.addBreak();

        buffer.addTitle("TYPE");
        objectType.toBuffer(buffer);
        buffer.addBreak();

        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Location", location.toString());
        buffer.addAttribute("Orientation", orientation.toString());
        buffer.addBreak();

        buffer.addTitle("GENERIC APPEARANCE");
        buffer.addBuffer(generic);
        buffer.addBreak();

        buffer.addTitle("SPECIFIC APPEARANCE");
        buffer.addBuffer(specific);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        objectId.read(stream);
        referencedObjectId.read(stream);
        update = stream.readUnsignedShort();
        force = stream.readUnsignedByte();
        modifications = stream.readUnsignedByte();
        objectType = ObjectTypes.getObjectType(POINT, stream.readInt());
        location.read(stream);
        orientation.read(stream);
        generic.read(stream);
        specific.read(stream);

        stream.readShort(); // 16 bits padding

        requestor.readPartial(stream);
        receiver.readPartial(stream);

        stream.readInt(); // 32 bits padding
   }
}
