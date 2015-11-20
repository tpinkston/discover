package discover.vdis.enums;

import java.util.List;

/**
 * EXP_CAT: This class is auto-generated by vdis.EnumGenerator
 */
public final class EXP_CAT extends Value {

    public static final EXP_CAT
       OTH = new EXP_CAT(0, "OTH", "Other", true),
       CHAFF = new EXP_CAT(1, "CHAFF", "Chaff", true),
       FLARE = new EXP_CAT(2, "FLARE", "Flare", true),
       COMB = new EXP_CAT(3, "COMB", "Combined chaff and flare", true),
       AE = new EXP_CAT(4, "AE", "Active emitter", true),
       PDEC = new EXP_CAT(5, "PDEC", "Passive decoy", true),
       WDEC = new EXP_CAT(6, "WDEC", "Winged decoy", true);

    private EXP_CAT(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, EXP_CAT.class);
    }

    /** @see Value#values(Class) */
    public static List<EXP_CAT> values() {

        return values(EXP_CAT.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<EXP_CAT> values(boolean known) {

        return values(EXP_CAT.class, known);
    }

    /** @see Value#get(int, Class) */
    public static EXP_CAT get(int value) {

        return get(value, EXP_CAT.class);
    }
}

