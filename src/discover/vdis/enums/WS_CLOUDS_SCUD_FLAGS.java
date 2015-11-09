package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_CLOUDS_SCUD_FLAGS implements EnumInterface {

    WSC_SCUD_FLAGS_OFF(0, "Off"),
    WSC_SCUD_FLAGS_BOTTOM(1, "Bottom"),
    WSC_SCUD_FLAGS_TOP_ON(2, "Top On"),
    WSC_SCUD_FLAGS_TOP_AND_BOTTOM(3, "Top and Bottom");

    private final int value;
    private final String description;

    private WS_CLOUDS_SCUD_FLAGS(int value, String description) {

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

        return Enumerations.getUnknownValue(value, WS_CLOUDS_SCUD_FLAGS.class);
    }
}

