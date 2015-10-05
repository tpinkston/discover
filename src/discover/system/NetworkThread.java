/**
 * @author Tony Pinkston
 */
package discover.system;

public abstract class NetworkThread extends Thread {

    private boolean paused = false;
    private boolean stopped = false;

    protected NetworkThread(String name) {

        super(name);
    }

    public final synchronized boolean isPaused() { return this.paused; }
    public final synchronized boolean isStopped() { return this.stopped; }

    public synchronized void startPaused() {

        this.paused = true;

        super.start();
    }

    public synchronized void setPaused(boolean paused) {

        this.paused = paused;
    }

    public synchronized void setStopped(boolean stopped) {

        this.stopped = stopped;
    }
}
