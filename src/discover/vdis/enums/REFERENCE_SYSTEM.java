package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum REFERENCE_SYSTEM implements EnumInterface {

    REF_UNDEFINED_COORDINATES(0, "Undefined Coordinates"),
    REF_WORLD_COORDINATES(1, "World Coordinates"),
    REF_ENTITY_COORDINATES(2, "Entity Coordinates");

    private final int value;
    private final String description;

    private REFERENCE_SYSTEM(int value, String description) {

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

