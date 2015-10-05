/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Binary;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.common.IFFFundamentalOperationalData;
import discover.vdis.common.IFFSystemIdentifier;
import discover.vdis.common.Location12;

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

        this.emitter.clear();
        this.event.clear();
        this.location.clear();
        this.identifier.clear();
        this.operational.clear();
        this.designator = 0x00;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Emitter", this.emitter.toString());
        buffer.addAttribute("Event", this.event.toString());
        buffer.addBreak();

        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Location", this.location.toString());
        buffer.addBreak();

        buffer.addTitle("SYSTEM ID");
        buffer.addBuffer(this.identifier);
        buffer.addBreak();

        buffer.addTitle("FUNDAMENTAL OPERATIONAL DATA");
        buffer.addBuffer(this.operational);
        buffer.addBreak();

        buffer.addTitle("SYSTEM DESIGNATOR");
        buffer.addItalic(Binary.toString8(this.designator));
        buffer.addBreak();
        buffer.addBreak();

        buffer.addTitle("SYSTEM SPECIFIC DATA");
        buffer.addItalic(Binary.toString8(this.specific));
        buffer.addBreak();
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        this.emitter.read(stream);
        this.event.read(stream);
        this.location.read(stream);
        this.identifier.read(stream);
        this.designator = stream.readByte();
        this.specific = stream.readByte();
        this.operational.read(stream);
    }
}
