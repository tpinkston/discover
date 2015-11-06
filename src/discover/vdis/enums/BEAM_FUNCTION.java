package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum BEAM_FUNCTION implements EnumInterface {

    BEAM_FUNC_OTHER(0, "Other"),
    BEAM_FUNC_SEARCH(1, "Search"),
    BEAM_FUNC_HEIGHT_FINDER(2, "Height finder"),
    BEAM_FUNC_ACQUISITION(3, "Acquisition"),
    BEAM_FUNC_TRACKING(4, "Tracking"),
    BEAM_FUNC_ACQUISITION_AND_TRACKING(5, "Acquisition and tracking"),
    BEAM_FUNC_COMMAND_GUIDANCE(6, "Command guidance"),
    BEAM_FUNC_ILLUMINATION(7, "Illumination"),
    BEAM_FUNC_RANGE_ONLY_RADAR(8, "Range only radar"),
    BEAM_FUNC_MISSILE_BEACON(9, "Missile beacon"),
    BEAM_FUNC_MISSILE_FUZE(10, "Missile fuze"),
    BEAM_FUNC_ACTIVE_RADAR_MISSILE_SEEKER(11, "Active radar missile seeker"),
    BEAM_FUNC_JAMMER(12, "Jammer"),
    BEAM_FUNC_IFF(13, "IFF"),
    BEAM_FUNC_NAVIGATIONAL_WEATHER(14, "Navigational/Weather"),
    BEAM_FUNC_METEOROLOGICAL(15, "Meteorological"),
    BEAM_FUNC_DATA_TRANSMISSION(16, "Data transmission"),
    BEAM_FUNC_NAVIGATIONAL_DIRECTIONAL_BEACON(17, "Navigational directional beacon"),
    BEAM_FUNC_TIME_SHARED_SEARCH(20, "Time-Shared Search"),
    BEAM_FUNC_TIME_SHARED_ACQUISITION(21, "Time-Shared Acquisition"),
    BEAM_FUNC_TIME_SHARED_TRACK(22, "Time-Shared Track"),
    BEAM_FUNC_TIME_SHARED_COMMAND_GUIDANCE(23, "Time-Shared Command Guidance"),
    BEAM_FUNC_TIME_SHARED_ILLUMINATION(24, "Time-Shared Illumination"),
    BEAM_FUNC_TIME_SHARED_JAMMING(25, "Time-Shared Jamming");

    private final int value;
    private final String description;

    private BEAM_FUNCTION(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getDescription() {

        return description;
    }
}

