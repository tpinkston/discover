package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Binary;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.enums.VP_RECORD_TYPE;

/**
 * @author Tony Pinkston
 */
public class DefaultVPR extends AbstractVPRecord {

    public static final int LENGTH = 16;

    protected final byte bytes[] = new byte[LENGTH];

    public DefaultVPR(int type) {

        super(type);

        setRecordType(type);
    }

    @Override
    public int getLength() {

        return LENGTH;
    }

    @Override
    public void setRecordType(int type) {

        super.setRecordType(type);

        bytes[0] = (byte)type;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VP_RECORD_TYPE.getValue(getRecordType()).getDescription();

        buffer.addTitle(title.toUpperCase());

        int count = 0;

        for(int i = 0; i < 8; ++i) {

            buffer.addText(Binary.toString8(bytes[count]));
            buffer.addText(" ");
            buffer.addText(Binary.toString8(bytes[count + 1]));
            buffer.addBreak();

            count += 2;
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        /* Assume the first byte (the record type) has been read. */
        for(int i = 1; i < 16; ++i) {

            bytes[i] = stream.readByte();
        }
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        for(int i = 0; i < 16; ++i) {

            stream.writeByte(bytes[i]);
        }
    }
}
