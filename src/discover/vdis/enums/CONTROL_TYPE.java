package discover.vdis.enums;

import java.util.List;

/**
 * CONTROL_TYPE: This class is auto-generated by vdis.EnumGenerator
 */
public final class CONTROL_TYPE extends Value {

    public static final CONTROL_TYPE
       RESERVED = new CONTROL_TYPE(0, "RESERVED", "Reserved", true),
       STATUS = new CONTROL_TYPE(1, "STATUS", "Status", true),
       REQUEST_ACKNOWLEDGE_REQUIRED = new CONTROL_TYPE(2, "REQUEST_ACKNOWLEDGE_REQUIRED", "Request - Acknowledge Required", true),
       REQUEST_NO_ACKNOWLEDGE = new CONTROL_TYPE(3, "REQUEST_NO_ACKNOWLEDGE", "Request - No Acknowledge", true),
       ACK_REQUEST_GRANTED = new CONTROL_TYPE(4, "ACK_REQUEST_GRANTED", "Ack - Request Granted", true),
       NACK_REQUEST_DENIED = new CONTROL_TYPE(5, "NACK_REQUEST_DENIED", "Nack - Request Denied", true);

    private CONTROL_TYPE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, CONTROL_TYPE.class);
    }

    /** @see Value#values(Class) */
    public static List<CONTROL_TYPE> values() {

        return values(CONTROL_TYPE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<CONTROL_TYPE> values(boolean known) {

        return values(CONTROL_TYPE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static CONTROL_TYPE get(int value) {

        return get(value, CONTROL_TYPE.class);
    }
}

