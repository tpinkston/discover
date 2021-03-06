package discover.vdis.enums;

import java.util.List;

/**
 * SERVICE_TYPE: This class is auto-generated by vdis.EnumGenerator
 */
public final class SERVICE_TYPE extends Value {

    public static final SERVICE_TYPE
       SERV_TYPE_OTHER = new SERVICE_TYPE(0, "SERV_TYPE_OTHER", "Other", true),
       SERV_TYPE_RESUPPLY = new SERVICE_TYPE(1, "SERV_TYPE_RESUPPLY", "Resupply", true),
       SERV_TYPE_REPAIR = new SERVICE_TYPE(2, "SERV_TYPE_REPAIR", "Repair", true);

    private SERVICE_TYPE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, SERVICE_TYPE.class);
    }

    /** @see Value#values(Class) */
    public static List<SERVICE_TYPE> values() {

        return values(SERVICE_TYPE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<SERVICE_TYPE> values(boolean known) {

        return values(SERVICE_TYPE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static SERVICE_TYPE get(int value) {

        return get(value, SERVICE_TYPE.class);
    }
}

