package discover.vdis.enums;

import java.util.List;

/**
 * APP_CTRL_CONTROL_TYPE: This class is auto-generated by vdis.EnumGenerator
 */
public final class APP_CTRL_CONTROL_TYPE extends Value {

    public static final APP_CTRL_CONTROL_TYPE
       OTHER = new APP_CTRL_CONTROL_TYPE(0, "OTHER", "Other", true),
       SHUTDOWN = new APP_CTRL_CONTROL_TYPE(1, "SHUTDOWN", "Shutdown", true),
       DATA_QUERY = new APP_CTRL_CONTROL_TYPE(2, "DATA_QUERY", "Data Query", true),
       DATA = new APP_CTRL_CONTROL_TYPE(3, "DATA", "Data", true),
       SET_DATA = new APP_CTRL_CONTROL_TYPE(4, "SET_DATA", "Set Data", true),
       ADD_DATA = new APP_CTRL_CONTROL_TYPE(5, "ADD_DATA", "Add Data", true),
       REMOVE_DATA = new APP_CTRL_CONTROL_TYPE(6, "REMOVE_DATA", "Remove Data", true),
       STATUS = new APP_CTRL_CONTROL_TYPE(7, "STATUS", "Status", true);

    private APP_CTRL_CONTROL_TYPE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, APP_CTRL_CONTROL_TYPE.class);
    }

    /** @see Value#values(Class) */
    public static List<APP_CTRL_CONTROL_TYPE> values() {

        return values(APP_CTRL_CONTROL_TYPE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<APP_CTRL_CONTROL_TYPE> values(boolean known) {

        return values(APP_CTRL_CONTROL_TYPE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static APP_CTRL_CONTROL_TYPE get(int value) {

        return get(value, APP_CTRL_CONTROL_TYPE.class);
    }
}

