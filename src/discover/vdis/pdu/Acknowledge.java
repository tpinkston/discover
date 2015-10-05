/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

public class Acknowledge extends AbstractPDU {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private int acknowledge = 0;
    private int response = 0;
    private long requestId = -1;
    
    public Acknowledge() {
    
    }

    public EntityId getOriginator() { return this.originator; }

    public EntityId getRecipient() { return this.recipient; }
    
    @Override
    public void clear() {
        
        this.originator.clear();
        this.recipient.clear();
        this.acknowledge = 0;
        this.response = 0;
        this.requestId = -1;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("ACKNOWLEDGE");
        buffer.addAttribute("Originator", this.originator.toString());
        buffer.addAttribute("Recipient", this.recipient.toString());
        buffer.addAttribute(
            "Acknowledge Flag",
            this.acknowledge,
            VDIS.ACK_ACKNOWLEDGE_FLAG);
        buffer.addAttribute(
            "Response Flag",
            this.response,
            VDIS.ACK_RESPONSE_FLAG);
        buffer.addAttribute("Request Id", this.requestId);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)
        
        this.originator.read(stream);
        this.recipient.read(stream);
        this.acknowledge = stream.readUnsignedShort();
        this.response = stream.readUnsignedShort();
        this.requestId = Common.toUnsigned32(stream.readInt());
    }
}
