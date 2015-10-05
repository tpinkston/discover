/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.datum.DatumSpecificationRecord;
import discover.vdis.enums.VDIS;

public class ActionResponse extends AbstractPDU {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private DatumSpecificationRecord specification = new DatumSpecificationRecord();
    private int status = 0;
    private long requestId = 0;

    public ActionResponse() {

    }

    public int getStatus() { return this.status; }

    public EntityId getOriginator() { return this.originator; }

    public EntityId getRecipient() { return this.recipient; }

    public DatumSpecificationRecord getSpecification() { return this.specification; }
    
    @Override
    public void clear() {
        
        this.originator.clear();
        this.recipient.clear();
        this.specification.clear();
        this.requestId = 0;
        this.status = 0;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("RESPONSE");
        buffer.addAttribute("Originator", this.originator.toString());
        buffer.addAttribute("Recipient", this.recipient.toString());
        buffer.addAttribute("Request Id", Long.toString(this.requestId));
        buffer.addAttribute("Status", this.status, VDIS.ACTRES_REQ_STATUS);
        buffer.addBreak();
        buffer.addBuffer(this.specification);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)
        
        this.originator.read(stream);
        this.recipient.read(stream);
        this.requestId = Common.toUnsigned32(stream.readInt());
        this.status = stream.readInt();
        this.specification.read(stream);
    }
}
