package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum EXP_CAT implements EnumInterface {

    OTH(0, "Other"),
    CHAFF(1, "Chaff"),
    FLARE(2, "Flare"),
    COMB(3, "Combined chaff and flare"),
    AE(4, "Active emitter"),
    PDEC(5, "Passive decoy"),
    WDEC(6, "Winged decoy");

    private final int value;
    private final String description;

    private EXP_CAT(int value, String description) {

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

