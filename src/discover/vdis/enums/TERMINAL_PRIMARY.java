package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum TERMINAL_PRIMARY implements EnumInterface {

    TERMINAL_PRIMARY_NONE(0, "None"),
    TERMINAL_PRIMARY_NTR(1, "NTR"),
    TERMINAL_PRIMARY_JTIDS_UNIT_PARTICIPANT(2, "JTIDS Unit Participant");

    private final int value;
    private final String description;

    private TERMINAL_PRIMARY(int value, String description) {

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

