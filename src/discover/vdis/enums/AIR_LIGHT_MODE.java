package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum AIR_LIGHT_MODE implements EnumInterface {

    AIRPLAT_LIGHT_MODE_OFF(0, "Off"),
    AIRPLAT_LIGHT_MODE_DIM(1, "Dim"),
    AIRPLAT_LIGHT_MODE_BRIGHT(2, "Bright"),
    AIRPLAT_LIGHT_MODE_FLASHING(3, "Flashing");

    private final int value;
    private final String description;

    private AIR_LIGHT_MODE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, AIR_LIGHT_MODE.class);
    }
}

