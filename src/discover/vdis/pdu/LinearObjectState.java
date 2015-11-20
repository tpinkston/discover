package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.common.LinearSegment;
import discover.vdis.common.ObjectId;
import discover.vdis.enums.FORCE_ID;
import discover.vdis.enums.OBJECT_GEOMETRY;
import discover.vdis.types.ObjectType;
import discover.vdis.types.ObjectTypes;

/**
 * @author Tony Pinkston
 */
public class LinearObjectState extends AbstractPDU {

    private ObjectId objectId = new ObjectId();
    private ObjectId referencedObjectId = new ObjectId();
    private ObjectType objectType = null;
    private EntityId requestor = new EntityId();
    private EntityId receiver = new EntityId();
    private List<LinearSegment> segments = new ArrayList<LinearSegment>();
    private int force = 0;
    private int update = 0;
    private int count = 0;

    public LinearObjectState() {

    }

    @Override
    public void clear() {

        objectId.clear();
        referencedObjectId.clear();
        objectType = null;
        requestor.clear();
        receiver.clear();
        segments.clear();
        force = 0;
        update = 0;
        count = 0;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        String count = (this.count + " (" + segments.size() + ")");
        FORCE_ID forceEnum = FORCE_ID.get(force);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Object", objectId.toString());
        buffer.addAttribute("Referenced Object", referencedObjectId.toString());
        buffer.addAttribute("Force", forceEnum.description);
        buffer.addAttribute("Requestor", requestor.toString());
        buffer.addAttribute("Receiver", receiver.toString());
        buffer.addAttribute("Update Number", update);
        buffer.addAttribute("Segments", count);
        buffer.addBreak();

        buffer.addTitle("TYPE");
        objectType.toBuffer(buffer);
        buffer.addBreak();

        for(LinearSegment segment : segments) {

            buffer.addBuffer(segment);
        }

        buffer.addSeparator();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        objectId.read(stream);
        referencedObjectId.read(stream);
        update = stream.readUnsignedShort();
        force = stream.readUnsignedByte();
        count = stream.readUnsignedByte();
        requestor.readPartial(stream);
        receiver.readPartial(stream);
        objectType = ObjectTypes.getObjectType(
            OBJECT_GEOMETRY.LINEAR.value,
            stream.readInt());

        for(int i = 0; i < count; ++i) {

            segments.add(new LinearSegment(stream));
        }
    }
}
