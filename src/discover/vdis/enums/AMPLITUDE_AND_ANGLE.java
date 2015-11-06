package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum AMPLITUDE_AND_ANGLE implements EnumInterface {

    AMP_ANG_OTHER(0, "Other"),
    AMP_ANG_AMPLITUDE_AND_ANGLE(1, "Amplitude and Angle");

    private final int value;
    private final String description;

    private AMPLITUDE_AND_ANGLE(int value, String description) {

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

