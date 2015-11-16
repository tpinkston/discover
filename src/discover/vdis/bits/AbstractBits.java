package discover.vdis.bits;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.Binary;
import discover.common.Readable;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.Enumerations;

/**
 * @author Tony Pinkston
 */
public abstract class AbstractBits implements Bufferable, Cloneable, Readable, Writable {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractBits.class);

    /**
     * @return True if all bits are zero.
     */
    public abstract boolean isEmpty();

    /**
     * @return Bits as 32bit integer.
     */
    public abstract int getBits();

    /**
     * @return Bits in string binary form.
     */
    public abstract String getBitString();

    /**
     * @return {@link AbstractBits}
     */
    @Override
    public abstract AbstractBits clone();

    /**
     * Implemented by child class if needed.
     *
     * @return List of possible values represented by this bit set.
     */
    public List<Bits> getValues() {

        return Collections.emptyList();
    }

    /**
     * @return True if the binary bit string gets included in buffer.
     * @see toBuffer()
     */
    public boolean bitsInBuffer() {

        return true;
    }

    public int getValue(Bits bits) {

        final int bitset = getBits();
        int value = 0x00;

        switch(bits.count) {

            case 1:
                value = Binary.get1Bit(bits.bit, bitset);
                break;
            case 2:
                value = Binary.get2Bits(bits.bit, bitset);
                break;
            case 3:
                value = Binary.get3Bits(bits.bit, bitset);
                break;
            case 4:
                value = Binary.get4Bits(bits.bit, bitset);
                break;
        }

        return value;
    }

    @Override
    public String toString() {

        return getBitString();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        if (bitsInBuffer()) {

            buffer.addText("Bits: " + getBitString());
            buffer.addBreak();
        }

        for(Bits bits : getValues()) {

            final int value = getValue(bits);

            if (bits.enumeration == null) {

                buffer.addAttribute(
                    bits.label,
                    "(" + value + ")");
            }
            else if ((value != 0) || bits.zeroVisible) {

                String description = Enumerations.getDescription(
                    value,
                    bits.enumeration);

                buffer.addAttribute(bits.label, description);
            }
        }
    }
}
