package discover.vdis.enums;

import java.util.List;

/**
 * ANTENNA_PATTERN_TYPE: This class is auto-generated by vdis.EnumGenerator
 */
public final class ANTENNA_PATTERN_TYPE extends Value {

    public static final ANTENNA_PATTERN_TYPE
       ANTENNA_PATTERN_OMNI_DIRECTIONAL = new ANTENNA_PATTERN_TYPE(0, "ANTENNA_PATTERN_OMNI_DIRECTIONAL", "Omni-Directional", true),
       ANTENNA_PATTERN_BEAM = new ANTENNA_PATTERN_TYPE(1, "ANTENNA_PATTERN_BEAM", "Beam", true),
       ANTENNA_PATTERN_SPHERICAL_HARMONIC = new ANTENNA_PATTERN_TYPE(2, "ANTENNA_PATTERN_SPHERICAL_HARMONIC", "Spherical Harmonic", true);

    private ANTENNA_PATTERN_TYPE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, ANTENNA_PATTERN_TYPE.class);
    }

    /** @see Value#values(Class) */
    public static List<ANTENNA_PATTERN_TYPE> values() {

        return values(ANTENNA_PATTERN_TYPE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<ANTENNA_PATTERN_TYPE> values(boolean known) {

        return values(ANTENNA_PATTERN_TYPE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static ANTENNA_PATTERN_TYPE get(int value) {

        return get(value, ANTENNA_PATTERN_TYPE.class);
    }
}

