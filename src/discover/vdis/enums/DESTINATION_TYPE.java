package discover.vdis.enums;

import java.util.List;

/**
 * DESTINATION_TYPE: This class is auto-generated by vdis.EnumGenerator
 */
public final class DESTINATION_TYPE extends Value {

    public static final DESTINATION_TYPE
       DEST_TYPE_URN = new DESTINATION_TYPE(0, "DEST_TYPE_URN", "URN", true),
       DEST_TYPE_IPV4 = new DESTINATION_TYPE(1, "DEST_TYPE_IPV4", "IPV4", true),
       DEST_TYPE_SUBSCRIBER_ID = new DESTINATION_TYPE(2, "DEST_TYPE_SUBSCRIBER_ID", "Subscriber ID", true);

    private DESTINATION_TYPE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, DESTINATION_TYPE.class);
    }

    /** @see Value#values(Class) */
    public static List<DESTINATION_TYPE> values() {

        return values(DESTINATION_TYPE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<DESTINATION_TYPE> values(boolean known) {

        return values(DESTINATION_TYPE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static DESTINATION_TYPE get(int value) {

        return get(value, DESTINATION_TYPE.class);
    }
}

