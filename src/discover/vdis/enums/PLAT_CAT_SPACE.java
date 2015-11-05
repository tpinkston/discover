package discover.vdis.enums;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PLAT_CAT_SPACE implements VdisEnum {

    MAN(1, "Manned"),
    UNMAN(2, "Unmanned"),
    BOOS(3, "Booster");

    private final int value;
    private final String description;

    private PLAT_CAT_SPACE(int value, String description) {

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

