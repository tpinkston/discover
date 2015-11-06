package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum LF_COND_CLEAN implements EnumInterface {

    LIFE_COND_CLEAN_NOT_SPECIFIED(0, "Not Specified"),
    LIFE_COND_CLEAN_SLIGHTLY_DIRTY(1, "Slightly Dirty"),
    LIFE_COND_CLEAN_MODERATELY_DIRTY(2, "Moderately Dirty"),
    LIFE_COND_CLEAN_VERY_DIRTY(3, "Very Dirty");

    private final int value;
    private final String description;

    private LF_COND_CLEAN(int value, String description) {

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
