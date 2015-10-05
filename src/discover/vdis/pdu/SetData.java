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

public class SetData extends AbstractPDU {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private DatumSpecificationRecord specification = new DatumSpecificationRecord();
    private long requestId = -1;

    public SetData() {

    }
    
    @Override
    public void clear() {
        
        this.originator.clear();
        this.recipient.clear();
        this.requestId = -1;
        this.specification.clear();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("DATA");
        buffer.addAttribute("Originator", this.originator.toString());
        buffer.addAttribute("Recipient", this.recipient.toString());
        buffer.addAttribute("Request Id", this.requestId);
        buffer.addBreak();
        buffer.addBuffer(this.specification);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)
        
        this.originator.read(stream);
        this.recipient.read(stream);
        this.requestId = Common.toUnsigned32(stream.readInt());
        
        stream.skipBytes(4); // 4 bytes padding
        
        this.specification.read(stream);
    }
}
