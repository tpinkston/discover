package discover.vdis.enums;

import java.util.List;

/**
 * LF_COND_EXT_DMG: This class is auto-generated by vdis.EnumGenerator
 */
public final class LF_COND_EXT_DMG extends Value {

    public static final LF_COND_EXT_DMG
       LIFE_COND_EXT_DMG_NOT_SPECIFIED = new LF_COND_EXT_DMG(0, "LIFE_COND_EXT_DMG_NOT_SPECIFIED", "Not Specified", true),
       LIFE_COND_EXT_DMG_SLIGHT = new LF_COND_EXT_DMG(1, "LIFE_COND_EXT_DMG_SLIGHT", "Slight", true),
       LIFE_COND_EXT_DMG_MODERATE = new LF_COND_EXT_DMG(2, "LIFE_COND_EXT_DMG_MODERATE", "Moderate", true),
       LIFE_COND_EXT_DMG_HEAVY = new LF_COND_EXT_DMG(3, "LIFE_COND_EXT_DMG_HEAVY", "Heavy", true);

    private LF_COND_EXT_DMG(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, LF_COND_EXT_DMG.class);
    }

    /** @see Value#values(Class) */
    public static List<LF_COND_EXT_DMG> values() {

        return values(LF_COND_EXT_DMG.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<LF_COND_EXT_DMG> values(boolean known) {

        return values(LF_COND_EXT_DMG.class, known);
    }

    /** @see Value#get(int, Class) */
    public static LF_COND_EXT_DMG get(int value) {

        return get(value, LF_COND_EXT_DMG.class);
    }
}

