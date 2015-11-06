package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum LF_CLOTH_SCHEME implements EnumInterface {

    LF_CLOTH_SCHEME_DEFAULT(0, "Default"),
    LF_CLOTH_SCHEME_SOLID(1, "Solid Color (Primary)"),
    LF_CLOTH_SCHEME_TWO_TONE(2, "Two Tone (top primary, bottom secondary)"),
    LF_CLOTH_SCHEME_UNI_SOLID_MILITARY(3, "Uniformed Solid Color (Military)"),
    LF_CLOTH_SCHEME_UNI_SOLID_CIVILIAN(4, "Uniformed Solid Color (Civilian)"),
    LF_CLOTH_SCHEME_CAMO_DESERT(5, "Camouflage, Desert"),
    LF_CLOTH_SCHEME_CAMO_JUNGLE(6, "Camouflage, Jungle"),
    LF_CLOTH_SCHEME_CAMO_SNOW(7, "Camouflage, Snow"),
    LF_CLOTH_SCHEME_CAMO_GRAVEL(8, "Camouflage, Gravel"),
    LF_CLOTH_SCHEME_CAMO_PAVEMENT(9, "Camouflage, Pavement"),
    LF_CLOTH_SCHEME_CAMO_SAND(10, "Camouflage, Sand"),
    LF_CLOTH_SCHEME_CAMO_FOREST(11, "Camouflage, Forest"),
    LF_CLOTH_SCHEME_NAKED(12, "Naked"),
    LF_CLOTH_SCHEME_TOPLESS(13, "Topless (but not bottomless)"),
    LF_CLOTH_SCHEME_BOTTOMLESS(14, "Bottomless (but not topless)"),
    LF_CLOTH_SCHEME_INDIGENOUS(15, "Indigenous Clothing"),
    LF_CLOTH_SCHEME_TRADITIONAL(16, "Traditional Clothing");

    private final int value;
    private final String description;

    private LF_CLOTH_SCHEME(int value, String description) {

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

