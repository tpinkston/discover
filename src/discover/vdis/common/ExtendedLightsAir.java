/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract32Bits;
import discover.vdis.bits.Bits;

public class ExtendedLightsAir extends Abstract32Bits {

    private static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.get3("Formation Lights Intensity", 2, false, null));
        values.add(Bits.getOnOff("Red Anti-Collision Light", 3));
        values.add(Bits.getOnOff("Spot/Search Lights", 4));
        values.add(Bits.getOnOff("Search Light #1 NVG", 5));
        values.add(Bits.getOnOff("Search Light #2 NVG", 6));
        values.add(Bits.getYesNo("Search Light #1 Extended", 7));
        values.add(Bits.getYesNo("Search Light #2 Extended", 8));
        values.add(Bits.getOnOff("Aft Navigation  Light", 9));
        values.add(Bits.getYesNo("Landing Lights Extended", 10));
    }

    public ExtendedLightsAir() {

    }

    public ExtendedLightsAir(int value) {

        super.set(value);
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public ExtendedLightsAir clone() {

        ExtendedLightsAir lights = new ExtendedLightsAir();

        lights.set(super.get());

        return lights;
    }
}
