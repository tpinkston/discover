package discover.vdis.enums;

import java.util.List;

/**
 * LF_WEAPON_STATE: This class is auto-generated by vdis.EnumGenerator
 */
public final class LF_WEAPON_STATE extends Value {

    public static final LF_WEAPON_STATE
       NONE = new LF_WEAPON_STATE(0, "NONE", "None", true),
       STOWED = new LF_WEAPON_STATE(1, "STOWED", "Stowed", true),
       DEPLOYED = new LF_WEAPON_STATE(2, "DEPLOYED", "Deployed", true),
       FIRING_POSITION = new LF_WEAPON_STATE(3, "FIRING_POSITION", "Firing Position", true);

    private LF_WEAPON_STATE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, LF_WEAPON_STATE.class);
    }

    /** @see Value#values(Class) */
    public static List<LF_WEAPON_STATE> values() {

        return values(LF_WEAPON_STATE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<LF_WEAPON_STATE> values(boolean known) {

        return values(LF_WEAPON_STATE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static LF_WEAPON_STATE get(int value) {

        return get(value, LF_WEAPON_STATE.class);
    }
}

