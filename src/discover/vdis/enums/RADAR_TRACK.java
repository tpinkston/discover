package discover.vdis.enums;

import java.util.List;

/**
 * RADAR_TRACK: This class is auto-generated by vdis.EnumGenerator
 */
public final class RADAR_TRACK extends Value {

    public static final RADAR_TRACK
       NOT_SPECIFIED = new RADAR_TRACK(0, "NOT_SPECIFIED", "Not Specified", true),
       RADAR_TRACK_DETECTED = new RADAR_TRACK(1, "RADAR_TRACK_DETECTED", "Radar Track Detected", true),
       RADAR_TRACK_BROKEN = new RADAR_TRACK(2, "RADAR_TRACK_BROKEN", "Radar Track Broken", true);

    private RADAR_TRACK(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, RADAR_TRACK.class);
    }

    /** @see Value#values(Class) */
    public static List<RADAR_TRACK> values() {

        return values(RADAR_TRACK.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<RADAR_TRACK> values(boolean known) {

        return values(RADAR_TRACK.class, known);
    }

    /** @see Value#get(int, Class) */
    public static RADAR_TRACK get(int value) {

        return get(value, RADAR_TRACK.class);
    }
}

