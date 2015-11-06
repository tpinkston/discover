package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum LF_COND_MTL implements EnumInterface {

    LIFE_COND_MTL_NOT_SPECIFIED(0, "Not Specified"),
    LIFE_COND_MTL_CLOTH(1, "Cloth"),
    LIFE_COND_MTL_LEATHER(2, "Leather"),
    LIFE_COND_MTL_RUBBER(3, "Rubber");

    private final int value;
    private final String description;

    private LF_COND_MTL(int value, String description) {

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

