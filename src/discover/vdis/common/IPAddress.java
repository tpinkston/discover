/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

import discover.common.Readable;
import discover.common.Writable;

public class IPAddress implements Readable, Writable {
    
    public static final int MASK = 0x000000FF;
    
    public final byte address[] = new byte[] { 0, 0, 0, 0 };
    
    @Override
    public String toString() {
        
        StringBuilder string = new StringBuilder();
        
        string.append(MASK & (int)this.address[0]);
        string.append(".");
        string.append(MASK & (int)this.address[1]);
        string.append(".");
        string.append(MASK & (int)this.address[2]);
        string.append(".");
        string.append(MASK & (int)this.address[3]);
        
        return string.toString();
    }

    public void set(InetAddress address) {
        
        byte bytes[] = address.getAddress();
        
        this.address[0] = bytes[0];
        this.address[1] = bytes[1];
        this.address[2] = bytes[2];
        this.address[3] = bytes[3];
    }

    @Override
    public void read(DataInputStream stream) throws IOException {
        
        stream.read(this.address, 0, 4);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        
        stream.write(this.address, 0, 4);
    }
}
