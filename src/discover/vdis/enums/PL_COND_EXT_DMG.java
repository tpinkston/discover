package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PL_COND_EXT_DMG implements EnumInterface {

    PLAT_COND_EXT_DMG_NOT_SPECIFIED(0, "Not Specified"),
    PLAT_COND_EXT_DMG_SLIGHT(1, "Slight"),
    PLAT_COND_EXT_DMG_MODERATE(2, "Moderate"),
    PLAT_COND_EXT_DMG_HEAVY(3, "Heavy");

    private final int value;
    private final String description;

    private PL_COND_EXT_DMG(int value, String description) {

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

