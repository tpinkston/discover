package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum TRANSMIT_STATE implements EnumInterface {

    TRANSMIT_OFF(0, "Off"),
    TRANSMIT_ON_BUT_NOT_TRANSMITTING(1, "On But Not Transmitting"),
    TRANSMIT_ON_AND_TRANSMITTING(2, "On And Transmitting");

    private final int value;
    private final String description;

    private TRANSMIT_STATE(int value, String description) {

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

