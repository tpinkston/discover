package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum IFF_SYSTEM_MODE implements EnumInterface {

    IFF_SYS_MODE_NO_STATEMENT(0, "No Statement"),
    IFF_SYS_MODE_OFF(1, "Off"),
    IFF_SYS_MODE_STANDBY(2, "Standby"),
    IFF_SYS_MODE_NORMAL(3, "Normal"),
    IFF_SYS_MODE_EMERGENCY(4, "Emergency");

    private final int value;
    private final String description;

    private IFF_SYSTEM_MODE(int value, String description) {

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
