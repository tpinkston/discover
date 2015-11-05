package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum CF_CAT_SUBSURFACE implements EnumInterface {

    OTH(0, "Other"),
    OIL(1, "Oil Derrick"),
    BUOY(2, "Buoy"),
    SHIP(3, "Sunken Ship");

    private final int value;
    private final String description;

    private CF_CAT_SUBSURFACE(int value, String description) {

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

