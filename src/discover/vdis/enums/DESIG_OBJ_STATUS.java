package discover.vdis.enums;

import java.util.List;

/**
 * DESIG_OBJ_STATUS: This class is auto-generated by vdis.EnumGenerator
 */
public final class DESIG_OBJ_STATUS extends Value {

    public static final DESIG_OBJ_STATUS
       DES_OBJ_STATUS_OTHER = new DESIG_OBJ_STATUS(0, "DES_OBJ_STATUS_OTHER", "No Statement", true),
       DES_OBJ_STATUS_INITIAL_REPORT = new DESIG_OBJ_STATUS(1, "DES_OBJ_STATUS_INITIAL_REPORT", "Initial Report", true),
       DES_OBJ_STATUS_CHANGE_REPORT = new DESIG_OBJ_STATUS(2, "DES_OBJ_STATUS_CHANGE_REPORT", "Change Report", true),
       DES_OBJ_STATUS_FINAL_REPORT = new DESIG_OBJ_STATUS(3, "DES_OBJ_STATUS_FINAL_REPORT", "Final Report", true);

    private DESIG_OBJ_STATUS(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, DESIG_OBJ_STATUS.class);
    }

    /** @see Value#values(Class) */
    public static List<DESIG_OBJ_STATUS> values() {

        return values(DESIG_OBJ_STATUS.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<DESIG_OBJ_STATUS> values(boolean known) {

        return values(DESIG_OBJ_STATUS.class, known);
    }

    /** @see Value#get(int, Class) */
    public static DESIG_OBJ_STATUS get(int value) {

        return get(value, DESIG_OBJ_STATUS.class);
    }
}

