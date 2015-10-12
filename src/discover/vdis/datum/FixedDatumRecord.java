package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.Hexadecimal;
import discover.common.Readable;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class FixedDatumRecord implements Bufferable, Readable, Writable {

    private static final NumberFormat format = NumberFormat.getInstance();

    static {

        format.setGroupingUsed(true);
    }

    private int datumId = -1;
    private int datumValue = -1;

    public FixedDatumRecord() {

    }

    public FixedDatumRecord(DataInputStream stream) throws IOException {

        read(stream);
    }

    public int getDatumId() { return datumId; }
    public int getDatumValue() { return datumValue; }

    public void setDatumId(int id) {

        datumId = id;
    }

    public void setDatumValue(int value) {

        datumValue = value;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        StringBuffer value = new StringBuffer();

        value.append(format.format(datumValue));
        value.append(" (0x");
        value.append(Hexadecimal.toString32(datumValue));
        value.append(")");

        buffer.addAttribute("Datum Id", datumId, VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Value", value.toString());
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        datumId = stream.readInt();
        datumValue = stream.readInt();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeInt(datumId);
        stream.writeInt(datumValue);
    }
}
