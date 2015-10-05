/**
 * @author Tony Pinkston
 */
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

public class ActionRequest extends AbstractPDU implements Writable {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private DatumSpecificationRecord specification = new DatumSpecificationRecord();
    private int actionId = 0;
    private long requestId = 0;
    
    public ActionRequest() {
        
    }
    
    public long getRequestId() { return this.requestId; }
    
    public void setRequestId(long id) { this.requestId = id; }

    public int getActionId() { return this.actionId; }

    public void setActionId(int id) { this.actionId = id; }

    public EntityId getOriginator() { return this.originator; }

    public EntityId getRecipient() { return this.recipient; }
    
    public DatumSpecificationRecord getSpecification() {
        
        return this.specification;
    }
    
    @Override
    public void clear() {
        
        this.originator.clear();
        this.recipient.clear();
        this.requestId = 0;
        this.actionId = 0;
        this.specification.clear();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("REQUEST");
        buffer.addAttribute("Originator", this.originator.toString());
        buffer.addAttribute("Recipient", this.recipient.toString());
        buffer.addAttribute("Request Id", this.requestId);
        buffer.addAttribute("Action Id", this.actionId, VDIS.ACTREQ_ACTION_IDS);
        buffer.addBreak();
        buffer.addBuffer(this.specification);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)
        
        this.originator.read(stream); // 6 bytes
        this.recipient.read(stream); // 6 bytes
        this.requestId = Common.toUnsigned32(stream.readInt()); // 4 bytes
        this.actionId = stream.readInt(); // 4 bytes
        this.specification.read(stream);
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
        
        this.originator.write(stream);
        this.recipient.write(stream);
        
        stream.writeInt((int)this.requestId);
        stream.writeInt(this.actionId);
        
        this.specification.write(stream);
    }
}
