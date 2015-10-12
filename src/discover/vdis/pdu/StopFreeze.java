package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.ClockTime;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
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

        originator.clear();
        recipient.clear();
        real.clear();
        reason = 0;
        behavior = 0;
        requestId = -1L;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Originator", originator.toString());
        buffer.addAttribute("Recipient", recipient.toString());
        buffer.addAttribute(
            "Reason",
            VDIS.getDescription(VDIS.SF_REASON_CODES, reason));
        buffer.addAttribute(
            "Frozen Behavior",
            VDIS.getDescription(VDIS.FROZEN_BEHAVIOR, behavior));
        buffer.addAttribute("Request Id", requestId);
        buffer.addBreak();

        buffer.addTitle("REAL TIME");
        buffer.addBuffer(real);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        originator.read(stream);
        recipient.read(stream);
        real.read(stream);
        reason = stream.readUnsignedByte();
        behavior = stream.readUnsignedByte();
        stream.skipBytes(2); // 16 bit padding
        requestId = Common.toUnsigned32(stream.readInt());
    }
}
