/**
 * @author Tony Pinkston
 */
package discover.common;

/**
 * No index checking is made on byte arrays to allow for 
 * {@link ArrayIndexOutOfBoundsException} 
 */
public class ByteArray {
    
    public static byte[] getArray(byte bytes[], int index, int length) {
        
        byte array[] = new byte[length];
        
        for(int i = 0; i < length; ++i) {
            
            array[i] = bytes[i + length];
        }
        
        return array;
    }
    
    public static int get8bits(byte bytes[], int index) {
        
        return (bytes[index] & 0x000000FF);
    }
    
    public static void set8Bits(byte bytes[], int index, int value) {
        
        bytes[index] = (byte)(value & 0xFF);
    }

    public static int get16bits(byte bytes[], int index) {
        
        int value = 0;
        
        value |= ((bytes[index + 1]) & 0xFF);
        value |= ((bytes[index] << 8) & 0xFF00);

        return value;
    }
    
    public static void set16Bits(byte bytes[], int index, int value) {
        
        bytes[index + 1] = (byte)(value & 0xFF);
        bytes[index] = (byte)((value >> 8) & 0xFF);
    }
    
    public static int get32bits(byte bytes[], int index) {

        long value = 0;
        
        value |= ((bytes[index + 3]) & 0xFF);
        value |= ((bytes[index + 2] << 8) & 0xFF00);
        value |= ((bytes[index + 1] << 16) & 0xFF0000);
        value |= ((bytes[index] << 24) & 0xFF000000);
        
        return (int)value;
    }
    
    public static void set32Bits(byte bytes[], int index, int value) {
        
        bytes[index + 3] = (byte)(value & 0xFF);
        bytes[index + 2] = (byte)((value >> 8) & 0xFF);
        bytes[index + 1] = (byte)((value >> 16) & 0xFF);
        bytes[index] = (byte)((value >> 24) & 0xFF);
    }

    public static long getU32bits(byte bytes[], int index) {

        long value = 0;
        
        value |= ((bytes[index + 3]) & 0xFF);
        value |= ((bytes[index + 2] << 8) & 0xFF00);
        value |= ((bytes[index + 1] << 16) & 0xFF0000);
        value |= ((bytes[index] << 24) & 0xFF000000);
        
        return value;
    }
    
    public static void setU32Bits(byte bytes[], int index, long value) {
        
        bytes[index + 3] = (byte)(value & 0xFF);
        bytes[index + 2] = (byte)((value >> 8) & 0xFF);
        bytes[index + 1] = (byte)((value >> 16) & 0xFF);
        bytes[index] = (byte)((value >> 24) & 0xFF);
    }
    
    public static long get64bits(byte bytes[], int index) {

        long value = 0;
        
        value |= (((long)(bytes[index + 7])) & 0xFFL);
        value |= (((long)(bytes[index + 6]) << 8) & 0xFF00L);
        value |= (((long)(bytes[index + 5]) << 16) & 0xFF0000L);
        value |= (((long)(bytes[index + 4]) << 24) & 0xFF000000L);
        value |= (((long)(bytes[index + 3]) << 32) & 0xFF00000000L);
        value |= (((long)(bytes[index + 2]) << 40) & 0xFF0000000000L);
        value |= (((long)(bytes[index + 1]) << 48) & 0xFF000000000000L);
        value |= (((long)(bytes[index]) << 56) & 0xFF00000000000000L);
        
        return value;
    }
    
    public static void set64Bits(byte bytes[], int index, long value) {
        
        bytes[index + 7] = (byte)(value & 0xFF);
        bytes[index + 6] = (byte)((value >> 8) & 0xFF);
        bytes[index + 5] = (byte)((value >> 16) & 0xFF);
        bytes[index + 4] = (byte)((value >> 24) & 0xFF);
        bytes[index + 3] = (byte)((value >> 32) & 0xFF);
        bytes[index + 2] = (byte)((value >> 40) & 0xFF);
        bytes[index + 1] = (byte)((value >> 48) & 0xFF);
        bytes[index] = (byte)((value >> 56) & 0xFF);
    }
}
