/**
 * @author Tony Pinkston
 */
package discover.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import discover.Discover;

public class Common {

    // RADIX:
    public static final int BIN = 2;
    public static final int DEC = 10;
    public static final int HEX = 16;

    // SIZE:
    public static final int SIZE8 = 8;
    public static final int SIZE16 = 16;
    public static final int SIZE32 = 32;
    public static final int SIZE64 = 64;

    // RADIANS:
    public static final double HALF_PI = (Math.PI / 2.0);
    public static final double TWO_PI = (Math.PI * 2.0);

    private static final Logger logger = Discover.getLogger();

    /**
     * Gets integer value from unsigned byte value.
     * 
     * @param value - Byte value (-128 to 127).
     * 
     * @return Positive integer from 0 to 255.
     */
    public static int toUnsigned8(byte value) {
        
        return (0xFF & value);
    }

    /**
     * Gets integer value from unsigned short value.
     * 
     * @param value - Short value (-32768 to 32767).
     * 
     * @return Positive integer from 0 to 65535.
     */
    public static int toUnsigned16(short value) {
        
        return (0xFFFF & value);
    }

    /**
     * Gets long value from unsigned int value.
     * 
     * @param value - Integer value (-2147483648 to 2147483647).
     * 
     * @return Positive long from 0 to 4294967295.
     */
    public static long toUnsigned32(int value) {
        
        return ((value < 0) ? (0x100000000L + value) : value);
    }

    /**
     * @param radians = Angle in radians.
     */
    public static double clampToPI(double radians) {

        radians = clampTo2PI(radians);

        if ((radians >= (-Math.PI)) && (radians <= Math.PI)) {

            return radians;
        }
        else if (radians > 0.0) {

            return (radians - TWO_PI);
        }
        else {
            
            return (radians + TWO_PI);
        }
    }

    /**
     * @param radians = Angle in radians.
     */
    public static double clampTo2PI(double radians) {

        if ((radians >= (-TWO_PI)) && (radians <= TWO_PI)) {

            return radians;
        }
        else {
            
            return (radians % TWO_PI);
        }
    }

    /**
     * @param radians = Angle in radians.
     */
    public static double clampToHalfPI(double radians) {

        radians = clampToPI(radians);

        if ((radians >= (-HALF_PI)) && (radians <= HALF_PI)) {

            return radians;
        }
        else if (radians > 0) {

            return (Math.PI - radians);
        }
        else {
            
            return (-(Math.PI + radians));
        }
    }
    
    /**
     * @param number - Double, Float, Long, Integer, Short or Byte object.
     * 
     * @return Array of bytes representing bit patterns of number as it
     *         would be in data stream.
     */
    public static byte[] getByteArray(Number number) {
        
        try {
            
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(bytes);
            
            if (number instanceof Double) {
                
                stream.writeDouble(((Double)number).doubleValue());
            }
            else if (number instanceof Float) {
                
                stream.writeFloat(((Float)number).floatValue());
            }
            else if (number instanceof Long) {
                
                stream.writeLong(((Long)number).longValue());
            }
            else if (number instanceof Integer) {
                
                stream.writeInt(((Integer)number).intValue());
            }
            else if (number instanceof Short) {
                
                stream.writeShort(((Short)number).shortValue());
            }
            else if (number instanceof Byte) {
                
                stream.writeByte(((Byte)number).byteValue());
            }
            
            bytes.close();
            
            return bytes.toByteArray();
        }
        catch(IOException exception) {
            
            logger.log(Level.SEVERE, "Caught exception!", exception);
            return null;
        }
    }

    /**
     * Parses input string to get number value.
     * 
     * @param value - Number in string form (binary, hexadecimal or decimal).
     * @param size - Expected size of number (8, 16, 32 or 64 bits).
     * @param radix - Base (2, 10 or 16).
     * @param floating - True if number is floating point (32 or 64 bit only).
     * 
     * @return  Byte, Short, Integer, Long, Float or Double object.
     * 
     * @throws NumberFormatException
     */
    public static Number getNumber(
        String value, 
        int size,
        int radix,
        boolean floating) throws NumberFormatException {
        
        Number number = null;
        
        if ((radix != BIN) && (radix != HEX) && (radix != DEC)) {
            
            logger.severe("Invalid radix: " + radix);
        }
        else if (floating) {

            if (radix == DEC) { 

                if (size == SIZE64) {
                    
                    number = Double.parseDouble(value);
                }
                else {
                    
                    number = Float.parseFloat(value);
                }
            }
            else {

                Long parsed = Long.parseLong(value, radix);
                
                if (parsed != null) {
                    
                    if (size == SIZE64) {
                        
                        number = Double.longBitsToDouble(parsed);
                    }
                    else {
                        
                        number = Float.intBitsToFloat(parsed.intValue());
                    }
                }
            }
        }
        else {
            
            Long parsed = Long.parseLong(value, radix);

            switch(size) {
            
                case SIZE8:
                    number = parsed.byteValue();
                    break;
                case SIZE16:
                    number = parsed.shortValue();
                    break;
                case SIZE32:
                    number = parsed.intValue();
                    break;
                case SIZE64:
                    number = parsed;
                    break;
                default:
                    logger.severe("Invalid size: " + size);
                    
            }
        }
        
        return number;
    }
}
