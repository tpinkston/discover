package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum EDS_SURF_DMG implements EnumInterface {

    EDS_SURF_DMG_NORMAL_APP(0, "Normal Appearance"),
    EDS_SURF_DMG_LIGHT_CHAR(1, "Light Charring"),
    EDS_SURF_DMG_HEAVY_CHAR(2, "Heavy Charring"),
    EDS_SURF_DMG_HOLES(3, "One or More Holes");

    private final int value;
    private final String description;

    private EDS_SURF_DMG(int value, String description) {

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

