package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum COORD_SYSTEM implements EnumInterface {

    COORD_SYS_RIGHT_HANDED_CARTESIAN(0, "Right Handed Cartesian"),
    COORD_SYS_LEFT_HANDED_CARTESIAN(1, "Left Handed Cartesian"),
    COORD_SYS_LATITUDE_LONGITUDE_HEIGHT(2, "Latitude, Longitude, Height"),
    COORD_SYS_LATITUDE_LONGITUDE_DEPTH(3, "Latitude, Longitude, Depth");

    private final int value;
    private final String description;

    private COORD_SYSTEM(int value, String description) {

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

