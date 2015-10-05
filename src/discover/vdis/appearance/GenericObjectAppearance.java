/**
 * @author Tony Pinkston
 */
package discover.vdis.appearance;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.bits.Abstract8Bits;
import discover.vdis.bits.Bits;
import discover.vdis.enums.VDIS;

/**
 * 16-bit record for the general appearance of an environment object (point,
 * linear or areal).  The lower 8 bits are for a single integer value, 
 * the upper 8 bits comprise several individual enumerations. 
 */
public class GenericObjectAppearance extends Abstract8Bits {

    public static final List<Bits> values;
    
    private int percentComplete = 0;
    
    static {
        
        values = new ArrayList<Bits>();

        values.add(Bits.get2("Damage", 1, true, getHandle(VDIS.OBJECT_DAMAGE)));
        values.add(Bits.getYesNo("Predistributed", 2));
        values.add(Bits.getYesNo("Activated", 3));
        values.add(Bits.getYesNo("Smoking", 4));
        values.add(Bits.getYesNo("Flaming", 5));
    }
    
    public void clear() {

        super.set((byte)0);
        this.percentComplete = 0;
    }
    
    @Override
    public boolean bitsInBuffer() {
        
        return false;
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addText("Bits: " + this.getBitString());
        buffer.addBreak();

        buffer.addAttribute("Percent Complete", this.percentComplete);
        
        super.toBuffer(buffer);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {
        
        // Read upper 8 bits:
        super.read(stream);

        // Read lower 8 bits:
        this.percentComplete = stream.readUnsignedByte();
    }

    @Override
    public GenericObjectAppearance clone() {
        
        GenericObjectAppearance appearance = new GenericObjectAppearance();
        
        appearance.set(super.get());
        appearance.percentComplete = this.percentComplete;
        
        return appearance;
    }
}
