/**
 * @author Tony Pinkston
 */
package discover.system;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;

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
        
        this.port = -1;
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
    
        return this.port;
    }

    /**
     * @return {@link DatagramPacket}
     */
    public DatagramPacket getPacket() {
        
        return this.packet;
    }
    
    /**
     * @return Length of packet's data buffer.
     */
    public int getLength() {
        
        return this.packet.getLength();
    }
    
    /**
     * @return Packet's data buffer.
     */
    public byte[] getData() {
        
        return this.packet.getData();
    }
    
    /**
     * @return Source IP address.
     */
    public InetAddress getAddress() {
        
        return this.packet.getAddress();
    }
    
    /**
     * @return Destination or source IP address in textual form.
     */
    public String getHostAddress() {
        
        if (this.getAddress() == null) {
            
            return null;
        }
        else {
            
            return this.getAddress().getHostAddress();
        }
    }

    /**
     * @return Domain name for this packet's IP address.
     */
    public String getCanonicalHostName() {
        
        if (this.getAddress() == null) {
            
            return null;
        }
        else {
            
            return this.getAddress().getCanonicalHostName();
        }
    }
    
    /**
     * @return {@link DataInputStream} containing packet buffer.
     */
    public DataInputStream getInputStream() {
        
        byte buffer[] = this.packet.getData();
        
        return new DataInputStream(new ByteArrayInputStream(buffer));
    }
    
    /**
     * Returns packet buffer size and host address in string form.
     */
    @Override
    public String toString() {
        
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("[");
        buffer.append(this.getLength());
        buffer.append(" bytes, port ");
        buffer.append(this.port);
        buffer.append(", ");
        buffer.append(this.getHostAddress());
        buffer.append("]");
         
        return buffer.toString();
    }
}
