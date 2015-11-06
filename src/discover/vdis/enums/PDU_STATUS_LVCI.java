package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PDU_STATUS_LVCI implements EnumInterface {

    PDU_STATUS_LVCI_NO_STATEMENT(0, "No Statement"),
    PDU_STATUS_LVCI_LIVE(1, "Live"),
    PDU_STATUS_LVCI_VIRTUAL(2, "Virtual"),
    PDU_STATUS_LVCI_CONSTRUCTIVE(3, "Constructive");

    private final int value;
    private final String description;

    private PDU_STATUS_LVCI(int value, String description) {

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

