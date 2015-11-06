package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PDU_STATUS_RAI implements EnumInterface {

    PDU_STATUS_RAI_NO_STATEMENT(0, "No Statement"),
    PDU_STATUS_RAI_NOT_ATTACHED(1, "Not Attached"),
    PDU_STATUS_RAI_ATTACHED(2, "Attached");

    private final int value;
    private final String description;

    private PDU_STATUS_RAI(int value, String description) {

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

