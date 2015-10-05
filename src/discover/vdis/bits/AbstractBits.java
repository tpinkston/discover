/**
 * @author Tony Pinkston
 */
package discover.vdis.bits;

import java.util.Collections;
import java.util.List;

import discover.common.Binary;
import discover.common.Readable;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;
import discover.vdis.enums.VDIS.Handle;

public abstract class AbstractBits implements Bufferable, Cloneable, Readable, Writable {
    
    /**
     * @return True if all bits are zero.
     */
    public abstract boolean isEmpty();
    
    /**
     * @return Bits as 32bit integer.
     */
    public abstract int getBits();

    /**
     * @return Bits in string binary form.
     */
    public abstract String getBitString();
    
    /**
     * @return {@link AbstractBits}
     */
    @Override
    public abstract AbstractBits clone();
    
    /**
     * Implemented by child class if needed.
     * 
     * @return List of possible values represented by this bit set. 
     */
    public List<Bits> getValues() {
        
        return Collections.emptyList();
    }
    
    /**
     * @return True if the binary bit string gets included in buffer.
     * @see toBuffer()
     */
    public boolean bitsInBuffer() {
        
        return true;
    }
    
    public int getValue(Bits bits) {
        
        final int bitset = this.getBits();
        int value = 0x00;

        switch(bits.count) {
        
            case 1:
                value = Binary.get1Bit(bits.bit, bitset);
                break;
            case 2:
                value = Binary.get2Bits(bits.bit, bitset);
                break;
            case 3:
                value = Binary.get3Bits(bits.bit, bitset);
                break;
            case 4:
                value = Binary.get4Bits(bits.bit, bitset);
                break;
        }

        return value;
    }
    
    @Override
    public String toString() {
        
        return this.getBitString();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        if (this.bitsInBuffer()) {
            
            buffer.addText("Bits: " + this.getBitString());
            buffer.addBreak();
        }
        
        for(Bits bits : this.getValues()) {
            
            int value = this.getValue(bits);
            
            if (bits.handle == null) {
                
                buffer.addAttribute(
                    bits.label, 
                    "(" + value + ")");
            }
            else if ((value != 0) || bits.zeroVisible) {
                
                buffer.addAttribute(
                    bits.label, 
                    bits.handle.getDescription(value));
            }
        }
    }
    
    protected static Handle getHandle(int type) {
        
        return VDIS.getHandle(type);
    }
}
