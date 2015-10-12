package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.common.Timestamp;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class DataQuery extends AbstractPDU {

    private EntityId originator = new EntityId();
    private EntityId recipient = new EntityId();
    private Timestamp time = new Timestamp();
    private int fixed[] = null;
    private int variable[] = null;
    private long requestId = 0;

    public DataQuery() {

    }

    @Override
    public void clear() {

        originator.clear();
        recipient.clear();
        time.clear();
        requestId = 0;
        fixed = null;
        variable = null;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("DATA QUERY");
        buffer.addAttribute("Originator", originator.toString());
        buffer.addAttribute("Recipient", recipient.toString());
        buffer.addAttribute("Time Interval", time.toString());
        buffer.addAttribute("Request Id", requestId);
        buffer.addBreak();
        buffer.addTitle("FIXED DATUM IDS");

        if (fixed == null) {

            buffer.addItalic("None");
            buffer.addBreak();
        }
        else for(int i = 0, size = fixed.length; i < size; ++i) {

            buffer.addAttribute(
                Integer.toString(i + 1),
                fixed[i],
                VDIS.DATUM_IDS);
        }

        buffer.addBreak();
        buffer.addTitle("VARIABLE DATUM IDS");

        if (variable == null) {

            buffer.addItalic("None");
            buffer.addBreak();
        }
        else for(int i = 0, size = variable.length; i < size; ++i) {

            buffer.addAttribute(
                Integer.toString(i + 1),
                variable[i],
                VDIS.DATUM_IDS);
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        originator.read(stream);
        recipient.read(stream);
        requestId = Common.toUnsigned32(stream.readInt());
        time.read(stream);

        fixed = new int[stream.readInt()];
        variable = new int[stream.readInt()];

        for(int i = 0; i < fixed.length; ++i) {

            fixed[i] = stream.readInt();
        }

        for(int i = 0; i < variable.length; ++i) {

            variable[i] = stream.readInt();
        }
    }
}
