/**
 * @author Tony Pinkston
 */
package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Hexadecimal;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.enums.VDIS;

public class VariableDatumRecord extends AbstractDatumRecord {

    private byte[] datumValue = null;

    public VariableDatumRecord(int id) {
        
        super(id);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Datum Id", super.getDatumId(), VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Length (bytes)", super.getValueSizeInBytes());
        buffer.addLabel("Datum Value");
        
        if (this.datumValue == null) {
            
            buffer.addItalic("N/A");
            buffer.addBreak();
        }
        else {
            
            buffer.addBreak();
            Hexadecimal.toBuffer(buffer, " - ", 4, false, this.datumValue);
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)

        final int length = super.getValueSizeInBytes();
        
        if (length > 0) {
            
            this.datumValue = new byte[super.getValueSizeInBytes()];

            stream.readFully(this.datumValue);
        }
    }
}
