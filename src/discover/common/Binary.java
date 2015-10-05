/**
 * @author Tony Pinkston
 */
package discover.common;

public class Binary {
    
    private static final int SET_MASK[] = new int[] {
      
        0xFFFFFFFE, 0xFFFFFFFD, 0xFFFFFFFB, 0xFFFFFFF7,
        0xFFFFFFEF, 0xFFFFFFDF, 0xFFFFFFBF, 0xFFFFFF7F,
        0xFFFFFEFF, 0xFFFFFDFF, 0xFFFFFBFF, 0xFFFFF7FF,
        0xFFFFEFFF, 0xFFFFDFFF, 0xFFFFBFFF, 0xFFFF7FFF,
        0xFFFEFFFF, 0xFFFDFFFF, 0xFFFBFFFF, 0xFFF7FFFF,
        0xFFEFFFFF, 0xFFDFFFFF, 0xFFBFFFFF, 0xFF7FFFFF,
        0xFEFFFFFF, 0xFDFFFFFF, 0xFBFFFFFF, 0xF7FFFFFF,
        0xEFFFFFFF, 0xDFFFFFFF, 0xBFFFFFFF, 0x7FFFFFFF
    };

    public static int get1Bit(int index, byte bits) {
        
        checkIndex(index, 0, 7);

        return get1Bit(index, (bits & 0xFF));
    }

    public static int get1Bit(int index, short bits) {
        
        checkIndex(index, 0, 15);

        return get1Bit(index, (bits & 0xFFFF));
    }

    public static int get1Bit(int index, int bits) {
        
        checkIndex(index, 0, 31);

        return ((bits >> index) & 0x1);
    }

    public static int get2Bits(int index, byte bits) {
        
        checkIndex(index, 1, 7);

        return get2Bits(index, (bits & 0xFF));
    }

    public static int get2Bits(int index, short bits) {
        
        checkIndex(index, 1, 15);

        return get2Bits(index, (bits & 0xFFFF));
    }

    public static int get2Bits(int index, int bits) {
        
        checkIndex(index, 1, 31);

        return ((bits >> (index - 1)) & 0x3);
    }

    public static int get3Bits(int index, byte bits) {
        
        checkIndex(index, 2, 7);

        return get3Bits(index, (bits & 0xFF));
    }

    public static int get3Bits(int index, short bits) {
        
        checkIndex(index, 2, 15);

        return get3Bits(index, (bits & 0xFFFF));
    }

    public static int get3Bits(int index, int bits) {
        
        checkIndex(index, 2, 31);

        return ((bits >> (index - 2)) & 0x7);
    }

    public static int get4Bits(int index, byte bits) {
        
        checkIndex(index, 3, 7);
        
        return get4Bits(index, (bits & 0xFF));
    }

    public static int get4Bits(int index, short bits) {
        
        checkIndex(index, 3, 15);

        return get4Bits(index, (bits & 0xFFFF));
    }

    public static int get4Bits(int index, int bits) {
        
        checkIndex(index, 3, 31);

        return ((bits >> (index - 3)) & 0xF);
    }
    
    public static byte set1Bit(int index, byte bits, int value) {
        
        checkIndex(index, 0, 7);
        
        return (byte)set1Bit(index, (bits & 0xFF), value);
    }
    
    public static short set1Bit(int index, short bits, int value) {
        
        checkIndex(index, 0, 15);
        
        return (short)set1Bit(index, (bits & 0xFFFF), value);
    }
    
    public static int set1Bit(int index, int bits, int value) {
        
        checkValue(value, 1);
        checkIndex(index, 0, 31);

        if (value == 0) {
        
            return (bits & SET_MASK[index]);
        }
        else {
            
            return (bits | (1 << index));
        }
    }
    
    public static byte set2Bits(int index, byte bits, int value) {
        
        checkIndex(index, 1, 7);
        
        return (byte)set2Bits(index, (bits & 0xFF), value);
    }
    
