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
        
        this.originator.clear();
        this.recipient.clear();
        this.real.clear();
        this.simulation.clear();
        this.requestId = -1L;
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Originator", this.originator.toString());
        buffer.addAttribute("Recipient", this.recipient.toString());
        buffer.addAttribute("Request Id", this.requestId);
        buffer.addBreak();
        
        buffer.addTitle("REAL TIME");
        buffer.addBuffer(this.real);
        buffer.addBreak();
        
        buffer.addTitle("SIMULATION TIME");
        buffer.addBuffer(this.simulation);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)
        
        this.originator.read(stream);
        this.recipient.read(stream);
        this.real.read(stream);
        this.simulation.read(stream);
        this.requestId = Common.toUnsigned32(stream.readInt());
    }
}
