package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_GR_FOG_STATUS implements EnumInterface {

    WSGF_FOG_STATUS_OFF(0, "Off"),
    WSGF_FOG_STATUS_ON(1, "On");

    private final int value;
    private final String description;

    private WS_GR_FOG_STATUS(int value, String description) {

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

