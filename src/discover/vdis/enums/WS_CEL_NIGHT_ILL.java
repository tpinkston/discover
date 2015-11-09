package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_CEL_NIGHT_ILL implements EnumInterface {

    WSC_NIGHT_ILL_NONE(0, "None"),
    WSC_NIGHT_ILL_STARLIGHT(1, "Starlight"),
    WSC_NIGHT_ILL_QUARTER_MOON(2, "Quarter Moon"),
    WSC_NIGHT_ILL_HALF_MOON(3, "Half Moon"),
    WSC_NIGHT_ILL_THREE_QUARTER_MOON(4, "Three Quarter Moon"),
    WSC_NIGHT_ILL_FULL_MOON(5, "Full Moon");

    private final int value;
    private final String description;

    private WS_CEL_NIGHT_ILL(int value, String description) {

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

        return Enumerations.getUnknownValue(value, WS_CEL_NIGHT_ILL.class);
    }
}

