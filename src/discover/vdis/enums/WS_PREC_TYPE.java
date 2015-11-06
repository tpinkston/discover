package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_PREC_TYPE implements EnumInterface {

    WSP_TYPE_NONE(0, "None"),
    WSP_TYPE_OTHER(1, "Other"),
    WSP_TYPE_RAIN(2, "Rain"),
    WSP_TYPE_SNOW(3, "Snow"),
    WSP_TYPE_HAIL(4, "Hail"),
    WSP_TYPE_SLEET(5, "Sleet"),
    WSP_TYPE_FREEZING_RAIN(6, "Freezing Rain");

    private final int value;
    private final String description;

    private WS_PREC_TYPE(int value, String description) {

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

