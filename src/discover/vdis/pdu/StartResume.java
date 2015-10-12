package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.ClockTime;
import discover.vdis.common.EntityId;

/**
 * @author Tony Pinkston
 */
public class StartResume extends AbstractPDU {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private ClockTime real = new ClockTime();
    private ClockTime simulation = new ClockTime();
    private long requestId = -1L;

    public StartResume() {

    }

    @Override
    public void clear() {

        originator.clear();
        recipient.clear();
        real.clear();
        simulation.clear();
        requestId = -1L;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Originator", originator.toString());
        buffer.addAttribute("Recipient", recipient.toString());
        buffer.addAttribute("Request Id", requestId);
        buffer.addBreak();

        buffer.addTitle("REAL TIME");
        buffer.addBuffer(real);
        buffer.addBreak();

        buffer.addTitle("SIMULATION TIME");
        buffer.addBuffer(simulation);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        originator.read(stream);
        recipient.read(stream);
        real.read(stream);
        simulation.read(stream);
        requestId = Common.toUnsigned32(stream.readInt());
    }
}
