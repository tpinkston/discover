package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DATA_REP implements EnumInterface {

    DATA_REP_TYPE_0(0, "Type 0"),
    DATA_REP_TYPE_1(1, "Type 1"),
    DATA_REP_TYPE_2(2, "Type 2");

    private final int value;
    private final String description;

    private DATA_REP(int value, String description) {

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

