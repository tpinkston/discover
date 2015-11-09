package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_THUN_STATUS implements EnumInterface {

    WST_THUNDER_STATUS_OFF(0, "Off"),
    WST_THUNDER_STATUS_INSTANTANEOUS(1, "Instantaneous Thunder"),
    WST_THUNDER_STATUS_RANDOM(2, "Random Thunder"),
    WST_THUNDER_STATUS_COINCIDENT_LIGHTNING(3, "Thunder Coincident with Lightning"),
    WST_THUNDER_STATUS_OVERCAST(4, "Overcast");

    private final int value;
    private final String description;

    private WS_THUN_STATUS(int value, String description) {

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

        return Enumerations.getUnknownValue(value, WS_THUN_STATUS.class);
    }
}

