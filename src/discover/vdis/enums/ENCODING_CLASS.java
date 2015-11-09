package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ENCODING_CLASS implements EnumInterface {

    ENCODING_CLASS_ENCODED_AUDIO(0, "Encoded Audio"),
    ENCODING_CLASS_RAW_BINARY_DATA(1, "Raw Binary Data"),
    ENCODING_CLASS_APPLICATION_SPECIFIC_DATA(2, "Application-Specific Data"),
    ENCODING_CLASS_DATABASE_INDEX(3, "Database Index");

    private final int value;
    private final String description;

    private ENCODING_CLASS(int value, String description) {

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

        return Enumerations.getUnknownValue(value, ENCODING_CLASS.class);
    }
}

