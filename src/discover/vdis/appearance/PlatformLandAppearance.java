package discover.vdis.appearance;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Bits;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class PlatformLandAppearance extends AbstractAppearance {

    public static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.getYesNo("Camouflaged", 0));
        values.add(Bits.getYesNo("Mobility Damage", 1));
        values.add(Bits.getYesNo("Firepower Damage", 2));
        values.add(Bits.get2("Damage", 4, false, getHandle(VDIS.SEVERITY)));
        values.add(Bits.get2("Smoke", 6, false, getHandle(VDIS.LAND_SMOKE)));
        values.add(Bits.getOnOff("Head Lights", 12));
        values.add(Bits.getOnOff("Tail Lights", 13));
        values.add(Bits.getOnOff("Brake Lights", 14));
        values.add(Bits.getYesNo("Flaming", 15));
        values.add(Bits.getYesNo("Launcher", 16));
        values.add(Bits.getYesNo("Frozen", 21));
        values.add(Bits.getOnOff("Power Plant", 22));
        values.add(Bits.getYesNo("Deactivated", 23));
        values.add(Bits.getYesNo("Tent", 24));
        values.add(Bits.getYesNo("Ramp", 25));
        values.add(Bits.getOnOff("Blackout Lights", 26));
        values.add(Bits.getOnOff("Blackout Brake Lights", 27));
        values.add(Bits.getOnOff("Spot Lights", 28));
        values.add(Bits.getOnOff("Interior Lights", 29));
        values.add(Bits.getYesNo("Surrendered", 30));
    }

    @Override
    public String getName() {

        return "Platform Land Appearance";
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public PlatformLandAppearance clone() {

        PlatformLandAppearance appearance = new PlatformLandAppearance();

        appearance.set(get());

        return appearance;
    }
}
