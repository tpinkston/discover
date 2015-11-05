package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum MUN_CAT implements EnumInterface {

    OTH(0, "Other"),
    GUID(1, "Guided"),
    BALL(2, "Ballistic"),
    FIX(3, "Fixed");

    private final int value;
    private final String description;

    private MUN_CAT(int value, String description) {

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

