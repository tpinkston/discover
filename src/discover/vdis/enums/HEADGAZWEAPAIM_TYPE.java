package discover.vdis.enums;

import java.util.List;

/**
 * HEADGAZWEAPAIM_TYPE: This class is auto-generated by vdis.EnumGenerator
 */
public final class HEADGAZWEAPAIM_TYPE extends Value {

    public static final HEADGAZWEAPAIM_TYPE
       HGWA_TYPE_NOT_SPECIFIED = new HEADGAZWEAPAIM_TYPE(0, "HGWA_TYPE_NOT_SPECIFIED", "Not Specified", true),
       HGWA_TYPE_UP = new HEADGAZWEAPAIM_TYPE(1, "HGWA_TYPE_UP", "Up", true),
       HGWA_TYPE_DOWN = new HEADGAZWEAPAIM_TYPE(2, "HGWA_TYPE_DOWN", "Down", true),
       HGWA_TYPE_LEFT = new HEADGAZWEAPAIM_TYPE(3, "HGWA_TYPE_LEFT", "Left", true),
       HGWA_TYPE_RIGHT = new HEADGAZWEAPAIM_TYPE(4, "HGWA_TYPE_RIGHT", "Right", true),
       HGWA_TYPE_FORWARD = new HEADGAZWEAPAIM_TYPE(5, "HGWA_TYPE_FORWARD", "Forward", true),
       HGWA_TYPE_BACKWARD = new HEADGAZWEAPAIM_TYPE(6, "HGWA_TYPE_BACKWARD", "Backward", true),
       HGWA_TYPE_SPECIFIED_GLOBAL_LOCATION = new HEADGAZWEAPAIM_TYPE(7, "HGWA_TYPE_SPECIFIED_GLOBAL_LOCATION", "Specified Global Location", true),
       HGWA_TYPE_SPECIFIED_ORIENTATION_VECTOR = new HEADGAZWEAPAIM_TYPE(8, "HGWA_TYPE_SPECIFIED_ORIENTATION_VECTOR", "Specified Orientation Vector", true),
       HGWA_TYPE_TRACKED_CENTER = new HEADGAZWEAPAIM_TYPE(9, "HGWA_TYPE_TRACKED_CENTER", "Tracked Center", true),
       HGWA_TYPE_TRACKED_STATION = new HEADGAZWEAPAIM_TYPE(10, "HGWA_TYPE_TRACKED_STATION", "Tracked Station", true),
       HGWA_TYPE_OUT_STATION_NOT_TRACKED = new HEADGAZWEAPAIM_TYPE(11, "HGWA_TYPE_OUT_STATION_NOT_TRACKED", "Out Station Not Tracked", true),
       HGWA_TYPE_OUT_STATION_TRACKED_CENTER = new HEADGAZWEAPAIM_TYPE(12, "HGWA_TYPE_OUT_STATION_TRACKED_CENTER", "Out Station Tracked Center", true),
       HGWA_TYPE_OUT_STATION_TRACKED_STATION = new HEADGAZWEAPAIM_TYPE(13, "HGWA_TYPE_OUT_STATION_TRACKED_STATION", "Out Station Tracked Station", true),
       HGWA_TYPE_STOWED_EYES_CLOSED = new HEADGAZWEAPAIM_TYPE(14, "HGWA_TYPE_STOWED_EYES_CLOSED", "Stowed Eyes Closed", true);

    private HEADGAZWEAPAIM_TYPE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, HEADGAZWEAPAIM_TYPE.class);
    }

    /** @see Value#values(Class) */
    public static List<HEADGAZWEAPAIM_TYPE> values() {

        return values(HEADGAZWEAPAIM_TYPE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<HEADGAZWEAPAIM_TYPE> values(boolean known) {

        return values(HEADGAZWEAPAIM_TYPE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static HEADGAZWEAPAIM_TYPE get(int value) {

        return get(value, HEADGAZWEAPAIM_TYPE.class);
    }
}

