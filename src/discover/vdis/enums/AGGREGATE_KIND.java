package discover.vdis.enums;

import java.util.List;

/**
 * AGGREGATE_KIND: This class is auto-generated by vdis.EnumGenerator
 */
public final class AGGREGATE_KIND extends Value {

    public static final AGGREGATE_KIND
       OTHER = new AGGREGATE_KIND(0, "OTHER", "Other", true),
       MILITARY_HIERARCHY = new AGGREGATE_KIND(1, "MILITARY_HIERARCHY", "Military Hierarchy", true),
       COMMON_TYPE = new AGGREGATE_KIND(2, "COMMON_TYPE", "Common Type", true),
       COMMON_MISSION = new AGGREGATE_KIND(3, "COMMON_MISSION", "Common Mission", true),
       SIMILAR_CAPABILITIES = new AGGREGATE_KIND(4, "SIMILAR_CAPABILITIES", "Similar Capabilities", true),
       COMMON_LOCATION = new AGGREGATE_KIND(5, "COMMON_LOCATION", "Common Location", true);

    private AGGREGATE_KIND(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, AGGREGATE_KIND.class);
    }

    /** @see Value#values(Class) */
    public static List<AGGREGATE_KIND> values() {

        return values(AGGREGATE_KIND.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<AGGREGATE_KIND> values(boolean known) {

        return values(AGGREGATE_KIND.class, known);
    }

    /** @see Value#get(int, Class) */
    public static AGGREGATE_KIND get(int value) {

        return get(value, AGGREGATE_KIND.class);
    }
}

