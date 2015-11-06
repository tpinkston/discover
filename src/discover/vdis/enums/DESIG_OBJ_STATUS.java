package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DESIG_OBJ_STATUS implements EnumInterface {

    DES_OBJ_STATUS_OTHER(0, "No Statement"),
    DES_OBJ_STATUS_INITIAL_REPORT(1, "Initial Report"),
    DES_OBJ_STATUS_CHANGE_REPORT(2, "Change Report"),
    DES_OBJ_STATUS_FINAL_REPORT(3, "Final Report");

    private final int value;
    private final String description;

    private DESIG_OBJ_STATUS(int value, String description) {

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

