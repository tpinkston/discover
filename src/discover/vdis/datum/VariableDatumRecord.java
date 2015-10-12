package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Hexadecimal;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class VariableDatumRecord extends AbstractDatumRecord {

    private byte[] datumValue = null;

    public VariableDatumRecord(int id) {

        super(id);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Datum Id", getDatumId(), VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Length (bytes)", getValueSizeInBytes());
        buffer.addLabel("Datum Value");

        if (datumValue == null) {

            buffer.addItalic("N/A");
            buffer.addBreak();
        }
        else {

            buffer.addBreak();
            Hexadecimal.toBuffer(buffer, " - ", 4, false, datumValue);
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)

        final int length = getValueSizeInBytes();

        if (length > 0) {

            datumValue = new byte[length];

            stream.readFully(datumValue);
        }
    }
}
