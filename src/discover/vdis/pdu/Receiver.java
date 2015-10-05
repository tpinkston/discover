/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

public class Receiver extends AbstractPDU {

    private EntityId entityId = new EntityId();
    private EntityId transmitterEntityId = new EntityId();
    private int receiverState = 0;
    private int radioId = 0;
    private int transmitterRadioId = 0;
    private float power = 0.0f;

    public Receiver() {

    }

    @Override
    public void clear() {

        this.entityId.clear();
        this.transmitterEntityId.clear();
        this.receiverState = 0;
        this.radioId = 0;
        this.transmitterRadioId = 0;
        this.power = 0.0f;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Entity", this.entityId.toString());
        buffer.addAttribute("Radio", this.radioId);
        buffer.addBreak();

        buffer.addTitle("RECEIVER");
        buffer.addAttribute(
            "State",
            VDIS.getDescription(VDIS.RECEIVER, this.receiverState));
        buffer.addAttribute("Power (dBm)", this.power);
        buffer.addBreak();

        buffer.addTitle("TRANSMITTER");
        buffer.addAttribute("Entity", this.transmitterEntityId.toString());
        buffer.addAttribute("Radio", this.transmitterRadioId);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        this.entityId.read(stream);
        this.radioId = stream.readUnsignedShort();
        this.receiverState = stream.readUnsignedShort();
        stream.skipBytes(2); // padding
        this.power = stream.readFloat();
        this.transmitterEntityId.read(stream);
        this.radioId = stream.readUnsignedShort();
    }
}
