package discover.vdis.enums;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum MUNITION_CAT {

    GUID(1, "Guided"),
    BALL(2, "Ballistic"),
    FIX(3, "Fixed");

    private final int value;
    private final String description;

    private MUNITION_CAT(int value, String description) {

        this.value = value;
        this.description = description;
    }

    public int getValue() {

        return value;
    }

    public String getDescription() {

        return description;
    }
}

