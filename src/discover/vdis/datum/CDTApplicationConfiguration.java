/**
 * @author Tony Pinkston
 */
package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import discover.common.Hexadecimal;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.IPAddress;
import discover.vdis.enums.VDIS;

public class CDTApplicationConfiguration
    extends AbstractDatumRecord
    implements Writable {

    /** DID_CDT_APPLICATION_CONFIGURATION (not in V-DIS specification) */
    public static final int DATUM_ID = 400500;

    /** Length of entire record minus datum ID and length fields. */
    public static final int DATUM_LENGTH = (8128 - 8);

    /** Length of configuration file segment in bytes. */
    public static int CONFIGURATION_LENGTH = 1000;

    /** Length of padding in bytes. */
    public static int PADDING_LENGTH = 4;

    public final IPAddress receivingIPAddress = new IPAddress();
    public final byte configuration[] = new byte[CONFIGURATION_LENGTH];

    public CDTApplicationConfiguration() {

        this(DATUM_ID);

        Arrays.fill(this.configuration, 0, CONFIGURATION_LENGTH, (byte)(0));
    }

    public CDTApplicationConfiguration(int id) {

        super(id);

        Arrays.fill(this.configuration, 0, CONFIGURATION_LENGTH, (byte)(0));
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("Application Configuration");
        buffer.addAttribute("Datum Id", this.getDatumId(), VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Length (bytes)", super.getValueSizeInBytes());
        buffer.addAttribute("Receiver IP", this.receivingIPAddress.toString());
        buffer.addLabel("Datum Value");
        buffer.addBreak();

        Hexadecimal.toBuffer(buffer, " - ", 8, true, this.configuration);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        Arrays.fill(this.configuration, 0, CONFIGURATION_LENGTH, (byte)(0));

        super.read(stream); // Record length (record type already read)

        this.receivingIPAddress.read(stream);

        stream.read(this.configuration, 0, CONFIGURATION_LENGTH);

        stream.skipBytes(PADDING_LENGTH);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeInt(DATUM_ID); // 4 bytes
        stream.writeInt(DATUM_LENGTH); // 4 bytes

        this.receivingIPAddress.write(stream);

        stream.write(this.configuration, 0, CONFIGURATION_LENGTH);

        for(int i = 0; i < PADDING_LENGTH; ++i) {

            stream.writeByte(0);
        }
    }
}
