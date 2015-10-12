package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Binary;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.common.IFFFundamentalOperationalData;
import discover.vdis.common.IFFSystemIdentifier;
import discover.vdis.common.Location12;

/**
 * @author Tony Pinkston
 */
public class IdentificationFriendOrFoe extends AbstractPDU {

    private EntityId emitter = new EntityId();
    private EntityId event = new EntityId();
    private Location12 location = new Location12();
    private IFFSystemIdentifier identifier = new IFFSystemIdentifier();
    private IFFFundamentalOperationalData operational = new IFFFundamentalOperationalData();
    private byte designator = 0x00;
    private byte specific = 0x00;

    public IdentificationFriendOrFoe() {

    }

    @Override
    public void clear() {

        emitter.clear();
        event.clear();
        location.clear();
        identifier.clear();
        operational.clear();
        designator = 0x00;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Emitter", emitter.toString());
        buffer.addAttribute("Event", event.toString());
        buffer.addBreak();

        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Location", location.toString());
        buffer.addBreak();

        buffer.addTitle("SYSTEM ID");
        buffer.addBuffer(identifier);
        buffer.addBreak();

        buffer.addTitle("FUNDAMENTAL OPERATIONAL DATA");
        buffer.addBuffer(operational);
        buffer.addBreak();

        buffer.addTitle("SYSTEM DESIGNATOR");
        buffer.addItalic(Binary.toString8(designator));
        buffer.addBreak();
        buffer.addBreak();

        buffer.addTitle("SYSTEM SPECIFIC DATA");
        buffer.addItalic(Binary.toString8(specific));
        buffer.addBreak();
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        emitter.read(stream);
        event.read(stream);
        location.read(stream);
        identifier.read(stream);
        designator = stream.readByte();
        specific = stream.readByte();
        operational.read(stream);
    }
}
