package discover.vdis.enums;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ENV_CAT_LAND implements VdisEnum {

    OTH(0, "Other"),
    RADAR(1, "Radar collected noise");

    private final int value;
    private final String description;

    private ENV_CAT_LAND(int value, String description) {

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

