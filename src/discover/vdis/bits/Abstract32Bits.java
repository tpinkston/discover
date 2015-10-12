package discover.vdis.bits;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Binary;

/**
 * @author Tony Pinkston
 */
public abstract class Abstract32Bits extends AbstractBits {

    public static final int LENGTH = 4;

    private int value = 0x00;

    @Override
    public abstract Abstract32Bits clone();

    public int get() {

        return value;
    }

    public void set(int value) {

        this.value = value;
    }

    @Override
    public boolean isEmpty() {

        return (value == 0x00);
    }

    @Override
    public int getBits() {

        return value;
    }

    @Override
    public String getBitString() {

        return Binary.toString32(value);
    }

    @Override
    public boolean equals(Object object) {

        if ((object != null) &&(getClass() == object.getClass())) {

            Abstract32Bits bits = (Abstract32Bits)object;

            return (getBits() == bits.getBits());
        }

        return false;
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        value = stream.readInt();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeInt(value);
    }
}
