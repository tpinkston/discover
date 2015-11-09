package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum SPEED_BUMP_MTL implements EnumInterface {

    MTL_SPEED_BUMP_NOT_SPECIFIED(0, "Not specified"),
    MTL_SPEED_BUMP_PLASTIC(1, "Plastic"),
    MTL_SPEED_BUMP_RUBBER(2, "Rubber"),
    MTL_SPEED_BUMP_ROAD(3, "Road");

    private final int value;
    private final String description;

    private SPEED_BUMP_MTL(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getName() {

        return name();
    }

    @Override
    public String getDescription() {

        return description;
    }

    public static EnumInterface getValue(int value) {

        for(EnumInterface element : values()) {

            if (element.getValue() == value) {

                return element;
            }
        }

        return Enumerations.getUnknownValue(value, SPEED_BUMP_MTL.class);
    }
}

