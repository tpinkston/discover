package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract8Bits;
import discover.vdis.bits.Bits;
import discover.vdis.enums.DISGUISE_STATUS;
import discover.vdis.enums.PRESENT_DOMAIN;

/**
 * @author Tony Pinkston
 */
public class ExtendedStatus extends Abstract8Bits {

    public static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.get4("PresentDomain", 3, false, PRESENT_DOMAIN.class));
        values.add(Bits.get3("Disguise", 6, false, DISGUISE_STATUS.class));
        values.add(Bits.getYesNo("Invincible", 7));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public ExtendedStatus clone() {

        ExtendedStatus record = new ExtendedStatus();

        record.set(get());

        return record;
    }
}
