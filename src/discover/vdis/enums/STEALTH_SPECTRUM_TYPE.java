package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum STEALTH_SPECTRUM_TYPE implements EnumInterface {

    STEALTH_SPECTRUM_TYPE_OTW(0, "Out the Window (OTW)"),
    STEALTH_SPECTRUM_TYPE_DAY_TV(1, "Day TV"),
    STEALTH_SPECTRUM_TYPE_THERMAL_MANUAL(2, "Thermal Manual"),
    STEALTH_SPECTRUM_TYPE_THERMAL_AUTO(3, "Thermal Automatic"),
    STEALTH_SPECTRUM_TYPE_THERMAL_QUARTER(4, "Thermal Quarter Resolution"),
    STEALTH_SPECTRUM_TYPE_THERMAL_HALF(5, "Thermal Half Resolution");

    private final int value;
    private final String description;

    private STEALTH_SPECTRUM_TYPE(int value, String description) {

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

