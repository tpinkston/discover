package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class ForceIdentification extends AbstractDatumRecord {

    private int forceId = 0;
    private String name = null;

    public ForceIdentification(int id) {

        super(id);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Datum Id", getDatumId());
        buffer.addAttribute("Datum Length (bytes)", getValueSizeInBytes());
        buffer.addAttribute("Force Id", forceId, VDIS.FORCE_ID);
        buffer.addAttribute("Name", name);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)

        forceId = stream.readUnsignedShort();

        stream.skipBytes(2); // padding

        byte[] array = new byte[getValueSizeInBytes() - 4];

        stream.read(array, 0, array.length);

        name = new String(array);
        name = name.trim();
    }
}
