package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract16Bits;
import discover.vdis.bits.Bits;
import discover.vdis.enums.IED_PRESENCE;

/**
 * @author Tony Pinkston
 */
public class ExtendedEquipmentCulturalFeature extends Abstract16Bits {

    private static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.getYesNo("Sling Loaded", 5));
        values.add(Bits.get2("IED", 10, false, IED_PRESENCE.class));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public ExtendedEquipmentCulturalFeature clone() {

        ExtendedEquipmentCulturalFeature equipment = new ExtendedEquipmentCulturalFeature();

        equipment.set(get());

        return equipment;
    }
}
