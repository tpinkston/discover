package discover.system;

/**
 * @author Tony Pinkston
 */
public interface PlaybackThreadListener {

    /**
     * @param status - {@link PlaybackStatus}
     * @param percent - Percent complete
     */
    public void sendStatus(PlaybackStatus status, int percent);
}
