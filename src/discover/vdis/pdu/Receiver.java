package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
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

        entityId.clear();
        transmitterEntityId.clear();
        receiverState = 0;
        radioId = 0;
        transmitterRadioId = 0;
        power = 0.0f;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Entity", entityId.toString());
        buffer.addAttribute("Radio", radioId);
        buffer.addBreak();

        buffer.addTitle("RECEIVER");
        buffer.addAttribute(
            "State",
            VDIS.getDescription(VDIS.RECEIVER, receiverState));
        buffer.addAttribute("Power (dBm)", power);
        buffer.addBreak();

        buffer.addTitle("TRANSMITTER");
        buffer.addAttribute("Entity", transmitterEntityId.toString());
        buffer.addAttribute("Radio", transmitterRadioId);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        entityId.read(stream);
        radioId = stream.readUnsignedShort();
        receiverState = stream.readUnsignedShort();
        stream.skipBytes(2); // padding
        power = stream.readFloat();
        transmitterEntityId.read(stream);
        radioId = stream.readUnsignedShort();
    }
}
