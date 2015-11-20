package discover.vdis.enums;

import java.util.List;

/**
 * LF_CLOTH_SCHEME: This class is auto-generated by vdis.EnumGenerator
 */
public final class LF_CLOTH_SCHEME extends Value {

    public static final LF_CLOTH_SCHEME
       DEFAULT = new LF_CLOTH_SCHEME(0, "DEFAULT", "Default", true),
       SOLID = new LF_CLOTH_SCHEME(1, "SOLID", "Solid Color (Primary)", true),
       TWO_TONE = new LF_CLOTH_SCHEME(2, "TWO_TONE", "Two Tone (top primary, bottom secondary)", true),
       UNI_SOLID_MILITARY = new LF_CLOTH_SCHEME(3, "UNI_SOLID_MILITARY", "Uniformed Solid Color (Military)", true),
       UNI_SOLID_CIVILIAN = new LF_CLOTH_SCHEME(4, "UNI_SOLID_CIVILIAN", "Uniformed Solid Color (Civilian)", true),
       CAMO_DESERT = new LF_CLOTH_SCHEME(5, "CAMO_DESERT", "Camouflage, Desert", true),
       CAMO_JUNGLE = new LF_CLOTH_SCHEME(6, "CAMO_JUNGLE", "Camouflage, Jungle", true),
       CAMO_SNOW = new LF_CLOTH_SCHEME(7, "CAMO_SNOW", "Camouflage, Snow", true),
       CAMO_GRAVEL = new LF_CLOTH_SCHEME(8, "CAMO_GRAVEL", "Camouflage, Gravel", true),
       CAMO_PAVEMENT = new LF_CLOTH_SCHEME(9, "CAMO_PAVEMENT", "Camouflage, Pavement", true),
       CAMO_SAND = new LF_CLOTH_SCHEME(10, "CAMO_SAND", "Camouflage, Sand", true),
       CAMO_FOREST = new LF_CLOTH_SCHEME(11, "CAMO_FOREST", "Camouflage, Forest", true),
       NAKED = new LF_CLOTH_SCHEME(12, "NAKED", "Naked", true),
       TOPLESS = new LF_CLOTH_SCHEME(13, "TOPLESS", "Topless (but not bottomless)", true),
       BOTTOMLESS = new LF_CLOTH_SCHEME(14, "BOTTOMLESS", "Bottomless (but not topless)", true),
       INDIGENOUS = new LF_CLOTH_SCHEME(15, "INDIGENOUS", "Indigenous Clothing", true),
       TRADITIONAL = new LF_CLOTH_SCHEME(16, "TRADITIONAL", "Traditional Clothing", true);

    private LF_CLOTH_SCHEME(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, LF_CLOTH_SCHEME.class);
    }

    /** @see Value#values(Class) */
    public static List<LF_CLOTH_SCHEME> values() {

        return values(LF_CLOTH_SCHEME.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<LF_CLOTH_SCHEME> values(boolean known) {

        return values(LF_CLOTH_SCHEME.class, known);
    }

    /** @see Value#get(int, Class) */
    public static LF_CLOTH_SCHEME get(int value) {

        return get(value, LF_CLOTH_SCHEME.class);
    }
}

