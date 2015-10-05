/**
 * @author Tony Pinkston
 */
package discover.vdis.bits;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import discover.Discover;
import discover.common.Binary;

public abstract class Abstract8Bits extends AbstractBits {

    public static final int LENGTH = 1;

    protected static final Logger logger = Discover.getLogger();

    private byte value = 0x00;
    
    public final byte get() {
        
        return this.value;
    }
    
    public final void set(byte value) {
        
        this.value = value;
    }
    
    @Override
    public final boolean isEmpty() {
        
        return (this.value == 0x00);
    }

    @Override
    public final int getBits() {
        
        return (this.value & 0xFF);
    }

    @Override
    public final String getBitString() {
        
        return Binary.toString8(this.value);
    }
    
    @Override
    public final boolean equals(Object object) {
        
        if ((object != null) &&(this.getClass() == object.getClass())) {
                
            Abstract8Bits bits = (Abstract8Bits)object;

            return (this.getBits() == bits.getBits());
        }
        
        return false;
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.value = stream.readByte();
    }
    
    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeByte(this.value & 0xFF);
    }
}
