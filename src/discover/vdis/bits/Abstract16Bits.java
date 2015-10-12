package discover.vdis.bits;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Binary;

/**
 * @author Tony Pinkston
 */
public abstract class Abstract16Bits extends AbstractBits {

    public static final int LENGTH = 2;

    private short value = 0x00;

    public final short get() {

        return value;
    }

    public final void set(short value) {

        this.value = value;
    }

    @Override
    public final boolean isEmpty() {

        return (value == 0x00);
    }

    @Override
    public final int getBits() {

        return (value & 0xFFF);
    }

    @Override
    public final String getBitString() {

        return Binary.toString16(value);
    }

    @Override
    public final boolean equals(Object object) {

        if ((object != null) && (getClass() == object.getClass())) {

            Abstract16Bits bits = (Abstract16Bits)object;

            return (getBits() == bits.getBits());
        }

        return false;
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        value = stream.readShort();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeShort(value & 0xFFFF);
    }
}
