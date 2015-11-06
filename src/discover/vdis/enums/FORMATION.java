package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum FORMATION implements EnumInterface {

    FORMATION_OTHER(0, "Other"),
    FORMATION_ASSEMBLY(1, "Assembly"),
    FORMATION_VEE(2, "Vee"),
    FORMATION_WEDGE(3, "Wedge"),
    FORMATION_LINE(4, "Line"),
    FORMATION_COLUMN(5, "Column");

    private final int value;
    private final String description;

    private FORMATION(int value, String description) {

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
