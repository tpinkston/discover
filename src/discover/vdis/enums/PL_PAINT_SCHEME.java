package discover.vdis.enums;

import java.util.List;

/**
 * PL_PAINT_SCHEME: This class is auto-generated by vdis.EnumGenerator
 */
public final class PL_PAINT_SCHEME extends Value {

    public static final PL_PAINT_SCHEME
       PLAT_PAINT_SCHEME_DEFAULT = new PL_PAINT_SCHEME(0, "PLAT_PAINT_SCHEME_DEFAULT", "Default", true),
       PLAT_PAINT_SCHEME_SOLID = new PL_PAINT_SCHEME(1, "PLAT_PAINT_SCHEME_SOLID", "Solid Color (primary)", true),
       PLAT_PAINT_SCHEME_TWO_TONE_TOP_BOTTOM = new PL_PAINT_SCHEME(2, "PLAT_PAINT_SCHEME_TWO_TONE_TOP_BOTTOM", "Two Tone (Top Primary, Bottom Secondary)", true),
       PLAT_PAINT_SCHEME_TWO_TONE_BODY_BED = new PL_PAINT_SCHEME(3, "PLAT_PAINT_SCHEME_TWO_TONE_BODY_BED", "Two Tone (Body Primary, Bed Secondary)", true),
       PLAT_PAINT_SCHEME_TWO_TONE_BODY_TRIM = new PL_PAINT_SCHEME(4, "PLAT_PAINT_SCHEME_TWO_TONE_BODY_TRIM", "Two Tone (Body Primary, Trim Secondary)", true),
       PLAT_PAINT_SCHEME_CAMO_DESERT = new PL_PAINT_SCHEME(5, "PLAT_PAINT_SCHEME_CAMO_DESERT", "Camouflage, Desert", true),
       PLAT_PAINT_SCHEME_CAMO_JUNGLE = new PL_PAINT_SCHEME(6, "PLAT_PAINT_SCHEME_CAMO_JUNGLE", "Camouflage, Jungle", true),
       PLAT_PAINT_SCHEME_CAMO_SNOW = new PL_PAINT_SCHEME(7, "PLAT_PAINT_SCHEME_CAMO_SNOW", "Camouflage, Snow", true),
       PLAT_PAINT_SCHEME_CAMO_GRAVEL = new PL_PAINT_SCHEME(8, "PLAT_PAINT_SCHEME_CAMO_GRAVEL", "Camouflage, Gravel", true),
       PLAT_PAINT_SCHEME_CAMO_PAVEMENT = new PL_PAINT_SCHEME(9, "PLAT_PAINT_SCHEME_CAMO_PAVEMENT", "Camouflage, Pavement", true),
       PLAT_PAINT_SCHEME_CAMO_SAND = new PL_PAINT_SCHEME(10, "PLAT_PAINT_SCHEME_CAMO_SAND", "Camouflage, Sand", true),
       PLAT_PAINT_SCHEME_CAMO_FOREST = new PL_PAINT_SCHEME(11, "PLAT_PAINT_SCHEME_CAMO_FOREST", "Camouflage, Forest", true),
       PLAT_PAINT_SCHEME_NATURAL_WOOD = new PL_PAINT_SCHEME(12, "PLAT_PAINT_SCHEME_NATURAL_WOOD", "Natural Wood", true),
       PLAT_PAINT_SCHEME_CLEAR = new PL_PAINT_SCHEME(13, "PLAT_PAINT_SCHEME_CLEAR", "Clear", true),
       PLAT_PAINT_SCHEME_UN = new PL_PAINT_SCHEME(14, "PLAT_PAINT_SCHEME_UN", "United Nations (UN)", true),
       PLAT_PAINT_SCHEME_TAXI = new PL_PAINT_SCHEME(15, "PLAT_PAINT_SCHEME_TAXI", "Taxi", true),
       PLAT_PAINT_SCHEME_NEWS = new PL_PAINT_SCHEME(16, "PLAT_PAINT_SCHEME_NEWS", "News", true),
       PLAT_PAINT_SCHEME_JINGLE = new PL_PAINT_SCHEME(17, "PLAT_PAINT_SCHEME_JINGLE", "Jingle", true);

    private PL_PAINT_SCHEME(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, PL_PAINT_SCHEME.class);
    }

    /** @see Value#values(Class) */
    public static List<PL_PAINT_SCHEME> values() {

        return values(PL_PAINT_SCHEME.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<PL_PAINT_SCHEME> values(boolean known) {

        return values(PL_PAINT_SCHEME.class, known);
    }

    /** @see Value#get(int, Class) */
    public static PL_PAINT_SCHEME get(int value) {

        return get(value, PL_PAINT_SCHEME.class);
    }
}

