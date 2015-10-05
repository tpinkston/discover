/**
 * @author Tony Pinkston
 */
package discover.system;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import discover.Discover;
import discover.vdis.PDU;

public class PlaybackThread extends NetworkThread {

    private static final Logger logger = Discover.getLogger();

    private final List<PDU> list;
    private final PlaybackThreadListener listener;
    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;
    
    private Exception exception = null;
    private int index = 0;
    
    public PlaybackThread(
        String name,
        InetAddress address,
        PlaybackThreadListener listener,
        List<PDU> list,
        int port) throws Exception {
    
        super(name);
        
        this.address = address;
        this.port = port;
        this.list = list;
        this.listener = listener;
        this.socket = new MulticastSocket();
        this.socket.setBroadcast(true);
        this.socket.setReuseAddress(true);
    }
    
    public int getPort() { return this.port; }
    
    public Exception getException() { return this.exception; }

    public void setPaused(boolean paused) {
        
        if (super.isPaused() != paused) {
            
            super.setPaused(paused);
            this.sendStatus(PlaybackStatus.PAUSED);
        }
    }
    
    @Override
    public void run() {
        
        this.sendStatus(PlaybackStatus.STARTED);
        
        try {
            
            while(!super.isStopped()) {
                
                if (super.isPaused()) {
                    
                    sleep(500);
                }
                else {    
                    
                    PDU pdu = this.list.get(this.index);
                    
                    this.sendPDU(pdu);
                                        
                    this.sendStatus(PlaybackStatus.SENDING);
                    this.index++;
                    
                    if (this.index < this.list.size()) {

                        PDU next = this.list.get(this.index);
                        
                        long duration = (next.getTime() - pdu.getTime());
                        
                        if (duration < 1) {
                            
                            duration = 20;
                        }
                        
                        sleep(duration);
                    }
                    else {
                        
                        super.setStopped(true);
                    }
                }
            }
        }
        catch(Exception exception) {

            this.exception = exception;
            logger.log(Level.SEVERE, "Caught exception!", exception);
        }
        
        this.socket.close();
        this.sendStatus(PlaybackStatus.COMPLETE);
    }
    
    public void sendPDU(PDU pdu) throws IOException {
        
        this.sendBuffer(pdu.getData(), pdu.getLength());
    }

    public void sendBuffer(byte buffer[], int length) throws IOException {
        
        DatagramPacket packet = new DatagramPacket(
            buffer,
            length,
            this.address,
            this.port);
        
        this.socket.send(packet);
    }
    
    private void sendStatus(PlaybackStatus status) {

        float percent = ((float)this.index / (float)this.list.size());
        
        this.listener.sendStatus(status, (int)(percent * 100.0f));
    }
}
