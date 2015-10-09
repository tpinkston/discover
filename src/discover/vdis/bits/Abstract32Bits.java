/**
 * @author Tony Pinkston
 */
package discover.vdis.bits;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Binary;

public abstract class Abstract32Bits extends AbstractBits {

    public static final int LENGTH = 4;

    private int value = 0x00;

    @Override
    public abstract Abstract32Bits clone();

    public int get() {

        return this.value;
    }

    public void set(int value) {

        this.value = value;
    }

    @Override
    public boolean isEmpty() {

        return (this.value == 0x00);
    }

    @Override
    public int getBits() {

        return this.value;
    }

    @Override
    public String getBitString() {

        return Binary.toString32(this.value);
    }

    @Override
    public boolean equals(Object object) {

        if ((object != null) &&(this.getClass() == object.getClass())) {

            Abstract32Bits bits = (Abstract32Bits)object;

            return (this.getBits() == bits.getBits());
        }

        return false;
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.value = stream.readInt();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeInt(this.value);
    }
}
