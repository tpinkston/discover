package discover.vdis.appearance;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Bits;
import discover.vdis.enums.LF_CAMOUFLAGE_TYPE;
import discover.vdis.enums.LF_CLOTH_TYPE;
import discover.vdis.enums.LF_COMPLIANCE;
import discover.vdis.enums.LF_HEALTH;
import discover.vdis.enums.LF_POSTURE;
import discover.vdis.enums.LF_WEAPON_STATE;

/**
 * @author Tony Pinkston
 */
public class LifeformAppearance extends AbstractAppearance {

    public static final List<Bits> values = new ArrayList<Bits>();

    static {

        values.add(Bits.get1("Clothing", 0, true, LF_CLOTH_TYPE.class));
        values.add(Bits.get2("Health", 4, true, LF_HEALTH.class));
        values.add(Bits.get4("Compliance", 8, false, LF_COMPLIANCE.class));
        values.add(Bits.getOnOff("Flash Lights", 12));
        values.add(Bits.get4("Posture", 19, true, LF_POSTURE.class));
        values.add(Bits.getYesNo("Frozen", 21));
        values.add(Bits.getYesNo("Mounted", 22));
        values.add(Bits.getYesNo("Deactivated", 23));
        values.add(Bits.get2("First Weapon", 25, true, LF_WEAPON_STATE.class));
        values.add(Bits.get2("Second Weapon", 27, true, LF_WEAPON_STATE.class));
        values.add(Bits.get2("Camouflage", 29, true, LF_CAMOUFLAGE_TYPE.class));
        values.add(Bits.getYesNo("Concealed Stationary", 30));
        values.add(Bits.getYesNo("Concealed Movement", 31));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public String getName() {

        return "Lifeform Appearance";
    }

    @Override
    public LifeformAppearance clone() {

        LifeformAppearance appearance = new LifeformAppearance();

        appearance.set(get());

        return appearance;
    }
}
