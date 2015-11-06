package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum TRANSMIT_LINE implements EnumInterface {

    TRANSMIT_TRANSMIT_LINE_STATE_NOT_APPLICABLE(0, "Transmit Line State Not Applicable"),
    TRANSMIT_NOT_TRANSMITTING(1, "Not Transmitting"),
    TRANSMIT_TRANSMITTING(2, "Transmitting");

    private final int value;
    private final String description;

    private TRANSMIT_LINE(int value, String description) {

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

