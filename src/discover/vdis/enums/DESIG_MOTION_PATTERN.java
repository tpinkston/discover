package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DESIG_MOTION_PATTERN implements EnumInterface {

    DES_MOT_PATT_NO_STATEMENT(0, "No Statement"),
    DES_MOT_PATT_ROPING(1, "Roping"),
    DES_MOT_PATT_SNAKING(2, "Snaking");

    private final int value;
    private final String description;

    private DESIG_MOTION_PATTERN(int value, String description) {

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
