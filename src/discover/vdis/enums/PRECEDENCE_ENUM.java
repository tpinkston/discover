package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PRECEDENCE_ENUM implements EnumInterface {

    PRECEDENCE_UNDEFINED(0, "Undefined"),
    PRECEDENCE_EMERGENCY(2, "Emergency"),
    PRECEDENCE_FLASH(4, "Flash"),
    PRECEDENCE_IMMEDIATE(5, "Immediate"),
    PRECEDENCE_PRIORITY(6, "Priority"),
    PRECEDENCE_ROUTINE(7, "Routine");

    private final int value;
    private final String description;

    private PRECEDENCE_ENUM(int value, String description) {

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

        return Enumerations.getUnknownValue(value, PRECEDENCE_ENUM.class);
    }
}

