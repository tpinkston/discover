package discover.vdis.enums;

import java.util.List;

/**
 * STEALTH_SPECTRUM_TYPE: This class is auto-generated by vdis.EnumGenerator
 */
public final class STEALTH_SPECTRUM_TYPE extends Value {

    public static final STEALTH_SPECTRUM_TYPE
       OTW = new STEALTH_SPECTRUM_TYPE(0, "OTW", "Out the Window (OTW)", true),
       DAY_TV = new STEALTH_SPECTRUM_TYPE(1, "DAY_TV", "Day TV", true),
       THERMAL_MANUAL = new STEALTH_SPECTRUM_TYPE(2, "THERMAL_MANUAL", "Thermal Manual", true),
       THERMAL_AUTO = new STEALTH_SPECTRUM_TYPE(3, "THERMAL_AUTO", "Thermal Automatic", true),
       THERMAL_QUARTER = new STEALTH_SPECTRUM_TYPE(4, "THERMAL_QUARTER", "Thermal Quarter Resolution", true),
       THERMAL_HALF = new STEALTH_SPECTRUM_TYPE(5, "THERMAL_HALF", "Thermal Half Resolution", true);

    private STEALTH_SPECTRUM_TYPE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, STEALTH_SPECTRUM_TYPE.class);
    }

    /** @see Value#values(Class) */
    public static List<STEALTH_SPECTRUM_TYPE> values() {

        return values(STEALTH_SPECTRUM_TYPE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<STEALTH_SPECTRUM_TYPE> values(boolean known) {

        return values(STEALTH_SPECTRUM_TYPE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static STEALTH_SPECTRUM_TYPE get(int value) {

        return get(value, STEALTH_SPECTRUM_TYPE.class);
    }
}

