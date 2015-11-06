package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DISGUISE_STATUS implements EnumInterface {

    DISGUISE_STATUS_NOT_SPECIFIED(0, "Not Specified"),
    DISGUISE_STATUS_NO_DISGUISE(1, "No Disguise"),
    DISGUISE_STATUS_DISGUISED(2, "Disguised"),
    DISGUISE_STATUS_INDISTINGUISHABLE(3, "Indistinguishable");

    private final int value;
    private final String description;

    private DISGUISE_STATUS(int value, String description) {

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

