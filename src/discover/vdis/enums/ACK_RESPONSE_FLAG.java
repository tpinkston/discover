package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ACK_RESPONSE_FLAG implements EnumInterface {

    ACK_RESP_OTHER(0, "Other"),
    ACK_RESP_ABLE_TO_COMPLY(1, "Able to Comply"),
    ACK_RESP_UNABLE_TO_COMPLY(2, "Unable to Comply"),
    ACK_RESP_PENDING_OPER_ACT(3, "Pending Operator Action");

    private final int value;
    private final String description;

    private ACK_RESPONSE_FLAG(int value, String description) {

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

