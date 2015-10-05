/**
 * @author Tony Pinkston
 */
package discover.common;

import java.io.DataInputStream;
import java.io.IOException;

public interface Readable {

    /**
     * Reads object from input stream.
     * 
     * @param stream - {@link DataInputStream}
     * @throws IOException
     */
    public void read(DataInputStream stream) throws IOException;
}
