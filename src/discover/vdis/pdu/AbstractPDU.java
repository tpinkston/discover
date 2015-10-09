/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;
import java.text.DateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.common.PDUHeader;

public abstract class AbstractPDU implements Bufferable, Readable {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractPDU.class);

    protected static final DateFormat format = DateFormat.getDateTimeInstance();

    private final PDUHeader header = new PDUHeader();

    protected AbstractPDU() {

    }

    public abstract void clear();

    public PDUHeader getHeader() {

        return this.header;
    }

    /**
     * Determines edit-ability of bytes or bits in the PDU.
     *
     * @param bit - Bit in specified byte (0 - 7) or -1 for the whole byte.
     * @param index - Specified byte index in PDU data.
     *
     * @return True if given bits can be modified by user.
     */
    public boolean isEditable(int bit, int index) {

        if (!isByteEditable(this.header.getType(), index)) {

            return false;
        }
        else if ((bit < 0) || (bit > 7)) {

            return false;
        }

        return true;
    }

    /**
     *
     * @param type - PDU type
     * @param index - Byte index
     *
     * @return True if byte is editable.
     */
    public static boolean isByteEditable(int type, int index) {

        // PDU header checks
        switch(index) {

            case 2: // PDU type
            case 3: // PDU family
            case 8: // PDU length (1 of 2)
            case 9: // PDU length (2 of 2)
            case 11: // Padding (zero)
                return false;
        }

        if (type == 1) {

            // PDU_TYPE_ENTITY_STATE
            switch(index) {
                case 19: // VPRecord count
                    return false;
            }
        }

        return true;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addBuffer(this.header);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.header.read(stream);
    }
}
