package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum LF_SEC_COND_FACIAL implements EnumInterface {

    LIFE_SEC_COND_FACIAL_NOT_SPECIFIED(0, "Not Specified"),
    LIFE_SEC_COND_FACIAL_CLEAN_SHAVEN(1, "Clean Shaven"),
    LIFE_SEC_COND_FACIAL_SHORT_BEARD(2, "Short Beard"),
    LIFE_SEC_COND_FACIAL_LONG_BEARD(3, "Long Beard");

    private final int value;
    private final String description;

    private LF_SEC_COND_FACIAL(int value, String description) {

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

