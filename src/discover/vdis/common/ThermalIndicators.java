package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract8Bits;
import discover.vdis.bits.Bits;

/**
 * @author Tony Pinkston
 */
public class ThermalIndicators extends Abstract8Bits {

    public static final List<Bits> values = new ArrayList<Bits>();

    static {

        values.add(Bits.getYesNo("Main Power Plant", 0));
        values.add(Bits.getYesNo("Auxiliary Power Plant", 1));
        values.add(Bits.getYesNo("Drive Train (Left/Front/Top)", 2));
        values.add(Bits.getYesNo("Drive Train (Right/Rear/Bottom)", 3));
        values.add(Bits.getYesNo("Gun (Primary or Left)", 4));
        values.add(Bits.getYesNo("Gun (Secondary or Right)", 5));
        values.add(Bits.get2("Solar Heating", 7, false, null));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public ThermalIndicators clone() {

        ThermalIndicators record = new ThermalIndicators();

        record.set(get());

        return record;
    }
}
