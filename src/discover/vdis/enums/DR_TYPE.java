package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DR_TYPE implements EnumInterface {

    DRVP_DR_TYPE_NOT_SPECIFIED(0, "Not Specified"),
    DRVP_DR_TYPE_LINEAR_VELOCITY(1, "Linear Velocity"),
    DRVP_DR_TYPE_LINEAR_ACCELERATION(2, "Linear Acceleration"),
    DRVP_DR_TYPE_ANGULAR_VELOCITY(3, "Angular Velocity");

    private final int value;
    private final String description;

    private DR_TYPE(int value, String description) {

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

