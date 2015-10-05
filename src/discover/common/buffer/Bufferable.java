/**
 * @author Tony Pinkston
 */
package discover.common.buffer;

public interface Bufferable {

    /**
     * Stream object or record contents into buffer.
     * 
     * @param buffer - {@link AbstractBuffer}
     */
    public void toBuffer(AbstractBuffer buffer);
}
