package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract16Bits;
import discover.vdis.bits.Bits;

/**
 * @author Tony Pinkston
 */
public class SpreadSpectrum extends Abstract16Bits {

    public static final List<Bits> values = new ArrayList<Bits>();

    static {

        values.add(Bits.getYesNo("Frequency Hopping", 0));
        values.add(Bits.getYesNo("Pseudo Noise", 1));
        values.add(Bits.getYesNo("Time Hopping", 2));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public SpreadSpectrum clone() {

        SpreadSpectrum object = new SpreadSpectrum();

        object.set(get());

        return object;
    }
}
