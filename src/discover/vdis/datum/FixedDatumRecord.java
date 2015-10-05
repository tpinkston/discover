/**
 * @author Tony Pinkston
 */
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

        this.read(stream);
    }

    public int getDatumId() { return this.datumId; }
    public int getDatumValue() { return this.datumValue; }

    public void setDatumId(int id) {

        this.datumId = id;
    }

    public void setDatumValue(int value) {

        this.datumValue = value;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        StringBuffer value = new StringBuffer();

        value.append(format.format(this.datumValue));
        value.append(" (0x");
        value.append(Hexadecimal.toString32(this.datumValue));
        value.append(")");

        buffer.addAttribute("Datum Id", this.datumId, VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Value", value.toString());
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.datumId = stream.readInt();
        this.datumValue = stream.readInt();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeInt(this.datumId);
        stream.writeInt(this.datumValue);
    }
}
