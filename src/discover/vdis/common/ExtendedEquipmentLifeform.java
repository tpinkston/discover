package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract32Bits;
import discover.vdis.bits.Bits;
import discover.vdis.enums.IED_PRESENCE;
import discover.vdis.enums.LF_EQ_CHUTE;
import discover.vdis.enums.LF_EQ_LASER;

/**
 * @author Tony Pinkston
 */
public class ExtendedEquipmentLifeform extends Abstract32Bits {

    private static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.getGenericPresence("Binoculars", 0));
        values.add(Bits.get2("Parachute", 2, false, LF_EQ_CHUTE.class));
        values.add(Bits.getGenericPresence("Rebreather", 3));
        values.add(Bits.getGenericPresence("SCUBA Gear", 4));
        values.add(Bits.getGenericPresence("Night Vision Goggles", 5));
        values.add(Bits.get2("Laser", 7, false, LF_EQ_LASER.class));
        values.add(Bits.getGenericPresence("Radio", 8));
        values.add(Bits.get2("IED", 10, false, IED_PRESENCE.class));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public ExtendedEquipmentLifeform clone() {

        ExtendedEquipmentLifeform equipment = new ExtendedEquipmentLifeform();

        equipment.set(get());

        return equipment;
    }
}
