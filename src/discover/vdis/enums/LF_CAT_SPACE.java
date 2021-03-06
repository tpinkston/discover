package discover.vdis.enums;

import java.util.List;

/**
 * LF_CAT_SPACE: This class is auto-generated by vdis.EnumGenerator
 */
public final class LF_CAT_SPACE extends Value {

    public static final LF_CAT_SPACE
       OTH = new LF_CAT_SPACE(0, "OTH", "Other", true),
       ASTRO = new LF_CAT_SPACE(1, "ASTRO", "Astronaut (with)", true);

    private LF_CAT_SPACE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, LF_CAT_SPACE.class);
    }

    /** @see Value#values(Class) */
    public static List<LF_CAT_SPACE> values() {

        return values(LF_CAT_SPACE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<LF_CAT_SPACE> values(boolean known) {

        return values(LF_CAT_SPACE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static LF_CAT_SPACE get(int value) {

        return get(value, LF_CAT_SPACE.class);
    }
}

