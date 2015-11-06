package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PL_DECAL_SCHEME implements EnumInterface {

    PLAT_DECAL_SCHEME_NONE(0, "None"),
    PLAT_DECAL_SCHEME_UN(1, "United Nations (UN)"),
    PLAT_DECAL_SCHEME_FED_EX(2, "Fed Ex"),
    PLAT_DECAL_SCHEME_COCA_COLA(3, "Coca-Cola"),
    PLAT_DECAL_SCHEME_PEPSI_COLA(4, "Pepsi-Cola"),
    PLAT_DECAL_SCHEME_YELLOW_CAB(5, "Yellow Cab"),
    PLAT_DECAL_SCHEME_CHANNEL_5_NEWS(6, "Channel 5 News");

    private final int value;
    private final String description;

    private PL_DECAL_SCHEME(int value, String description) {

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

