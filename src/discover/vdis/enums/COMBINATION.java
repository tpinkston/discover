package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum COMBINATION implements EnumInterface {

    COMBO_OTHER(0, "Other"),
    COMBO_AMPLITUDE_ANGLE_PULSE(1, "Amplitude-Angle-Pulse");

    private final int value;
    private final String description;

    private COMBINATION(int value, String description) {

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

