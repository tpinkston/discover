package discover.system;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;

import discover.vdis.PDU;

/**
 * @author Tony Pinkston
 */
public class PlaybackThread extends NetworkThread {

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
        socket = new MulticastSocket();
        socket.setBroadcast(true);
        socket.setReuseAddress(true);
    }

    public int getPort() {

        return port;
    }

    public Exception getException() {

        return exception;
    }

    @Override
    public void setPaused(boolean paused) {

        if (super.isPaused() != paused) {

            super.setPaused(paused);
            sendStatus(PlaybackStatus.PAUSED);
        }
    }

    @Override
    public void run() {

        sendStatus(PlaybackStatus.STARTED);

        try {

            while (!super.isStopped()) {

                if (super.isPaused()) {

                    sleep(500);
                }
                else {

                    PDU pdu = list.get(index);

                    sendPDU(pdu);

                    sendStatus(PlaybackStatus.SENDING);
                    index++;

                    if (index < list.size()) {

                        PDU next = list.get(index);

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
        catch (Exception exception) {

            this.exception = exception;
            logger.error("Caught exception!", exception);
        }

        socket.close();
        sendStatus(PlaybackStatus.COMPLETE);
    }

    public void sendPDU(PDU pdu) throws IOException {

        sendBuffer(pdu.getData(), pdu.getLength());
    }

    public void sendBuffer(byte buffer[], int length) throws IOException {

        DatagramPacket packet = new DatagramPacket(
            buffer,
            length,
            address,
            port);

        socket.send(packet);
    }

    private void sendStatus(PlaybackStatus status) {

        float percent = ((float) index / (float) list.size());

        listener.sendStatus(status, (int) (percent * 100.0f));
    }
}
