package discover.common;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Tony Pinkston
 */
public interface Writable {

    /**
     * Writes object to output stream.
     *
     * @param stream - {@link DataOutputStream}
     * @throws IOException
     */
    public void write(DataOutputStream stream) throws IOException;
}
