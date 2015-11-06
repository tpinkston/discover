package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum STEALTHSTATE_SENSOR_MODE implements EnumInterface {

    STEALTHSTATE_SENSOR_MODE_OTHER(0, "Other"),
    STEALTHSTATE_SENSOR_MODE_OTW(1, "Out the Window (OTW)"),
    STEALTHSTATE_SENSOR_MODE_IR(2, "Infrared (IR)"),
    STEALTHSTATE_SENSOR_MODE_NVG(3, "Night Vision Goggles (NVG)");

    private final int value;
    private final String description;

    private STEALTHSTATE_SENSOR_MODE(int value, String description) {

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
