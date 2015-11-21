package discover.test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import discover.vdis.types.EntityType;
import discover.vdis.types.EntityTypes;

public class TestEntityTypes {

    @Test
    public void test() {

        EntityTypes.load();

        byte arrays[][] = {
            { 1, 1, 0, -31, 1, 1, 1, 0 },
            { 1, 1, 0, -31, 1, 1, 2, 0 },
            { 1, 1, 0, -31, 1, 1, 3, 0 },
            { 1, 1, 0, -31, 1, 1, 4, 0 },
            { 1, 1, 0, -31, 1, 1, 5, 0 },
            { 1, 1, 0, -31, 1, 1, 6, 0 },
            { 1, 1, 0, -31, 1, 1, 7, 0 },
            { 1, 1, 0, -31, 1, 1, 8, 0 },
            { 1, 1, 0, -31, 1, 1, 9, 0 }
        };

        for(byte[] array : arrays) {

            ByteArrayInputStream bytes = new ByteArrayInputStream(array);
            DataInputStream stream = new DataInputStream(bytes);

            try {

                EntityType type = EntityTypes.read(stream);

                System.out.println();
                System.out.println("BYTE ARRAY: " + Arrays.toString(array));
                System.out.println(type.name);
                System.out.println(type.septuple.string);
                System.out.println(type.description);

                stream.close();
            }
            catch (IOException exception) {

                exception.printStackTrace();
            }
        }
    }
}
