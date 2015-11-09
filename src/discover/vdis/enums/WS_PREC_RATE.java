package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_PREC_RATE implements EnumInterface {

    WSP_RATE_NO_PRECIPITATION(0, "No Precipitation"),
    WSP_RATE_LIGHT_PRECIPITATION(1, "Light Precipitation"),
    WSP_RATE_MODERATE_PRECIPITATION(2, "Moderate Precipitation"),
    WSP_RATE_HEAVY_PRECIPITATION(3, "Heavy Precipitation");

    private final int value;
    private final String description;

    private WS_PREC_RATE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, WS_PREC_RATE.class);
    }
}

