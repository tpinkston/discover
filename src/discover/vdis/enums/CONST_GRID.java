package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum CONST_GRID implements EnumInterface {

    CONST_GRID_CONSTANT_GRID(0, "Constant Grid"),
    CONST_GRID_UPDATED_GRID(1, "Updated Grid");

    private final int value;
    private final String description;

    private CONST_GRID(int value, String description) {

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

