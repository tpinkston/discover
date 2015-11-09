package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DESTINATION_LINE implements EnumInterface {

    DESTINATION_NONE(0, "None"),
    DESTINATION_SET_LINE_STATE_TRANSMITTING(1, "Set Line State - Transmitting"),
    DESTINATION_SET_LINE_STATE_NOT_TRANSMITTING(2, "Set Line State - Not Transmitting"),
    DESTINATION_RETURN_TO_LOCAL_LINE_STATE_CONTROL(3, "Return to Local Line State Control");

    private final int value;
    private final String description;

    private DESTINATION_LINE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, DESTINATION_LINE.class);
    }
}

