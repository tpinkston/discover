package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum CF_CAT_LAND implements EnumInterface {

    OTH(0, "Other"),
    BRD(1, "Bridge"),
    BLDG(2, "Building"),
    TRAK(3, "Tracks"),
    CRAT(4, "Crater"),
    OBS(5, "Obstacle"),
    FLAG(6, "Flags/Markers"),
    PREP(7, "Prepared Positions"),
    UGRD(8, "Underground Facility"),
    SSM(9, "SSM Site"),
    RNWY(10, "Runway"),
    SAM(11, "SAM Site"),
    NUKE(12, "Nuclear Power Plant"),
    RAIL(13, "Railyard"),
    CTF(14, "Camp Lejeune MOUT Collective Training Facility (CTF)"),
    PST(15, "POL Storage Tank"),
    CARGO(16, "Cargo Container"),
    TOWER(17, "Tower"),
    AAA(18, "AAA Site"),
    BRDSP(19, "Bridge Span"),
    ROAD(20, "Road Element");

    private final int value;
    private final String description;

    private CF_CAT_LAND(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getName() {

        return name();
    }

    @Override
    public String getDescription() {

        return description;
    }

    public static EnumInterface getValue(int value) {

        for(EnumInterface element : values()) {

            if (element.getValue() == value) {

                return element;
            }
        }

        return Enumerations.getUnknownValue(value, CF_CAT_LAND.class);
    }
}

