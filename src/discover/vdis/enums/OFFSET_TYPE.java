package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum OFFSET_TYPE implements EnumInterface {

    OFFVP_OFFSET_TYPE_CARRIER_ORIGIN(0, "Carrier Origin"),
    OFFVP_OFFSET_TYPE_STATION_LOC(1, "Station Location"),
    OFFVP_OFFSET_TYPE_POS_MOD_BY_CARRIER(2, "Position Modified by Carrier"),
    OFFVP_OFFSET_TYPE_ORIENT_SET_BY_CARRIER(3, "Orientation Modified by Carrier"),
    OFFVP_OFFSET_TYPE_CENTER_OF_GRAVITY(100, "Center of Gravity");

    private final int value;
    private final String description;

    private OFFSET_TYPE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, OFFSET_TYPE.class);
    }
}

