package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum HEADGAZWEAPAIM_TYPE implements EnumInterface {

    HGWA_TYPE_NOT_SPECIFIED(0, "Not Specified"),
    HGWA_TYPE_UP(1, "Up"),
    HGWA_TYPE_DOWN(2, "Down"),
    HGWA_TYPE_LEFT(3, "Left"),
    HGWA_TYPE_RIGHT(4, "Right"),
    HGWA_TYPE_FORWARD(5, "Forward"),
    HGWA_TYPE_BACKWARD(6, "Backward"),
    HGWA_TYPE_SPECIFIED_GLOBAL_LOCATION(7, "Specified Global Location"),
    HGWA_TYPE_SPECIFIED_ORIENTATION_VECTOR(8, "Specified Orientation Vector"),
    HGWA_TYPE_TRACKED_CENTER(9, "Tracked Center"),
    HGWA_TYPE_TRACKED_STATION(10, "Tracked Station"),
    HGWA_TYPE_OUT_STATION_NOT_TRACKED(11, "Out Station Not Tracked"),
    HGWA_TYPE_OUT_STATION_TRACKED_CENTER(12, "Out Station Tracked Center"),
    HGWA_TYPE_OUT_STATION_TRACKED_STATION(13, "Out Station Tracked Station"),
    HGWA_TYPE_STOWED_EYES_CLOSED(14, "Stowed Eyes Closed");

    private final int value;
    private final String description;

    private HEADGAZWEAPAIM_TYPE(int value, String description) {

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

