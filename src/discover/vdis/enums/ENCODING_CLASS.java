package discover.vdis.enums;

import java.util.List;

/**
 * ENCODING_CLASS: This class is auto-generated by vdis.EnumGenerator
 */
public final class ENCODING_CLASS extends Value {

    public static final ENCODING_CLASS
       ENCODED_AUDIO = new ENCODING_CLASS(0, "ENCODED_AUDIO", "Encoded Audio", true),
       RAW_BINARY_DATA = new ENCODING_CLASS(1, "RAW_BINARY_DATA", "Raw Binary Data", true),
       APPLICATION_SPECIFIC_DATA = new ENCODING_CLASS(2, "APPLICATION_SPECIFIC_DATA", "Application-Specific Data", true),
       DATABASE_INDEX = new ENCODING_CLASS(3, "DATABASE_INDEX", "Database Index", true);

    private ENCODING_CLASS(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, ENCODING_CLASS.class);
    }

    /** @see Value#values(Class) */
    public static List<ENCODING_CLASS> values() {

        return values(ENCODING_CLASS.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<ENCODING_CLASS> values(boolean known) {

        return values(ENCODING_CLASS.class, known);
    }

    /** @see Value#get(int, Class) */
    public static ENCODING_CLASS get(int value) {

        return get(value, ENCODING_CLASS.class);
    }
}

