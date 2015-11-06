package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum COLLISION implements EnumInterface {

    COLLISION_INELASTIC(0, "Inelastic"),
    COLLISION_ELASTIC(1, "Elastic");

    private final int value;
    private final String description;

    private COLLISION(int value, String description) {

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

