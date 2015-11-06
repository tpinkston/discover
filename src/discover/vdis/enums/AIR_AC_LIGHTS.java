package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum AIR_AC_LIGHTS implements EnumInterface {

    AIRPLAT_AC_LIGHTS_DIM(0, "Dim (night)"),
    AIRPLAT_AC_LIGHTS_BRIGHT(1, "Bright (day)"),
    AIRPLAT_AC_LIGHTS_FLASHING_DIM(2, "Flashing (dim/night)"),
    AIRPLAT_AC_LIGHTS_FLASHING_BRIGHT(3, "Flashing (bright/day)");

    private final int value;
    private final String description;

    private AIR_AC_LIGHTS(int value, String description) {

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

