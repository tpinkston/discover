package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum CONTROL_TYPE implements EnumInterface {

    CONTROL_TYPE_RESERVED(0, "Reserved"),
    CONTROL_TYPE_STATUS(1, "Status"),
    CONTROL_TYPE_REQUEST_ACKNOWLEDGE_REQUIRED(2, "Request - Acknowledge Required"),
    CONTROL_TYPE_REQUEST_NO_ACKNOWLEDGE(3, "Request - No Acknowledge"),
    CONTROL_TYPE_ACK_REQUEST_GRANTED(4, "Ack - Request Granted"),
    CONTROL_TYPE_NACK_REQUEST_DENIED(5, "Nack - Request Denied");

    private final int value;
    private final String description;

    private CONTROL_TYPE(int value, String description) {

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

