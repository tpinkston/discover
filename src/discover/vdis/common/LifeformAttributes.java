/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract16Bits;
import discover.vdis.bits.Bits;
import discover.vdis.enums.VDIS;

public class LifeformAttributes extends Abstract16Bits {

    private static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.get1("Gender", 0, true, getHandle(VDIS.SEX)));
        values.add(Bits.get4("Age Group", 4, false, getHandle(VDIS.HUMAN_AGE_GROUP)));
        values.add(Bits.get4("Ethnicity", 8, false, getHandle(VDIS.ETHNICITY)));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public LifeformAttributes clone() {

        LifeformAttributes attributes = new LifeformAttributes();

        attributes.set(this.get());

        return attributes;
    }
}
