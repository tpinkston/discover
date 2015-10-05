/**
 * @author Tony Pinkston
 */
package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.buffer.Bufferable;

/**
 * Variable datum records only!
 */
public abstract class AbstractDatumRecord implements Bufferable, Readable {

    protected static final int ALIGNMENT_BOUNDRY_BITS = 64;
    protected static final int BYTES_PER_SEGMENT = 8;

    private int id = 0;
    private int length = 0;

    protected AbstractDatumRecord(int id) {

        this.id = id;
    }

    public final int getDatumId() { return this.id; }
    public final int getDatumLength() { return this.length; }

    public final int getValueSizeInBytes() {

        int segments = (this.length / ALIGNMENT_BOUNDRY_BITS);

        if ((this.length % ALIGNMENT_BOUNDRY_BITS) > 0) {

            segments++;
        }

        return (segments * BYTES_PER_SEGMENT);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.length = stream.readInt();
    }
}
