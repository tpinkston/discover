package discover.vdis.enums;

import java.util.List;

/**
 * DOMAIN: This class is auto-generated by vdis.EnumGenerator
 */
public final class DOMAIN extends Value {

    public static final DOMAIN
       OTHER = new DOMAIN(0, "OTHER", "Other", true),
       LAND = new DOMAIN(1, "LAND", "Land", true),
       AIR = new DOMAIN(2, "AIR", "Air", true),
       SURFACE = new DOMAIN(3, "SURFACE", "Surface", true),
       SUBSURFACE = new DOMAIN(4, "SUBSURFACE", "Subsurface", true),
       SPACE = new DOMAIN(5, "SPACE", "Space", true);

    private DOMAIN(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, DOMAIN.class);
    }

    /** @see Value#values(Class) */
    public static List<DOMAIN> values() {

        return values(DOMAIN.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<DOMAIN> values(boolean known) {

        return values(DOMAIN.class, known);
    }

    /** @see Value#get(int, Class) */
    public static DOMAIN get(int value) {

        return get(value, DOMAIN.class);
    }
}

