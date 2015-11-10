package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract16Bits;
import discover.vdis.bits.Bits;
import discover.vdis.enums.AIR_EQ_SLINGLOAD;
import discover.vdis.enums.HOIST_STATUS;
import discover.vdis.enums.IED_PRESENCE;
import discover.vdis.enums.SLING_DAMAGE;

/**
 * @author Tony Pinkston
 */
public class ExtendedEquipmentAir extends Abstract16Bits {

    public static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.getOnOff("Auxiliary Power Plant #2", 0));
        values.add(Bits.getOnOff("Auxiliary Power Plant #3", 1));
        values.add(Bits.getOnOff("Auxiliary Power Plant #4", 2));
        values.add(Bits.get2("Sling Load", 4, false, AIR_EQ_SLINGLOAD.class));
        values.add(Bits.getYesNo("Sling Loaded", 5));
        values.add(Bits.get2("Sling Damage", 7, false, SLING_DAMAGE.class));
        values.add(Bits.getGenericPresence("SA Server", 8));
        values.add(Bits.get2("IED", 10, false, IED_PRESENCE.class));
        values.add(Bits.get2("Hoist", 12, false, HOIST_STATUS.class));
        values.add(Bits.getYesNo("Right Side Door Open", 13));
        values.add(Bits.getYesNo("Left Side Door Open", 14));
        values.add(Bits.getYesNo("ATIR Countermeasures Active", 15));
    }

    public ExtendedEquipmentAir() {

    }

    public ExtendedEquipmentAir(short value) {

        set(value);
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public ExtendedEquipmentAir clone() {

        ExtendedEquipmentAir equipment = new ExtendedEquipmentAir();

        equipment.set(get());

        return equipment;
    }
}
