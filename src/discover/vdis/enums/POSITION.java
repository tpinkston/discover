package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum POSITION implements EnumInterface {

    POSITION_OTHER(0, "Other"),
    POSITION_ON_TOP_OF(1, "On Top Of"),
    POSITION_INSIDE_OF(2, "Inside Of");

    private final int value;
    private final String description;

    private POSITION(int value, String description) {

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

