/**
 * @author Tony Pinkston
 */
package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.IPAddress;
import discover.vdis.enums.VDIS;

public class CDTOneSAFControllerKillRecord
    extends AbstractDatumRecord
    implements Writable {

    /** DID_CDT_CONTROLLER_KILL (not in V-DIS specification) */
    public static final int DATUM_ID = 986882;

    /** Length of entire record minus datum ID and length fields. */
    public static final int DATUM_LENGTH = 128;

    public final IPAddress address = new IPAddress();
    public int node = 0;
    public int previousStatus = 0; // // 32-bit enumeration (CDT_ONESAF_STATUS)
    public int newStatus = 0; // // 32-bit enumeration (CDT_ONESAF_STATUS)

    public CDTOneSAFControllerKillRecord() {

        this(DATUM_ID);
    }

    public CDTOneSAFControllerKillRecord(int id) {

        super(id);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("OneSAF Controller Kill");
        buffer.addAttribute("Datum Id", super.getDatumId(), VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Length (bytes)", Integer.toString(super.getValueSizeInBytes()));
        buffer.addAttribute("Server IP", this.address.toString());
        buffer.addAttribute("OneSAF Node", this.node);
        buffer.addAttribute("Previous Status", this.previousStatus, VDIS.CDT_ONESAF_STATUS);
        buffer.addAttribute("New Status", this.newStatus, VDIS.CDT_ONESAF_STATUS);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)

        this.address.read(stream);
        this.node = stream.readInt();
        this.previousStatus = stream.readInt();
        this.newStatus = stream.readInt();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeInt(DATUM_ID); // 4 bytes
        stream.writeInt(DATUM_LENGTH); // 4 bytes

        this.address.write(stream);
        stream.writeInt(this.node);
        stream.writeInt(this.previousStatus);
        stream.writeInt(this.newStatus);
    }
}
