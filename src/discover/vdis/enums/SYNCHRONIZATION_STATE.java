package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum SYNCHRONIZATION_STATE implements EnumInterface {

    SYNC_STATE_OTHER(0, "Other Synchronization State"),
    SYNC_STATE_COARSE(1, "Coarse Synchronization State"),
    SYNC_STATE_FINE(2, "Fine Synchronization State");

    private final int value;
    private final String description;

    private SYNCHRONIZATION_STATE(int value, String description) {

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

