package discover.vdis.appearance;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Bits;
import discover.vdis.enums.AIR_SMOKE;
import discover.vdis.enums.SEVERITY;

/**
 * @author Tony Pinkston
 */
public class PlatformAirAppearance extends AbstractAppearance {

    public static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.getYesNo("Camouflaged", 0));
        values.add(Bits.getYesNo("Propulsion Damage", 1));
        values.add(Bits.getYesNo("Firepower Damage", 2));
        values.add(Bits.get2("Damage", 4, false, SEVERITY.class));
        values.add(Bits.get2("Smoke", 6, false, AIR_SMOKE.class));
        values.add(Bits.getOnOff("Landing Lights", 12));
        values.add(Bits.getOnOff("Navigation Lights", 13));
        values.add(Bits.getOnOff("Collision Lights", 14));
        values.add(Bits.getYesNo("Flaming", 15));
        values.add(Bits.getOnOff("After Burner", 16));
        values.add(Bits.getYesNo("Frozen", 21));
        values.add(Bits.getOnOff("Power Plant", 22));
        values.add(Bits.getYesNo("Deactivated", 23));
        values.add(Bits.getOnOff("Formation Lights", 24));
        values.add(Bits.getOnOff("Spot Lights", 28));
        values.add(Bits.getOnOff("Interior Lights", 29));
    }

    @Override
    public String getName() {

        return "Platform Air Appearance";
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public PlatformAirAppearance clone() {

        PlatformAirAppearance appearance = new PlatformAirAppearance();

        appearance.set(get());

        return appearance;
    }
}
