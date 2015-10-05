/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.ClockTime;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

public class StopFreeze extends AbstractPDU {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private ClockTime real = new ClockTime();
    private int reason = 0;
    private int behavior = 0;
    private long requestId = -1L;

    public StopFreeze() {

    }

    @Override
    public void clear() {
        
        this.originator.clear();
        this.recipient.clear();
        this.real.clear();
        this.reason = 0;
        this.behavior = 0;
        this.requestId = -1L;
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Originator", this.originator.toString());
        buffer.addAttribute("Recipient", this.recipient.toString());
        buffer.addAttribute(
            "Reason", 
            VDIS.getDescription(VDIS.SF_REASON_CODES, this.reason));
        buffer.addAttribute(
            "Frozen Behavior",
            VDIS.getDescription(VDIS.FROZEN_BEHAVIOR, this.behavior));
        buffer.addAttribute("Request Id", this.requestId);
        buffer.addBreak();
        
        buffer.addTitle("REAL TIME");
        buffer.addBuffer(this.real);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)
        
        this.originator.read(stream);
        this.recipient.read(stream);
        this.real.read(stream);
        this.reason = stream.readUnsignedByte();
        this.behavior = stream.readUnsignedByte();
        stream.skipBytes(2); // 16 bit padding
        this.requestId = Common.toUnsigned32(stream.readInt());
    }
}
