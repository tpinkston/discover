package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ENV_CAT_SUBSURFACE implements EnumInterface {

    OTH(0, "Other"),
    SVP(1, "Sound Velocity Profile"),
    ICEB(2, "Ice Berg"),
    CP(3, "Current Profile"),
    BIO(4, "Biologics"),
    TRANS(5, "Transmissivity profile"),
    ICEM(6, "Ice Merchant"),
    KELP(7, "Kelp Bed"),
    KNUC(8, "Knuckle"),
    DEB(9, "Debris/Clutter");

    private final int value;
    private final String description;

    private ENV_CAT_SUBSURFACE(int value, String description) {

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

