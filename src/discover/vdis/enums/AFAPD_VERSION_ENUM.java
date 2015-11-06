package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum AFAPD_VERSION_ENUM implements EnumInterface {

    AFAPD_VERSION_AV(0, "AFAPD AV"),
    AFAPD_VERSION_BG(1, "AFAPD BG");

    private final int value;
    private final String description;

    private AFAPD_VERSION_ENUM(int value, String description) {

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

