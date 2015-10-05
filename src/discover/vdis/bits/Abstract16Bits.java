/**
 * @author Tony Pinkston
 */
package discover.vdis.bits;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import discover.Discover;
import discover.common.Binary;

public abstract class Abstract16Bits extends AbstractBits {

    public static final int LENGTH = 2;

    protected static final Logger logger = Discover.getLogger();

    private short value = 0x00;

    public final short get() {

        return this.value;
    }

    public final void set(short value) {

        this.value = value;
    }

    @Override
    public final boolean isEmpty() {

        return (this.value == 0x00);
    }

    @Override
    public final int getBits() {

        return (this.value & 0xFFF);
    }

    @Override
    public final String getBitString() {

        return Binary.toString16(this.value);
    }

    @Override
    public final boolean equals(Object object) {

        if ((object != null) && (this.getClass() == object.getClass())) {

            Abstract16Bits bits = (Abstract16Bits)object;

            return (this.getBits() == bits.getBits());
        }

        return false;
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.value = stream.readShort();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeShort(this.value & 0xFFFF);
    }
}
