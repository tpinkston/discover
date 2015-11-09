package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ENTITY_MARKING implements EnumInterface {

    ENTITY_MARKING_UNUSED(0, "Unused"),
    ENTITY_MARKING_ASCII(1, "ASCII"),
    ENTITY_MARKING_US_ARMY(2, "U.S. Army Marking"),
    ENTITY_MARKING_DIGIT_CHEVRON(3, "Digit Chevron");

    private final int value;
    private final String description;

    private ENTITY_MARKING(int value, String description) {

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

        return Enumerations.getUnknownValue(value, ENTITY_MARKING.class);
    }
}

