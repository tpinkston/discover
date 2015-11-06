package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_CLOUDS_STATUS implements EnumInterface {

    WSC_CLOUD_STATUS_OFF(0, "Off"),
    WSC_CLOUD_STATUS_ON(1, "On");

    private final int value;
    private final String description;

    private WS_CLOUDS_STATUS(int value, String description) {

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

