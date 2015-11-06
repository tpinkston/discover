package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum CLEAR_CHANNEL implements EnumInterface {

    CLEAR_NOT_CLEAR_CHANNEL(0, "Not Clear Channel"),
    CLEAR_CLEAR_CHANNEL(1, "Clear Channel");

    private final int value;
    private final String description;

    private CLEAR_CHANNEL(int value, String description) {

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

