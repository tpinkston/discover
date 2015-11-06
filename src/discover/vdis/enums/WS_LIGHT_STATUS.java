package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_LIGHT_STATUS implements EnumInterface {

    WSL_LIGHTING_STATUS_OFF(0, "Off"),
    WSL_LIGHTING_STATUS_INSTANTANEOUS(1, "Instantaneous Lightning"),
    WSL_LIGHTING_STATUS_RANDOM(2, "Random Lightning");

    private final int value;
    private final String description;

    private WS_LIGHT_STATUS(int value, String description) {

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

