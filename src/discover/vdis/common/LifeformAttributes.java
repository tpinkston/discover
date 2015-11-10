package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract16Bits;
import discover.vdis.bits.Bits;
import discover.vdis.enums.LF_AGE_GROUP;
import discover.vdis.enums.LF_ETHNICITY;
import discover.vdis.enums.LF_GENDER;

/**
 * @author Tony Pinkston
 */
public class LifeformAttributes extends Abstract16Bits {

    private static final List<Bits> values = new ArrayList<Bits>();

    static {

        values.add(Bits.get1("Gender", 0, true, LF_GENDER.class));
        values.add(Bits.get4("Age Group", 4, false, LF_AGE_GROUP.class));
        values.add(Bits.get4("Ethnicity", 8, false, LF_ETHNICITY.class));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public LifeformAttributes clone() {

        LifeformAttributes attributes = new LifeformAttributes();

        attributes.set(get());

        return attributes;
    }
}
