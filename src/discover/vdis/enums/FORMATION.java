package discover.vdis.enums;

import java.util.List;

/**
 * FORMATION: This class is auto-generated by vdis.EnumGenerator
 */
public final class FORMATION extends Value {

    public static final FORMATION
       OTHER = new FORMATION(0, "OTHER", "Other", true),
       ASSEMBLY = new FORMATION(1, "ASSEMBLY", "Assembly", true),
       VEE = new FORMATION(2, "VEE", "Vee", true),
       WEDGE = new FORMATION(3, "WEDGE", "Wedge", true),
       LINE = new FORMATION(4, "LINE", "Line", true),
       COLUMN = new FORMATION(5, "COLUMN", "Column", true);

    private FORMATION(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, FORMATION.class);
    }

    /** @see Value#values(Class) */
    public static List<FORMATION> values() {

        return values(FORMATION.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<FORMATION> values(boolean known) {

        return values(FORMATION.class, known);
    }

    /** @see Value#get(int, Class) */
    public static FORMATION get(int value) {

        return get(value, FORMATION.class);
    }
}

