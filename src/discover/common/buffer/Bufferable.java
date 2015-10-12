package discover.common.buffer;

/**
 * @author Tony Pinkston
 */
public interface Bufferable {

    /**
     * Stream object or record contents into buffer.
     *
     * @param buffer - {@link AbstractBuffer}
     */
    public void toBuffer(AbstractBuffer buffer);
}
