package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum COMMS_TYPE implements EnumInterface {

    COMMS_RESERVED(0, "Reserved"),
    COMMS_CONNECTION_FDX(1, "Connection FDX"),
    COMMS_CONNECTION_HDX_DESTINATION_IS_RECEIVE_ONLY(2, "Connection HDX - Destination Is Receive Only"),
    COMMS_CONNECTION_HDX_DESTINATION_IS_TRANSMIT_ONLY(3, "Connection HDX - Destination Is Transmit Only"),
    COMMS_CONNECTION_HDX(4, "Connection HDX");

    private final int value;
    private final String description;

    private COMMS_TYPE(int value, String description) {

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

