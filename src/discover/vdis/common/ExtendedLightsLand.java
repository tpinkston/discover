/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract32Bits;
import discover.vdis.bits.Bits;

public class ExtendedLightsLand extends Abstract32Bits {

    private static final List<Bits> values;
    
    static {
        
        values = new ArrayList<Bits>();

        values.add(Bits.getOnOff("Left Signal", 0));
        values.add(Bits.getOnOff("Right Signal", 1));
        values.add(Bits.getOnOff("High Beams", 2));
        values.add(Bits.getOnOff("Spot/Search #2", 3));
        values.add(Bits.getOnOff("Running Lights", 4));
        values.add(Bits.getOnOff("Emergency Lights", 5));
        values.add(Bits.getOnOff("Fog Lights", 6));
        values.add(Bits.getOnOff("Parking Lights", 7));
        values.add(Bits.getOnOff("Reverse Lights", 8));
        values.add(Bits.getOnOff("Auxiliary High Beams", 9));
        values.add(Bits.getOnOff("Rear Interior Lights", 10));
        values.add(Bits.getOnOff("Emergency Vehicle Lights", 11));
    }
    
    public ExtendedLightsLand() {

    }
    
    public ExtendedLightsLand(int value) {
        
        super.set(value);
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public ExtendedLightsLand clone() {
        
        ExtendedLightsLand lights = new ExtendedLightsLand();
        
        lights.set(super.get());
        
        return lights;
    }
}
