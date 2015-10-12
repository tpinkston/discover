package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;

/**
 * @author Tony Pinkston
 */
public class CreateEntity extends AbstractPDU {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private long requestId = 0;

    public CreateEntity() {

    }

    @Override
    public void clear() {

        originator.clear();
        recipient.clear();
        requestId = 0;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);


        buffer.addAttribute("Originator", originator.toString());
        buffer.addAttribute("Recipient", recipient.toString());
        buffer.addAttribute("Request Id", requestId);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        originator.read(stream);
        recipient.read(stream);
        requestId = Common.toUnsigned32(stream.readInt());
    }
}
