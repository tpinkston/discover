package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PDU_STATUS_CEI implements EnumInterface {

    PDU_STATUS_CEI_NOT_COUPLED(0, "Not Coupled"),
    PDU_STATUS_CEI_COUPLED(1, "Coupled");

    private final int value;
    private final String description;

    private PDU_STATUS_CEI(int value, String description) {

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

