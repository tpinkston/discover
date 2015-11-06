package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum MUNITION_STATUS implements EnumInterface {

    MUNREC_MUNITION_STATUS_OTHER(0, "Other"),
    MUNREC_MUNITION_STATUS_READY(1, "Ready"),
    MUNREC_MUNITION_STATUS_INVENTORY(2, "Inventory");

    private final int value;
    private final String description;

    private MUNITION_STATUS(int value, String description) {

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

