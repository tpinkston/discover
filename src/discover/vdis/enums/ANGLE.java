package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ANGLE implements EnumInterface {

    ANGLE_OTHER(0, "Other"),
    ANGLE_FM(1, "FM (Frequency Modulation)"),
    ANGLE_FSK(2, "FSK (Frequency Shift Keying)"),
    ANGLE_PM(3, "PM (Phase Modulation)");

    private final int value;
    private final String description;

    private ANGLE(int value, String description) {

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

