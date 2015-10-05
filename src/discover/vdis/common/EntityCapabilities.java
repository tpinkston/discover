/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract32Bits;
import discover.vdis.bits.Bits;

public class EntityCapabilities extends Abstract32Bits {

    private static final List<Bits> values;
    
    static {
        
        values = new ArrayList<Bits>();

        values.add(Bits.getGenericPresence("Ammunition Supply", 0));
        values.add(Bits.getGenericPresence("Fuel Supply", 1));
        values.add(Bits.getGenericPresence("Recovery", 2));
        values.add(Bits.getGenericPresence("Repair", 3));
        values.add(Bits.getOnOff("ADS-Broadcast", 4));
        values.add(Bits.getYesNo("Sling Load Carrier", 5));
        values.add(Bits.getYesNo("Sling Loadable", 6));
        values.add(Bits.getYesNo("IED Presence", 7));
        values.add(Bits.getYesNo("Task Organizable", 8));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public EntityCapabilities clone() {
        
        EntityCapabilities capabilities = new EntityCapabilities();
        
        capabilities.set(super.get());
        
        return capabilities;
    }
}
