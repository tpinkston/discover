package discover.vdis.enums;

import java.util.List;

/**
 * CLASSIFICATION_ENUM: This class is auto-generated by vdis.EnumGenerator
 */
public final class CLASSIFICATION_ENUM extends Value {

    public static final CLASSIFICATION_ENUM
       UNCLASSIFIED = new CLASSIFICATION_ENUM(0, "UNCLASSIFIED", "Unclassified", true),
       CONFIDENTIAL = new CLASSIFICATION_ENUM(1, "CONFIDENTIAL", "Confidential", true),
       SECRET = new CLASSIFICATION_ENUM(2, "SECRET", "Secret", true),
       TOP_SECRET = new CLASSIFICATION_ENUM(3, "TOP_SECRET", "Top Secret", true);

    private CLASSIFICATION_ENUM(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, CLASSIFICATION_ENUM.class);
    }

    /** @see Value#values(Class) */
    public static List<CLASSIFICATION_ENUM> values() {

        return values(CLASSIFICATION_ENUM.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<CLASSIFICATION_ENUM> values(boolean known) {

        return values(CLASSIFICATION_ENUM.class, known);
    }

    /** @see Value#get(int, Class) */
    public static CLASSIFICATION_ENUM get(int value) {

        return get(value, CLASSIFICATION_ENUM.class);
    }
}

