package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PL_COND_CLEAN implements EnumInterface {

    PLAT_COND_CLEAN_NOT_SPECIFIED(0, "Not Specified"),
    PLAT_COND_CLEAN_SLIGHTLY_DIRTY(1, "Slightly Dirty"),
    PLAT_COND_CLEAN_MODERATELY_DIRTY(2, "Moderately Dirty"),
    PLAT_COND_CLEAN_VERY_DIRTY(3, "Very Dirty");

    private final int value;
    private final String description;

    private PL_COND_CLEAN(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getName() {

        return name();
    }

    @Override
    public String getDescription() {

        return description;
    }

    public static EnumInterface getValue(int value) {

        for(EnumInterface element : values()) {

            if (element.getValue() == value) {

                return element;
            }
        }

        return Enumerations.getUnknownValue(value, PL_COND_CLEAN.class);
    }
}