    public static short set2Bits(int index, short bits, int value) {
        
        checkIndex(index, 1, 15);
        
        return (short)set2Bits(index, (bits & 0xFFFF), value);
    }
    
    public static int set2Bits(int index, int bits, int value) {
        
        checkValue(value, 3);
        checkIndex(index, 1, 31);

        bits = set1Bit(index, bits, get1Bit(1, value));
        bits = set1Bit(index - 1, bits, get1Bit(0, value));
        
        return bits;
    }
    
    public static byte set3Bits(int index, byte bits, int value) {
        
        checkIndex(index, 2, 7);
        
        return (byte)set3Bits(index, (bits & 0xFF), value);
    }
    
    public static short set3Bits(int index, short bits, int value) {
        
        checkIndex(index, 2, 15);
        
        return (short)set3Bits(index, (bits & 0xFFFF), value);
    }
    
    public static int set3Bits(int index, int bits, int value) {
        
        checkValue(value, 7);
        checkIndex(index, 2, 31);

        bits = set1Bit(index, bits, get1Bit(2, value));
        bits = set1Bit(index - 1, bits, get1Bit(1, value));
        bits = set1Bit(index - 2, bits, get1Bit(0, value));
        
        return bits;
    }
    
    public static byte set4Bits(int index, byte bits, int value) {
        
        checkIndex(index, 3, 7);
        
        return (byte)set4Bits(index, (bits & 0xFF), value);
    }
    
    public static short set4Bits(int index, short bits, int value) {
        
        checkIndex(index, 3, 15);
        
        return (short)set4Bits(index, (bits & 0xFFFF), value);
    }
    
    public static int set4Bits(int index, int bits, int value) {
        
        checkValue(value, 15);
        checkIndex(index, 3, 31);

        bits = set1Bit(index, bits, get1Bit(3, value));
        bits = set1Bit(index - 1, bits, get1Bit(2, value));
        bits = set1Bit(index - 2, bits, get1Bit(1, value));
        bits = set1Bit(index - 3, bits, get1Bit(0, value));
        
        return bits;
    }
    
    public static byte[] getBytes(int value) {
        
        byte array[] = new byte[] { 0, 0, 0, 0 };
        
        array[3] = (byte)(value & 0xFF);
        array[2] = (byte)((value >>> 8) & 0xFF);
        array[1] = (byte)((value >>> 16) & 0xFF);
        array[0] = (byte)((value >>> 24) & 0xFF);
        
        return array;
    }

    public static String toString8(byte bits) {
        
        String string = Integer.toBinaryString(Common.toUnsigned8(bits));

        while(string.length() < 8) {
            
            string = ("0" + string);
        }
        
        return string;
    }

    public static String toString16(short bits) {
        
        String string = Integer.toBinaryString(Common.toUnsigned16(bits));

        while(string.length() < 16) {
            
            string = ("0" + string);
        }
        
        return (string.substring(0, 8) + "-" + string.substring(8, 16));
    }

    public static String toString32(int bits) {
        
        String string = Integer.toBinaryString(bits);

        while(string.length() < 32) {
            
            string = ("0" + string);
        }
        
        return (string.substring(0, 8) + "-" + string.substring(8, 16) + "-" +
                string.substring(16, 24) + "-" + string.substring(24, 32));
    }

    public static String toString64(long bits) {
        
        String string = Long.toBinaryString(bits);

        while (string.length() < 64) {
            
            string = ("0" + string);
        }

        return string;
    }

    private static void checkIndex(int index, int minimum, int maximum) {
        
        if ((index < minimum) || (index > maximum)) {
            
            throw new IndexOutOfBoundsException(
                "Bit index out of range: " + index + 
                ", minimum: " + minimum + 
                ", maximum: " + maximum);
        }
    }

    private static void checkValue(int value, int maximum) {
        
        if ((value < 0) || (value > maximum)) {
            
            throw new IllegalArgumentException(
                "Value out of range: " + value + 
                ", minimum: 0, maximum: " + maximum);
        }
    }
}
