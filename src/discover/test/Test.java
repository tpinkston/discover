/**
 * @author Tony Pinkston
 */
package discover.test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

import discover.common.ByteArray;
import discover.vdis.types.EntityType;
import discover.vdis.types.EntityTypes;

public class Test {

    public static void test(String test) {
        
        if (test.equalsIgnoreCase("default")) {
            
        }
        else if (test.equalsIgnoreCase("byte_array")) {
            
            testByteArray();
        }
        else if (test.equalsIgnoreCase("entity_types")) {
            
            testEntityTypes();
        }
        else {
            
            System.err.println("ERROR: Invalid test name: " + test);
        }
    }
    
    private static void testByteArray() {
        
        byte data[] = { 0, 1, -45, -1, 13, -88, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        
        System.out.println();
        System.out.println(Arrays.toString(data));
        System.out.println();
        System.out.println("8 bits:");
        System.out.println(ByteArray.get8bits(data, 2));
        System.out.println(ByteArray.get8bits(data, 3));
        System.out.println();
        System.out.println("16 bits:");
        System.out.println(ByteArray.get16bits(data, 2));
        System.out.println(ByteArray.get16bits(data, 3));
        System.out.println();
        System.out.println("32 bits:");
        System.out.println(ByteArray.get32bits(data, 2));
        System.out.println(ByteArray.get32bits(data, 3));
        System.out.println();
        System.out.println("U32 bits:");
        System.out.println(ByteArray.getU32bits(data, 2));
        System.out.println(ByteArray.getU32bits(data, 3));
        System.out.println();
        System.out.println("64 bits:");
        System.out.println(ByteArray.get64bits(data, 0));
        System.out.println(ByteArray.get64bits(data, 5));
        
        ByteArray.set8Bits(data, 2, 12);
        ByteArray.set8Bits(data, 3, 13);
        ByteArray.set8Bits(data, 4, 14);

        System.out.println(Arrays.toString(data));
        
        ByteArray.set16Bits(data, 2, 5);
        ByteArray.set16Bits(data, 4, 6);
        ByteArray.set16Bits(data, 6, 7);

        System.out.println(Arrays.toString(data));
        
        ByteArray.set32Bits(data, 0, 1);
        ByteArray.set32Bits(data, 4, 2);
        ByteArray.set32Bits(data, 8, 3);
        ByteArray.set32Bits(data, 12, 4);

        System.out.println(Arrays.toString(data));
        
        ByteArray.set64Bits(data, 0, 12345678901234L);
        ByteArray.set64Bits(data, 8, 12345678901234L);

        System.out.println(Arrays.toString(data));
        System.out.println(Arrays.toString(ByteArray.getArray(data, 8, 8)));
    }
    
    private static void testEntityTypes() {
        
        byte bytes[] = { 6, 0, 0, 0, 0, 13, 5, 2 };
        ByteArrayInputStream stream1 = new ByteArrayInputStream(bytes);
        DataInputStream stream2 = new DataInputStream(stream1);
        
        try {
            
            EntityType type = EntityTypes.read(stream2);
            
            System.out.println(type.name);
            System.out.println(type.septuple.string);
            System.out.println(type.description);
            System.out.println(type.alternate);

            stream2.close();
        }
        catch(IOException exception) {

            exception.printStackTrace();
        }
    }
}
