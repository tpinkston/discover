package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

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
    public String getName() {

        return name();
    }

    @Override
    public String getDescription() {

        return description;
    }

    public static EnumInterface getValue(int value) {

        for(EnumInterface element : values()) {

            if (element.getValue() == value) {

                return element;
            }
        }

        return Enumerations.getUnknownValue(value, RADAR_TRACK.class);
    }
}

