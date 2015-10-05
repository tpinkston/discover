/**
 * @author Tony Pinkston
 */
package discover.system;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import discover.Discover;
import discover.vdis.PDU;

public class CaptureThread extends NetworkThread {

    private static final Logger logger = Discover.getLogger();

    private static final int TIMEOUT = 1000; // 5 seconds

    private final List<PDU> pdus = new ArrayList<PDU>();
    private final CaptureThreadListener listener;
    private final int port;

    private DatagramSocket socket = null;
    private boolean bundled = false;

    public CaptureThread(
        String name,
        CaptureThreadListener listener,
        int port) throws Exception {

        super(name);

        this.port = port;
        this.listener = listener;

        String addresses[] = Network.getMulticastAddresses();

        this.socket = new MulticastSocket(this.port);

        if ((addresses == null) || (addresses.length == 0)) {

            this.socket.setReuseAddress(true);
        }
        else {

            for(String address : Network.getMulticastAddresses()) {

                InetAddress multicast = InetAddress.getByName(address);

                if (multicast != null) {

                    ((MulticastSocket)this.socket).joinGroup(multicast);
                }
            }
        }

        this.socket.setSoTimeout(TIMEOUT);
    }

    public synchronized void setBundledPDUs(boolean bundled) {

        this.bundled = bundled;
    }

    public void joinGroup(String address) {

        if (this.socket instanceof MulticastSocket) {

            try {

                InetAddress multicast = InetAddress.getByName(address);

                if (multicast != null) {

                    ((MulticastSocket)this.socket).joinGroup(multicast);
                }
            }
            catch(Exception exception) {

                logger.warning(
                    "Could not join address " + address +
                    ", exception: " + exception.getClass().getName());
            }
        }
    }

    @Override
    public void run() {

        DatagramPacket packet = new DatagramPacket(new byte[2048], 2048);

        logger.info("Thread started: " + super.getName());

        while(!super.isStopped()) {

            try {

                this.receive(packet);

                if (!this.pdus.isEmpty()) {

                    this.listener.pdusCaptured(this.pdus);
                    this.pdus.clear();
                }
            }
            catch(SocketTimeoutException exception) {

                // No inbound PDUs, do nothing but continue in while loop
                // if thread has not been stopped.
            }
            catch(IOException exception) {

                logger.log(Level.SEVERE, "Caught exception!", exception);
            }
        }

        logger.info("Thread finished: " + super.getName());
    }

    private void receive(DatagramPacket packet) throws IOException {

        ByteArrayInputStream bytes = null;
        DataInputStream stream = null;
        int index = 0;
        int length = 0;

        this.socket.receive(packet);

        if (!super.isPaused()) {

            final int size = packet.getLength();
            final byte[] data = packet.getData();

            if (logger.isLoggable(Level.FINEST)) {

                logger.finest(
                    "Packet from host: " + Network.getHostAddress(packet) +
                    ", port: " + this.port +
                    ", length: " + packet.getLength());
            }

            // Need at least 12 bytes for PDU header.
            if (size > 11) {

                if (!this.bundled) {

                    this.createPDU(packet, Arrays.copyOf(data, size));
                }
                else try {

                    bytes = new ByteArrayInputStream(packet.getData());
                    stream = new DataInputStream(bytes);
                    index = 0;

                    while(index < size) {

                        stream.skipBytes(8);

                        length = stream.readUnsignedShort();

                        if (length < 12) {

                            logger.severe("Stream size less than 12 bytes!");
                        }
                        else if (length > (size - index)) {

                            logger.severe("Stream size greater than data size!");
                        }
                        else {

                            byte[] subbytes = new byte[size];

                            System.arraycopy(data, index, subbytes, 0, length);

                            this.createPDU(packet, subbytes);

                            index += length;

                            stream.skipBytes(length - 10);
                        }
                    }

                    stream.close();
                }
                catch(IOException exception) {

                    logger.log(Level.SEVERE, "Caught exception!", exception);
                }
            }
        }
    }

    private void createPDU(DatagramPacket packet, byte[] data) {

        PDU pdu = new PDU(data);

        pdu.setData(data);
        pdu.setTitle();
        pdu.setInitiator();
        pdu.setTimestamp(true);
        pdu.setPort(this.port);
        pdu.setTime(System.currentTimeMillis());
        pdu.setSource(Network.getHostAddress(packet));

        this.pdus.add(pdu);
    }
}
