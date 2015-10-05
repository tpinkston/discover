/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Common;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.common.Timestamp;
import discover.vdis.enums.VDIS;

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

        this.originator.clear();
        this.recipient.clear();
        this.time.clear();
        this.requestId = 0;
        this.fixed = null;
        this.variable = null;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("DATA QUERY");
        buffer.addAttribute("Originator", this.originator.toString());
        buffer.addAttribute("Recipient", this.recipient.toString());
        buffer.addAttribute("Time Interval", this.time.toString());
        buffer.addAttribute("Request Id", this.requestId);
        buffer.addBreak();
        buffer.addTitle("FIXED DATUM IDS");

        if (this.fixed == null) {

            buffer.addItalic("None");
            buffer.addBreak();
        }
        else for(int i = 0, size = this.fixed.length; i < size; ++i) {

            buffer.addAttribute(
                Integer.toString(i + 1),
                this.fixed[i],
                VDIS.DATUM_IDS);
        }

        buffer.addBreak();
        buffer.addTitle("VARIABLE DATUM IDS");

        if (this.variable == null) {

            buffer.addItalic("None");
            buffer.addBreak();
        }
        else for(int i = 0, size = this.variable.length; i < size; ++i) {

            buffer.addAttribute(
                Integer.toString(i + 1),
                this.variable[i],
                VDIS.DATUM_IDS);
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        this.originator.read(stream);
        this.recipient.read(stream);
        this.requestId = Common.toUnsigned32(stream.readInt());
        this.time.read(stream);

        this.fixed = new int[stream.readInt()];
        this.variable = new int[stream.readInt()];

        for(int i = 0; i < this.fixed.length; ++i) {

            this.fixed[i] = stream.readInt();
        }

        for(int i = 0; i < this.variable.length; ++i) {

            this.variable[i] = stream.readInt();
        }
    }
}
