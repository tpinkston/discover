/**
 * @author Tony Pinkston
 */
package discover.vdis.appearance;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Bits;
import discover.vdis.enums.VDIS;

public class LifeformAppearance extends AbstractAppearance {

    public static final List<Bits> values = new ArrayList<Bits>();

    static {

        values.add(Bits.get1("Clothing", 0, true, getHandle(VDIS.LF_CLOTH_TYPE)));
        values.add(Bits.get2("Health", 4, true, getHandle(VDIS.LF_HEALTH)));
        values.add(Bits.get4("Compliance", 8, false, getHandle(VDIS.LF_COMPLIANCE)));
        values.add(Bits.getGenericPresence("Flash Lights", 12));
        values.add(Bits.get4("Posture", 19, true, getHandle(VDIS.LF_POSTURE)));
        values.add(Bits.getYesNo("Frozen", 21));
        values.add(Bits.getYesNo("Mounted", 22));
        values.add(Bits.getYesNo("Deactivated", 23));
        values.add(Bits.get2("First Weapon", 25, true, getHandle(VDIS.LF_WEAPON_STATE)));
        values.add(Bits.get2("Second Weapon", 27, true, getHandle(VDIS.LF_WEAPON_STATE)));
        values.add(Bits.get2("Camouflage", 29, true, getHandle(VDIS.LF_CAMOUFLAGE)));
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

        appearance.set(super.get());

        return appearance;
    }
}
