package discover.vdis.enums;

import java.util.List;

/**
 * DE_BEAM_SHAPE: This class is auto-generated by vdis.EnumGenerator
 */
public final class DE_BEAM_SHAPE extends Value {

    public static final DE_BEAM_SHAPE
       DE_FIRE_BEAM_SHAPE_OTHER = new DE_BEAM_SHAPE(0, "DE_FIRE_BEAM_SHAPE_OTHER", "Other", true),
       DE_FIRE_BEAM_SHAPE_GAUSSIAN = new DE_BEAM_SHAPE(1, "DE_FIRE_BEAM_SHAPE_GAUSSIAN", "Gaussian", true),
       DE_FIRE_BEAM_SHAPE_TOP_HAT = new DE_BEAM_SHAPE(2, "DE_FIRE_BEAM_SHAPE_TOP_HAT", "Top Hat", true);

    private DE_BEAM_SHAPE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, DE_BEAM_SHAPE.class);
    }

    /** @see Value#values(Class) */
    public static List<DE_BEAM_SHAPE> values() {

        return values(DE_BEAM_SHAPE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<DE_BEAM_SHAPE> values(boolean known) {

        return values(DE_BEAM_SHAPE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static DE_BEAM_SHAPE get(int value) {

        return get(value, DE_BEAM_SHAPE.class);
    }
}

