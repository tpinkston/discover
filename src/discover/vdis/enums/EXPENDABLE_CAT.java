package discover.vdis.enums;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum EXPENDABLE_CAT implements VdisEnum {

    CHAFF(1, "Chaff"),
    FLARE(2, "Flare"),
    COMB(3, "Combined chaff and flare"),
    AE(4, "Active emitter"),
    PDEC(5, "Passive decoy"),
    WDEC(6, "Winged decoy");

    private final int value;
    private final String description;

    private EXPENDABLE_CAT(int value, String description) {

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

