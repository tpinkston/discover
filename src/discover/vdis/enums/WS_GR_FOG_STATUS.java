package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_GR_FOG_STATUS implements EnumInterface {

    WSGF_FOG_STATUS_OFF(0, "Off"),
    WSGF_FOG_STATUS_ON(1, "On");

    private final int value;
    private final String description;

    private WS_GR_FOG_STATUS(int value, String description) {

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

        return Enumerations.getUnknownValue(value, WS_GR_FOG_STATUS.class);
    }
}

