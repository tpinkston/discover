package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract8Bits;
import discover.vdis.bits.Bits;

/**
 * @author Tony Pinkston
 */
public class IFFInformationLayers extends Abstract8Bits {

    private static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.getYesNo("Layer 1", 1));
        values.add(Bits.getYesNo("Layer 2", 2));
        values.add(Bits.getYesNo("Layer 3", 3));
        values.add(Bits.getYesNo("Layer 4", 4));
        values.add(Bits.getYesNo("Layer 5", 5));
        values.add(Bits.getYesNo("Layer 6", 6));
        values.add(Bits.getYesNo("Layer 7", 7));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public IFFInformationLayers clone() {

        IFFInformationLayers record = new IFFInformationLayers();

        record.set(get());

        return record;
    }
}
