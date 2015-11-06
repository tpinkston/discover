package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum RADAR_TRACK implements EnumInterface {

    RADAR_TRACK_NOT_SPECIFIED(0, "Not Specified"),
    RADAR_TRACK_RADAR_TRACK_DETECTED(1, "Radar Track Detected"),
    RADAR_TRACK_RADAR_TRACK_BROKEN(2, "Radar Track Broken");

    private final int value;
    private final String description;

    private RADAR_TRACK(int value, String description) {

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

