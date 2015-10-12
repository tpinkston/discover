package discover.vdis.common;

import discover.vdis.bits.Abstract16Bits;

/**
 * @author Tony Pinkston
 */
public class ExtendedEquipmentDefault extends Abstract16Bits {

    @Override
    public ExtendedEquipmentDefault clone() {

        ExtendedEquipmentDefault equipment = new ExtendedEquipmentDefault();

        equipment.set(get());

        return equipment;
    }

}
