package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.datum.DatumSpecificationRecord;
import discover.vdis.enums.ACTRES_REQ_STATUS;

/**
 * @author Tony Pinkston
 */
public class ActionResponse extends AbstractPDU {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private DatumSpecificationRecord specification = new DatumSpecificationRecord();
    private int status = 0;
    private long requestId = 0;

    public ActionResponse() {

    }

    public int getStatus() { return status; }

    public EntityId getOriginator() { return originator; }

    public EntityId getRecipient() { return recipient; }

    public DatumSpecificationRecord getSpecification() { return specification; }

    @Override
    public void clear() {

        originator.clear();
        recipient.clear();
        specification.clear();
        requestId = 0;
        status = 0;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("RESPONSE");
        buffer.addAttribute("Originator", originator.toString());
        buffer.addAttribute("Recipient", recipient.toString());
        buffer.addAttribute("Request Id", Long.toString(requestId));
        buffer.addAttribute("Status", status, ACTRES_REQ_STATUS.class);
        buffer.addBreak();
        buffer.addBuffer(specification);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        originator.read(stream);
        recipient.read(stream);
        requestId = Common.toUnsigned32(stream.readInt());
        status = stream.readInt();
        specification.read(stream);
    }
}
