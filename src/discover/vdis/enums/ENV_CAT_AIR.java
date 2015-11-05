package discover.vdis.enums;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ENV_CAT_AIR implements VdisEnum {

    OTH(0, "Other"),
    SMOKE(1, "Smoke"),
    FOG(2, "Fog"),
    CLD(3, "Cloud"),
    CLDRN(4, "Cloud With Rain Falling"),
    CLDSN(5, "Cloud With Snow Falling"),
    HAZE(6, "Haze"),
    DUST(7, "Dust/Sandstorm"),
    CLDSL(8, "Cloud with sleet falling");

    private final int value;
    private final String description;

    private ENV_CAT_AIR(int value, String description) {

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

