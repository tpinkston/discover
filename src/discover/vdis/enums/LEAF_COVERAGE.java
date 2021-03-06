package discover.vdis.enums;

import java.util.List;

/**
 * LEAF_COVERAGE: This class is auto-generated by vdis.EnumGenerator
 */
public final class LEAF_COVERAGE extends Value {

    public static final LEAF_COVERAGE
       NORMAL = new LEAF_COVERAGE(0, "NORMAL", "Normal", true),
       BARE = new LEAF_COVERAGE(1, "BARE", "Bare", true);

    private LEAF_COVERAGE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, LEAF_COVERAGE.class);
    }

    /** @see Value#values(Class) */
    public static List<LEAF_COVERAGE> values() {

        return values(LEAF_COVERAGE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<LEAF_COVERAGE> values(boolean known) {

        return values(LEAF_COVERAGE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static LEAF_COVERAGE get(int value) {

        return get(value, LEAF_COVERAGE.class);
    }
}

