package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum JAMMING_TECHNIQUE implements EnumInterface {

    0(0, "0"),
    0_1(1, "0"),
    0_2(2, "0");

    private final int value;
    private final String description;

    private JAMMING_TECHNIQUE(int value, String description) {

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

