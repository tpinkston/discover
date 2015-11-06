package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum RECEIVER implements EnumInterface {

    RECEIVER_OFF(0, "Off"),
    RECEIVER_ON_BUT_NOT_RECEIVING(1, "On But Not Receiving"),
    RECEIVER_ON_AND_RECEIVING(2, "On And Receiving");

    private final int value;
    private final String description;

    private RECEIVER(int value, String description) {

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

