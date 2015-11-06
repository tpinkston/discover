package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum TERMINAL_SECONDARY implements EnumInterface {

    TERMINAL_SECONDARY_NONE(0, "None "),
    TERMINAL_SECONDARY_NET_POSITION_REFERENCE(1, "Net Position Reference "),
    TERMINAL_SECONDARY_PRIMARY_NAVIGATION_CONTROLLER(2, "Primary Navigation Controller "),
    TERMINAL_SECONDARY_SECONDARY_NAVIGATION_CONTROLLER(3, "Secondary Navigation Controller");

    private final int value;
    private final String description;

    private TERMINAL_SECONDARY(int value, String description) {

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

