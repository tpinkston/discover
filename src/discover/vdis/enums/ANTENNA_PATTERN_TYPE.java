package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ANTENNA_PATTERN_TYPE implements EnumInterface {

    ANTENNA_PATTERN_OMNI_DIRECTIONAL(0, "Omni-Directional"),
    ANTENNA_PATTERN_BEAM(1, "Beam"),
    ANTENNA_PATTERN_SPHERICAL_HARMONIC(2, "Spherical Harmonic");

    private final int value;
    private final String description;

    private ANTENNA_PATTERN_TYPE(int value, String description) {

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

