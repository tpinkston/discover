package discover.vdis.enums;

import java.util.List;

/**
 * BEAM_FUNCTION: This class is auto-generated by vdis.EnumGenerator
 */
public final class BEAM_FUNCTION extends Value {

    public static final BEAM_FUNCTION
       BEAM_FUNC_OTHER = new BEAM_FUNCTION(0, "BEAM_FUNC_OTHER", "Other", true),
       BEAM_FUNC_SEARCH = new BEAM_FUNCTION(1, "BEAM_FUNC_SEARCH", "Search", true),
       BEAM_FUNC_HEIGHT_FINDER = new BEAM_FUNCTION(2, "BEAM_FUNC_HEIGHT_FINDER", "Height finder", true),
       BEAM_FUNC_ACQUISITION = new BEAM_FUNCTION(3, "BEAM_FUNC_ACQUISITION", "Acquisition", true),
       BEAM_FUNC_TRACKING = new BEAM_FUNCTION(4, "BEAM_FUNC_TRACKING", "Tracking", true),
       BEAM_FUNC_ACQUISITION_AND_TRACKING = new BEAM_FUNCTION(5, "BEAM_FUNC_ACQUISITION_AND_TRACKING", "Acquisition and tracking", true),
       BEAM_FUNC_COMMAND_GUIDANCE = new BEAM_FUNCTION(6, "BEAM_FUNC_COMMAND_GUIDANCE", "Command guidance", true),
       BEAM_FUNC_ILLUMINATION = new BEAM_FUNCTION(7, "BEAM_FUNC_ILLUMINATION", "Illumination", true),
       BEAM_FUNC_RANGE_ONLY_RADAR = new BEAM_FUNCTION(8, "BEAM_FUNC_RANGE_ONLY_RADAR", "Range only radar", true),
       BEAM_FUNC_MISSILE_BEACON = new BEAM_FUNCTION(9, "BEAM_FUNC_MISSILE_BEACON", "Missile beacon", true),
       BEAM_FUNC_MISSILE_FUZE = new BEAM_FUNCTION(10, "BEAM_FUNC_MISSILE_FUZE", "Missile fuze", true),
       BEAM_FUNC_ACTIVE_RADAR_MISSILE_SEEKER = new BEAM_FUNCTION(11, "BEAM_FUNC_ACTIVE_RADAR_MISSILE_SEEKER", "Active radar missile seeker", true),
       BEAM_FUNC_JAMMER = new BEAM_FUNCTION(12, "BEAM_FUNC_JAMMER", "Jammer", true),
       BEAM_FUNC_IFF = new BEAM_FUNCTION(13, "BEAM_FUNC_IFF", "IFF", true),
       BEAM_FUNC_NAVIGATIONAL_WEATHER = new BEAM_FUNCTION(14, "BEAM_FUNC_NAVIGATIONAL_WEATHER", "Navigational/Weather", true),
       BEAM_FUNC_METEOROLOGICAL = new BEAM_FUNCTION(15, "BEAM_FUNC_METEOROLOGICAL", "Meteorological", true),
       BEAM_FUNC_DATA_TRANSMISSION = new BEAM_FUNCTION(16, "BEAM_FUNC_DATA_TRANSMISSION", "Data transmission", true),
       BEAM_FUNC_NAVIGATIONAL_DIRECTIONAL_BEACON = new BEAM_FUNCTION(17, "BEAM_FUNC_NAVIGATIONAL_DIRECTIONAL_BEACON", "Navigational directional beacon", true),
       BEAM_FUNC_TIME_SHARED_SEARCH = new BEAM_FUNCTION(20, "BEAM_FUNC_TIME_SHARED_SEARCH", "Time-Shared Search", true),
       BEAM_FUNC_TIME_SHARED_ACQUISITION = new BEAM_FUNCTION(21, "BEAM_FUNC_TIME_SHARED_ACQUISITION", "Time-Shared Acquisition", true),
       BEAM_FUNC_TIME_SHARED_TRACK = new BEAM_FUNCTION(22, "BEAM_FUNC_TIME_SHARED_TRACK", "Time-Shared Track", true),
       BEAM_FUNC_TIME_SHARED_COMMAND_GUIDANCE = new BEAM_FUNCTION(23, "BEAM_FUNC_TIME_SHARED_COMMAND_GUIDANCE", "Time-Shared Command Guidance", true),
       BEAM_FUNC_TIME_SHARED_ILLUMINATION = new BEAM_FUNCTION(24, "BEAM_FUNC_TIME_SHARED_ILLUMINATION", "Time-Shared Illumination", true),
       BEAM_FUNC_TIME_SHARED_JAMMING = new BEAM_FUNCTION(25, "BEAM_FUNC_TIME_SHARED_JAMMING", "Time-Shared Jamming", true);

    private BEAM_FUNCTION(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, BEAM_FUNCTION.class);
    }

    /** @see Value#values(Class) */
    public static List<BEAM_FUNCTION> values() {

        return values(BEAM_FUNCTION.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<BEAM_FUNCTION> values(boolean known) {

        return values(BEAM_FUNCTION.class, known);
    }

    /** @see Value#get(int, Class) */
    public static BEAM_FUNCTION get(int value) {

        return get(value, BEAM_FUNCTION.class);
    }
}

