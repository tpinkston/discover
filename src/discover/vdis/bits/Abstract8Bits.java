package discover.vdis.bits;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Binary;

/**
 * @author Tony Pinkston
 */
public abstract class Abstract8Bits extends AbstractBits {

    public static final int LENGTH = 1;

    private byte value = 0x00;

    public final byte get() {

        return value;
    }

    public final void set(byte value) {

        this.value = value;
    }

    @Override
    public final boolean isEmpty() {

        return (value == 0x00);
    }

    @Override
    public final int getBits() {

        return (value & 0xFF);
    }

    @Override
    public final String getBitString() {

        return Binary.toString8(value);
    }

    @Override
    public final boolean equals(Object object) {

        if ((object != null) &&(getClass() == object.getClass())) {

            Abstract8Bits bits = (Abstract8Bits)object;

            return (getBits() == bits.getBits());
        }

        return false;
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        value = stream.readByte();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeByte(value & 0xFF);
    }
}
