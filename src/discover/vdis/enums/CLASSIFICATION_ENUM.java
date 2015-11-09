package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum CLASSIFICATION_ENUM implements EnumInterface {

    UNCLASSIFIED(0, "Unclassified"),
    CONFIDENTIAL(1, "Confidential"),
    SECRET(2, "Secret"),
    TOP_SECRET(3, "Top Secret");

    private final int value;
    private final String description;

    private CLASSIFICATION_ENUM(int value, String description) {

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

        return Enumerations.getUnknownValue(value, CLASSIFICATION_ENUM.class);
    }
}

