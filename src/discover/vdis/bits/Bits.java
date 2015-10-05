/**
 * @author Tony Pinkston
 */
package discover.vdis.bits;

import discover.vdis.enums.VDIS;
import discover.vdis.enums.VDIS.Handle;

public class Bits {
    
    /** Label for value of bit(s). */
    public final String label;

    /** Starting index for bit(s). */
    public final int bit;
    
    /** Number of bits needed for value. */
    public final int count;
    
    /** Determine whether default (0) values are visible. */
    public final boolean zeroVisible;
    
    /** Enumeration handle if applicable to value of bit(s). */
    public final Handle handle;
    
    private Bits(
        String label, 
        int bit, 
        int count, 
        boolean zeroVisible, 
        Handle handle) {
        
        this.label = label;
        this.bit = bit;
        this.count = count;
        this.zeroVisible = zeroVisible;
        this.handle = handle;
    }

    public static Bits getOnOff(
        String label, 
        int bit) {
        
        return new Bits(label, bit, 1, false, VDIS.getHandle(VDIS.ONOFF));
    }

    public static Bits getYesNo(
        String label, 
        int bit) {
        
        return new Bits(label, bit, 1, false, VDIS.getHandle(VDIS.YESNO));
    }

    public static Bits getGenericPresence(
        String label, 
        int bit) {
        
        return new Bits(label, bit, 1, false, VDIS.getHandle(VDIS.GENERIC_PRESENCE));
    }

    public static Bits get1(
        String label, 
        int bit,
        boolean zeroVisible,
        Handle handle) {
        
        return new Bits(label, bit, 1, zeroVisible, handle);
    }

    public static Bits get2(
        String label, 
        int bit,
        boolean zeroVisible,
        Handle handle) {
        
        return new Bits(label, bit, 2, zeroVisible, handle);
    }

    public static Bits get3(
        String label, 
        int bit,
        boolean zeroVisible,
        Handle handle) {
        
        return new Bits(label, bit, 3, zeroVisible, handle);
    }

    public static Bits get4(
        String label, 
        int bit,
        boolean zeroVisible,
        Handle handle) {
        
        return new Bits(label, bit, 4, zeroVisible, handle);
    }
}
