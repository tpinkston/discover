package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum STEALTH_ATTACH_COMMAND implements EnumInterface {

    STEALTH_ATTACH_COM_SPECIFIC(0, "Specific"),
    STEALTH_ATTACH_COM_ANY(1, "Any"),
    STEALTH_ATTACH_COM_DETACH(2, "Detach");

    private final int value;
    private final String description;

    private STEALTH_ATTACH_COMMAND(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getDescription() {

        return description;
    }
}

