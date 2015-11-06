package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_PREC_RAIN implements EnumInterface {

    WSP_RAIN_OFF(0, "Off"),
    WSP_RAIN_ON(1, "On");

    private final int value;
    private final String description;

    private WS_PREC_RAIN(int value, String description) {

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

