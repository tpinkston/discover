package discover.vdis.enums;

import java.util.List;

/**
 * COORD_SYSTEM: This class is auto-generated by vdis.EnumGenerator
 */
public final class COORD_SYSTEM extends Value {

    public static final COORD_SYSTEM
       COORD_SYS_RIGHT_HANDED_CARTESIAN = new COORD_SYSTEM(0, "COORD_SYS_RIGHT_HANDED_CARTESIAN", "Right Handed Cartesian", true),
       COORD_SYS_LEFT_HANDED_CARTESIAN = new COORD_SYSTEM(1, "COORD_SYS_LEFT_HANDED_CARTESIAN", "Left Handed Cartesian", true),
       COORD_SYS_LATITUDE_LONGITUDE_HEIGHT = new COORD_SYSTEM(2, "COORD_SYS_LATITUDE_LONGITUDE_HEIGHT", "Latitude, Longitude, Height", true),
       COORD_SYS_LATITUDE_LONGITUDE_DEPTH = new COORD_SYSTEM(3, "COORD_SYS_LATITUDE_LONGITUDE_DEPTH", "Latitude, Longitude, Depth", true);

    private COORD_SYSTEM(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, COORD_SYSTEM.class);
    }

    /** @see Value#values(Class) */
    public static List<COORD_SYSTEM> values() {

        return values(COORD_SYSTEM.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<COORD_SYSTEM> values(boolean known) {

        return values(COORD_SYSTEM.class, known);
    }

    /** @see Value#get(int, Class) */
    public static COORD_SYSTEM get(int value) {

        return get(value, COORD_SYSTEM.class);
    }
}

