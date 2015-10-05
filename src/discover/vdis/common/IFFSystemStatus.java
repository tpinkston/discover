/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract8Bits;
import discover.vdis.bits.Bits;

public class IFFSystemStatus extends Abstract8Bits {

    private static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.getOnOff("System", 0));
        values.add(Bits.getYesNo("Parameter 1 Capable", 1));
        values.add(Bits.getYesNo("Parameter 2 Capable", 2));
        values.add(Bits.getYesNo("Parameter 3 Capable", 3));
        values.add(Bits.getYesNo("Parameter 4 Capable", 4));
        values.add(Bits.getYesNo("Parameter 5 Capable", 5));
        values.add(Bits.getYesNo("Parameter 6 Capable", 6));
        values.add(Bits.getYesNo("Operational", 7));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public IFFSystemStatus clone() {

        IFFSystemStatus record = new IFFSystemStatus();

        record.set(super.get());

        return record;
    }
}
