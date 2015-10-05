/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import discover.vdis.bits.Abstract16Bits;

public class ExtendedEquipmentDefault extends Abstract16Bits {

    @Override
    public ExtendedEquipmentDefault clone() {

        ExtendedEquipmentDefault equipment = new ExtendedEquipmentDefault();

        equipment.set(super.get());

        return equipment;
    }

}
