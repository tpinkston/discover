package discover.vdis.pdu;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.datum.DatumSpecificationRecord;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class ActionRequest extends AbstractPDU implements Writable {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private DatumSpecificationRecord specification = new DatumSpecificationRecord();
    private int actionId = 0;
    private long requestId = 0;

    public ActionRequest() {

    }

    public long getRequestId() { return requestId; }

    public void setRequestId(long id) { requestId = id; }

    public int getActionId() { return actionId; }

    public void setActionId(int id) { actionId = id; }

    public EntityId getOriginator() { return originator; }

    public EntityId getRecipient() { return recipient; }

    public DatumSpecificationRecord getSpecification() {

        return specification;
    }

    @Override
    public void clear() {

        originator.clear();
        recipient.clear();
        requestId = 0;
        actionId = 0;
        specification.clear();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("REQUEST");
        buffer.addAttribute("Originator", originator.toString());
        buffer.addAttribute("Recipient", recipient.toString());
        buffer.addAttribute("Request Id", requestId);
        buffer.addAttribute("Action Id", actionId, VDIS.ACTREQ_ACTION_IDS);
        buffer.addBreak();
        buffer.addBuffer(specification);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        originator.read(stream); // 6 bytes
        recipient.read(stream); // 6 bytes
        requestId = Common.toUnsigned32(stream.readInt()); // 4 bytes
        actionId = stream.readInt(); // 4 bytes
        specification.read(stream);
    }

    public byte[] write() throws IOException {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(array);

        this.write(stream);

        byte bytes[] = array.toByteArray();

        stream.close();

        return bytes;
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.getHeader().write(stream);

        originator.write(stream);
        recipient.write(stream);

        stream.writeInt((int)requestId);
        stream.writeInt(actionId);

        specification.write(stream);
    }
}
