/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.common.LinearSegment;
import discover.vdis.common.ObjectId;
import discover.vdis.enums.VDIS;
import discover.vdis.types.ObjectType;
import discover.vdis.types.ObjectTypes;
import discover.vdis.types.ObjectType.Geometry;

public class LinearObjectState extends AbstractPDU {

    private static final Geometry LINEAR = Geometry.LINEAR;

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

        this.objectId.clear();
        this.referencedObjectId.clear();
        this.objectType = null;
        this.requestor.clear();
        this.receiver.clear();
        this.segments.clear();
        this.force = 0;
        this.update = 0;
        this.count = 0;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        String count = (this.count + " (" + this.segments.size() + ")");

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Object", this.objectId.toString());
        buffer.addAttribute("Referenced Object", this.referencedObjectId.toString());
        buffer.addAttribute(
            "Force",
            VDIS.getDescription(VDIS.FORCE_ID, this.force));
        buffer.addAttribute("Requestor", this.requestor.toString());
        buffer.addAttribute("Receiver", this.receiver.toString());
        buffer.addAttribute("Update Number", this.update);
        buffer.addAttribute("Segments", count);
        buffer.addBreak();

        buffer.addTitle("TYPE");
        this.objectType.toBuffer(buffer);
        buffer.addBreak();

        for(LinearSegment segment : this.segments) {

            buffer.addBuffer(segment);
        }

        buffer.addSeparator();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        this.objectId.read(stream);
        this.referencedObjectId.read(stream);
        this.update = stream.readUnsignedShort();
        this.force = stream.readUnsignedByte();
        this.count = stream.readUnsignedByte();
        this.requestor.readPartial(stream);
        this.receiver.readPartial(stream);
        this.objectType = ObjectTypes.getObjectType(LINEAR, stream.readInt());

        for(int i = 0; i < this.count; ++i) {

            this.segments.add(new LinearSegment(stream));
        }
    }
}
