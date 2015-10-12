package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class Acknowledge extends AbstractPDU {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private int acknowledge = 0;
    private int response = 0;
    private long requestId = -1;

    public Acknowledge() {

    }

    public EntityId getOriginator() { return originator; }

    public EntityId getRecipient() { return recipient; }

    @Override
    public void clear() {

        originator.clear();
        recipient.clear();
        acknowledge = 0;
        response = 0;
        requestId = -1;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("ACKNOWLEDGE");
        buffer.addAttribute("Originator", originator.toString());
        buffer.addAttribute("Recipient", recipient.toString());
        buffer.addAttribute(
            "Acknowledge Flag",
            acknowledge,
            VDIS.ACK_ACKNOWLEDGE_FLAG);
        buffer.addAttribute(
            "Response Flag",
            response,
            VDIS.ACK_RESPONSE_FLAG);
        buffer.addAttribute("Request Id", requestId);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        originator.read(stream);
        recipient.read(stream);
        acknowledge = stream.readUnsignedShort();
        response = stream.readUnsignedShort();
        requestId = Common.toUnsigned32(stream.readInt());
    }
}
