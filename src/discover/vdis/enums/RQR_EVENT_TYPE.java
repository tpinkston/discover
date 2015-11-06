package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum RQR_EVENT_TYPE implements EnumInterface {

    RQR_EVENT_TYPE_OTHER(0, "Other"),
    RQR_EVENT_TYPE_INTERNAL_ES_DATA(1, "Internal Entity State Data");

    private final int value;
    private final String description;

    private RQR_EVENT_TYPE(int value, String description) {

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

