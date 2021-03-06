package discover.vdis.enums;

import java.util.List;

/**
 * ACK_RESPONSE_FLAG: This class is auto-generated by vdis.EnumGenerator
 */
public final class ACK_RESPONSE_FLAG extends Value {

    public static final ACK_RESPONSE_FLAG
       ACK_RESP_OTHER = new ACK_RESPONSE_FLAG(0, "ACK_RESP_OTHER", "Other", true),
       ACK_RESP_ABLE_TO_COMPLY = new ACK_RESPONSE_FLAG(1, "ACK_RESP_ABLE_TO_COMPLY", "Able to Comply", true),
       ACK_RESP_UNABLE_TO_COMPLY = new ACK_RESPONSE_FLAG(2, "ACK_RESP_UNABLE_TO_COMPLY", "Unable to Comply", true),
       ACK_RESP_PENDING_OPER_ACT = new ACK_RESPONSE_FLAG(3, "ACK_RESP_PENDING_OPER_ACT", "Pending Operator Action", true);

    private ACK_RESPONSE_FLAG(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, ACK_RESPONSE_FLAG.class);
    }

    /** @see Value#values(Class) */
    public static List<ACK_RESPONSE_FLAG> values() {

        return values(ACK_RESPONSE_FLAG.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<ACK_RESPONSE_FLAG> values(boolean known) {

        return values(ACK_RESPONSE_FLAG.class, known);
    }

    /** @see Value#get(int, Class) */
    public static ACK_RESPONSE_FLAG get(int value) {

        return get(value, ACK_RESPONSE_FLAG.class);
    }
}

