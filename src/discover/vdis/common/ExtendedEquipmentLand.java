package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract16Bits;
import discover.vdis.bits.Bits;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class ExtendedEquipmentLand extends Abstract16Bits {

    private static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.getGenericPresence("Spoiler", 0));
        values.add(Bits.getGenericPresence("Cargo Racks", 1));
        values.add(Bits.getOnOff("Auxiliary Power Plant", 2));
        values.add(Bits.getOnOff("Emergency Vehicle Lights", 3));
        values.add(Bits.getYesNo("Sling Loaded", 5));
        values.add(Bits.getGenericPresence("Crew Antenna", 6));
        values.add(Bits.getGenericPresence("SA Server", 8));
        values.add(Bits.get2("IED", 10, false, getHandle(VDIS.IED_PRESENCE)));
    }

    public ExtendedEquipmentLand() {

    }

    public ExtendedEquipmentLand(short value) {

        set(value);
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public ExtendedEquipmentLand clone() {

        ExtendedEquipmentLand equipment = new ExtendedEquipmentLand();

        equipment.set(get());

        return equipment;
    }
}
