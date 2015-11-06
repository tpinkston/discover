package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ACK_ACKNOWLEDGE_FLAG implements EnumInterface {

    ACK_FLAG_CREATE_ENTITY(1, "Create Entity"),
    ACK_FLAG_REMOVE_ENTITY(2, "Remove Entity"),
    ACK_FLAG_START_RESUME(3, "Start Resume"),
    ACK_FLAG_STOP_FREEZE(4, "Stop Freeze"),
    ACK_FLAG_TRANSFER_CONTROL_REQUEST(5, "Transfer Control Request");

    private final int value;
    private final String description;

    private ACK_ACKNOWLEDGE_FLAG(int value, String description) {

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

