/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract16Bits;
import discover.vdis.bits.Bits;
import discover.vdis.enums.VDIS;

public class ExtendedEquipmentAir extends Abstract16Bits {

    public static final List<Bits> values;
    
    static {
        
        values = new ArrayList<Bits>();

        values.add(Bits.getGenericPresence("Auxiliary Power Plant #2", 0));
        values.add(Bits.getGenericPresence("Auxiliary Power Plant #3", 1));
        values.add(Bits.getGenericPresence("Auxiliary Power Plant #4", 2));
        values.add(Bits.get2("Sling Load", 4, false, getHandle(VDIS.AIR_EQ_SLINGLOAD)));
        values.add(Bits.getYesNo("Sling Loaded", 5));
        values.add(Bits.get2("Sling Damage", 7, false, getHandle(VDIS.SLING_DAMAGE)));
        values.add(Bits.getGenericPresence("SA Server", 8));
        values.add(Bits.get2("IED", 10, false, getHandle(VDIS.IED_PRESENCE)));
    }

    public ExtendedEquipmentAir() {

    }

    public ExtendedEquipmentAir(short value) {
        
        super.set(value);
    }
    
    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public ExtendedEquipmentAir clone() {
        
        ExtendedEquipmentAir equipment = new ExtendedEquipmentAir();
        
        equipment.set(super.get());
        
        return equipment;
    }
}
