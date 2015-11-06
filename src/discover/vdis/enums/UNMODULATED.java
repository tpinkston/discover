package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum UNMODULATED implements EnumInterface {

    UNMOD_OTHER(0, "Other"),
    UNMOD_CONTINUOUS_WAVE_EMISSION(1, "Continuous Wave Emission of an Unmodulated Carrier");

    private final int value;
    private final String description;

    private UNMODULATED(int value, String description) {

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

