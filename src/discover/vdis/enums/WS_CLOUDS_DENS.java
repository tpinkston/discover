package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_CLOUDS_DENS implements EnumInterface {

    WSC_DENSITY_NOT_SPECIFIED(0, "Not Specified"),
    WSC_DENSITY_CLEAR(1, "Clear"),
    WSC_DENSITY_SCATTERED(2, "Scattered"),
    WSC_DENSITY_BROKEN(3, "Broken"),
    WSC_DENSITY_OVERCAST(4, "Overcast");

    private final int value;
    private final String description;

    private WS_CLOUDS_DENS(int value, String description) {

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

