package discover.vdis.enums;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum MUNITION_CAT implements VdisEnum {

    GUID(1, "Guided"),
    BALL(2, "Ballistic"),
    FIX(3, "Fixed");

    private final int value;
    private final String description;

    private MUNITION_CAT(int value, String description) {

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

