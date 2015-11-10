package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract8Bits;
import discover.vdis.bits.Bits;
import discover.vdis.enums.PL_COND_MTL;
import discover.vdis.enums.SEVERITY;

/**
 * @author Tony Pinkston
 */
public final class ConditionMaterial extends Abstract8Bits {

    private static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.get2("Rust", 7, false, SEVERITY.class));
        values.add(Bits.get2("Material", 5, false, PL_COND_MTL.class));
        values.add(Bits.get2("Damage", 3, false, SEVERITY.class));
        values.add(Bits.get2("Cleanliness", 1, false, SEVERITY.class));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public ConditionMaterial clone() {

        ConditionMaterial record = new ConditionMaterial();

        record.set(get());

        return record;
    }
}
