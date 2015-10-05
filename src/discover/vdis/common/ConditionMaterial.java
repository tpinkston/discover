/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract8Bits;
import discover.vdis.bits.Bits;
import discover.vdis.enums.VDIS;

public final class ConditionMaterial extends Abstract8Bits {

    private static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.get2("Rust", 7, false, getHandle(VDIS.SEVERITY)));
        values.add(Bits.get2("Material", 5, false, getHandle(VDIS.PL_COND_MTL)));
        values.add(Bits.get2("Damage", 3, false, getHandle(VDIS.SEVERITY)));
        values.add(Bits.get2("Cleanliness", 1, false, getHandle(VDIS.SEVERITY)));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public ConditionMaterial clone() {

        ConditionMaterial record = new ConditionMaterial();

        record.set(super.get());

        return record;
    }
}
