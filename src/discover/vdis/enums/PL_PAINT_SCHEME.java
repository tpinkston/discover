package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PL_PAINT_SCHEME implements EnumInterface {

    PLAT_PAINT_SCHEME_DEFAULT(0, "Default"),
    PLAT_PAINT_SCHEME_SOLID(1, "Solid Color (primary)"),
    PLAT_PAINT_SCHEME_TWO_TONE_TOP_BOTTOM(2, "Two Tone (Top Primary, Bottom Secondary)"),
    PLAT_PAINT_SCHEME_TWO_TONE_BODY_BED(3, "Two Tone (Body Primary, Bed Secondary)"),
    PLAT_PAINT_SCHEME_TWO_TONE_BODY_TRIM(4, "Two Tone (Body Primary, Trim Secondary)"),
    PLAT_PAINT_SCHEME_CAMO_DESERT(5, "Camouflage, Desert"),
    PLAT_PAINT_SCHEME_CAMO_JUNGLE(6, "Camouflage, Jungle"),
    PLAT_PAINT_SCHEME_CAMO_SNOW(7, "Camouflage, Snow"),
    PLAT_PAINT_SCHEME_CAMO_GRAVEL(8, "Camouflage, Gravel"),
    PLAT_PAINT_SCHEME_CAMO_PAVEMENT(9, "Camouflage, Pavement"),
    PLAT_PAINT_SCHEME_CAMO_SAND(10, "Camouflage, Sand"),
    PLAT_PAINT_SCHEME_CAMO_FOREST(11, "Camouflage, Forest"),
    PLAT_PAINT_SCHEME_NATURAL_WOOD(12, "Natural Wood"),
    PLAT_PAINT_SCHEME_CLEAR(13, "Clear"),
    PLAT_PAINT_SCHEME_UN(14, "United Nations (UN)"),
    PLAT_PAINT_SCHEME_TAXI(15, "Taxi"),
    PLAT_PAINT_SCHEME_NEWS(16, "News"),
    PLAT_PAINT_SCHEME_JINGLE(17, "Jingle");

    private final int value;
    private final String description;

    private PL_PAINT_SCHEME(int value, String description) {

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

        return Enumerations.getUnknownValue(value, PL_PAINT_SCHEME.class);
    }
}

