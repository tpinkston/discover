package discover.vdis.enums;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum CF_CAT_SURFACE implements VdisEnum {

    OTH(0, "Other"),
    OIL(1, "Oil Derrick"),
    BUOY(2, "Buoy"),
    PIER(3, "Pier"),
    MARK(4, "Marker"),
    DAT(5, "Datum"),
    SPNT(6, "Special Point");

    private final int value;
    private final String description;

    private CF_CAT_SURFACE(int value, String description) {

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
