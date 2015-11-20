package discover.vdis.bits;

import discover.vdis.enums.ON_OFF;
import discover.vdis.enums.PRESENCE;
import discover.vdis.enums.Value;
import discover.vdis.enums.YES_NO;

/**
 * @author Tony Pinkston
 */
public class Bits {

    /** Label for value of bit(s). */
    public final String label;

    /** Starting index for bit(s). */
    public final int bit;

    /** Number of bits needed for value. */
    public final int count;

    /** Determine whether default (0) values are visible. */
    public final boolean zeroVisible;

    /** Enumeration class if applicable to value of bit(s). */
    public final Class<? extends Value> enumeration;

    private Bits(
            String label,
            int bit,
            int count,
            boolean zeroVisible,
            Class<? extends Value> enumeration) {

        this.label = label;
        this.bit = bit;
        this.count = count;
        this.zeroVisible = zeroVisible;
        this.enumeration = enumeration;
    }

    public static Bits getOnOff(String label, int bit) {

        return new Bits(label, bit, 1, false, ON_OFF.class);
    }

    public static Bits getYesNo(String label, int bit) {

        return new Bits(label, bit, 1, false, YES_NO.class);
    }

    public static Bits getGenericPresence(String label, int bit) {

        return new Bits(label, bit, 1, false, PRESENCE.class);
    }

    public static Bits get1(
        String label,
        int bit,
        boolean zeroVisible,
        Class<? extends Value> enumeration) {

        return new Bits(label, bit, 1, zeroVisible, enumeration);
    }

    public static Bits get2(
        String label,
        int bit,
        boolean zeroVisible,
        Class<? extends Value> enumeration) {

        return new Bits(label, bit, 2, zeroVisible, enumeration);
    }

    public static Bits get3(
        String label,
        int bit,
        boolean zeroVisible,
        Class<? extends Value> enumeration) {

        return new Bits(label, bit, 3, zeroVisible, enumeration);
    }

    public static Bits get4(
        String label,
        int bit,
        boolean zeroVisible,
        Class<? extends Value> enumeration) {

        return new Bits(label, bit, 4, zeroVisible, enumeration);
    }
}
