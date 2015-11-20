package discover.vdis.enums;

import java.util.List;

/**
 * PL_COND_EXT_DMG: This class is auto-generated by vdis.EnumGenerator
 */
public final class PL_COND_EXT_DMG extends Value {

    public static final PL_COND_EXT_DMG
       PLAT_COND_EXT_DMG_NOT_SPECIFIED = new PL_COND_EXT_DMG(0, "PLAT_COND_EXT_DMG_NOT_SPECIFIED", "Not Specified", true),
       PLAT_COND_EXT_DMG_SLIGHT = new PL_COND_EXT_DMG(1, "PLAT_COND_EXT_DMG_SLIGHT", "Slight", true),
       PLAT_COND_EXT_DMG_MODERATE = new PL_COND_EXT_DMG(2, "PLAT_COND_EXT_DMG_MODERATE", "Moderate", true),
       PLAT_COND_EXT_DMG_HEAVY = new PL_COND_EXT_DMG(3, "PLAT_COND_EXT_DMG_HEAVY", "Heavy", true);

    private PL_COND_EXT_DMG(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, PL_COND_EXT_DMG.class);
    }

    /** @see Value#values(Class) */
    public static List<PL_COND_EXT_DMG> values() {

        return values(PL_COND_EXT_DMG.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<PL_COND_EXT_DMG> values(boolean known) {

        return values(PL_COND_EXT_DMG.class, known);
    }

    /** @see Value#get(int, Class) */
    public static PL_COND_EXT_DMG get(int value) {

        return get(value, PL_COND_EXT_DMG.class);
    }
}

