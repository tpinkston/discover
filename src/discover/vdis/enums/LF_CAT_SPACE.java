package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum LF_CAT_SPACE implements EnumInterface {

    OTH(0, "Other"),
    ASTRO(1, "Astronaut (with)");

    private final int value;
    private final String description;

    private LF_CAT_SPACE(int value, String description) {

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

