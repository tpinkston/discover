package discover.system;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * @author Tony Pinkston
 */
public class Packet {

    private static int BUFFER_SIZE = 2048;

    private final DatagramPacket packet = new DatagramPacket(
        new byte[BUFFER_SIZE],
        BUFFER_SIZE);

    private int port;

    /**
     * Default Constructor.
     */
    public Packet() {

        port = -1;
    }

    /**
     * Constructor with port number.
     */
    public Packet(int port) {

        this.port = port;
    }

    /**
     * @return Port number.
     */
    public int getPort() {

        return port;
    }

    /**
     * @return {@link DatagramPacket}
     */
    public DatagramPacket getPacket() {

        return packet;
    }

    /**
     * @return Length of packet's data buffer.
     */
    public int getLength() {

        return packet.getLength();
    }

    /**
     * @return Packet's data buffer.
     */
    public byte[] getData() {

        return packet.getData();
    }

    /**
     * @return Source IP address.
     */
    public InetAddress getAddress() {

        return packet.getAddress();
    }

    /**
     * @return Destination or source IP address in textual form.
     */
    public String getHostAddress() {

        if (getAddress() == null) {

            return null;
        }
        else {

            return getAddress().getHostAddress();
        }
    }

    /**
     * @return Domain name for this packet's IP address.
     */
    public String getCanonicalHostName() {

        if (getAddress() == null) {

            return null;
        }
        else {

            return getAddress().getCanonicalHostName();
        }
    }

    /**
     * @return {@link DataInputStream} containing packet buffer.
     */
    public DataInputStream getInputStream() {

        byte buffer[] = packet.getData();

        return new DataInputStream(new ByteArrayInputStream(buffer));
    }

    /**
     * Returns packet buffer size and host address in string form.
     */
    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("[");
        buffer.append(getLength());
        buffer.append(" bytes, port ");
        buffer.append(port);
        buffer.append(", ");
        buffer.append(getHostAddress());
        buffer.append("]");

        return buffer.toString();
    }
}
