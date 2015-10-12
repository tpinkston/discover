package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.datum.DatumSpecificationRecord;

/**
 * @author Tony Pinkston
 */
public class Data extends AbstractPDU {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private DatumSpecificationRecord specification = new DatumSpecificationRecord();
    private long requestId = 0;

    public Data() {

    }

    @Override
    public void clear() {

        originator.clear();
        recipient.clear();
        specification.clear();
        requestId = 0;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("DATA");
        buffer.addAttribute("Originator", originator.toString());
        buffer.addAttribute("Recipient", recipient.toString());
        buffer.addAttribute("Request Id", requestId);
        buffer.addBreak();
        buffer.addBuffer(specification);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        originator.read(stream);
        recipient.read(stream);
        requestId = Common.toUnsigned32(stream.readInt());

        stream.skipBytes(4); // 4 bytes padding

        specification.read(stream);
    }
}
