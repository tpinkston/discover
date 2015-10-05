/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract8Bits;
import discover.vdis.bits.Bits;

public class IFFChangeOptions extends Abstract8Bits {

    private static final List<Bits> values;
    
    static {
        
        values = new ArrayList<Bits>();

        values.add(Bits.getYesNo("Change Indicator", 0));
        values.add(Bits.getYesNo("System Specific Field 1", 1));
        values.add(Bits.getYesNo("System Specific Field 2", 2));
        values.add(Bits.getYesNo("Heartbeat Indicator", 3));
        values.add(Bits.getYesNo("Transponder/Interrogator Indicator", 4));
        values.add(Bits.getYesNo("Simulation Mode", 5));
        values.add(Bits.getYesNo("Test Mode", 7));
    }
    
    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public IFFChangeOptions clone() {
        
        IFFChangeOptions record = new IFFChangeOptions();
        
        record.set(super.get());
        
        return record;
    }
}
