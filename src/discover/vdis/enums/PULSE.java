package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PULSE implements EnumInterface {

    PULSE_OTHER(0, "Other"),
    PULSE_PULSE(1, "Pulse"),
    PULSE_X_BAND_TACAN_PULSE(2, "X Band TACAN Pulse"),
    PULSE_Y_BAND_TACAN_PULSE(3, "Y Band TACAN Pulse");

    private final int value;
    private final String description;

    private PULSE(int value, String description) {

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

