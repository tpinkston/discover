package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum COMMAND implements EnumInterface {

    COMMAND_NO_COMMAND(0, "No Command"),
    COMMAND_STATUS(1, "Status"),
    COMMAND_CONNECT(2, "Connect"),
    COMMAND_DISCONNECT(3, "Disconnect"),
    COMMAND_RESET(4, "Reset"),
    COMMAND_ON(5, "On"),
    COMMAND_OFF(6, "Off");

    private final int value;
    private final String description;

    private COMMAND(int value, String description) {

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

        return Enumerations.getUnknownValue(value, COMMAND.class);
    }
}

