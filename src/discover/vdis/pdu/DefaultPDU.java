/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;

public final class DefaultPDU extends AbstractPDU {

    public DefaultPDU() {
        
    }

    @Override
    public void clear() {
        
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);
        
        buffer.addBreak();
        buffer.addItalic("Content decoding for this PDU is not yet supported.");
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)
        
        stream.skip(stream.available());
    }
}
