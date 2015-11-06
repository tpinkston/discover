package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum AIR_EQ_SLINGLOAD implements EnumInterface {

    AIRPLAT_EQ_SLNGLD_NOT_PRESENT(0, "Not Present"),
    AIRPLAT_EQ_SLNGLD_EMPTY(1, "Empty"),
    AIRPLAT_EQ_SLNGLD_LOADED(2, "Loaded"),
    AIRPLAT_EQ_SLNGLD_DAMAGED(3, "Damaged");

    private final int value;
    private final String description;

    private AIR_EQ_SLINGLOAD(int value, String description) {

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

