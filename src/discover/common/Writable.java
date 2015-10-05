/**
 * @author Tony Pinkston
 */
package discover.common;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Writable {

    /**
     * Writes object to output stream.
     * 
     * @param stream - {@link DataOutputStream}
     * @throws IOException
     */
    public void write(DataOutputStream stream) throws IOException;
}
