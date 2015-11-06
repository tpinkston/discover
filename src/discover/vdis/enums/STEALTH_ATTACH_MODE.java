package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum STEALTH_ATTACH_MODE implements EnumInterface {

    STEALTH_ATTATCH_MODE_FREE(0, "Free"),
    STEALTH_ATTATCH_MODE_HUG(1, "Hug"),
    STEALTH_ATTATCH_MODE_TETHER(2, "Tether"),
    STEALTH_ATTATCH_MODE_COMPASS(3, "Compass"),
    STEALTH_ATTATCH_MODE_ORBIT(4, "Orbit"),
    STEALTH_ATTATCH_MODE_MIMIC(5, "Mimic"),
    STEALTH_ATTATCH_MODE_NO(6, "No"),
    STEALTH_ATTATCH_MODE_GUNSIGHT(7, "Gunsight");

    private final int value;
    private final String description;

    private STEALTH_ATTACH_MODE(int value, String description) {

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

