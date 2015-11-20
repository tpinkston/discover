package discover.vdis.enums;

import java.util.List;

/**
 * SYNCHRONIZATION_STATE: This class is auto-generated by vdis.EnumGenerator
 */
public final class SYNCHRONIZATION_STATE extends Value {

    public static final SYNCHRONIZATION_STATE
       SYNC_STATE_OTHER = new SYNCHRONIZATION_STATE(0, "SYNC_STATE_OTHER", "Other Synchronization State", true),
       SYNC_STATE_COARSE = new SYNCHRONIZATION_STATE(1, "SYNC_STATE_COARSE", "Coarse Synchronization State", true),
       SYNC_STATE_FINE = new SYNCHRONIZATION_STATE(2, "SYNC_STATE_FINE", "Fine Synchronization State", true);

    private SYNCHRONIZATION_STATE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, SYNCHRONIZATION_STATE.class);
    }

    /** @see Value#values(Class) */
    public static List<SYNCHRONIZATION_STATE> values() {

        return values(SYNCHRONIZATION_STATE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<SYNCHRONIZATION_STATE> values(boolean known) {

        return values(SYNCHRONIZATION_STATE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static SYNCHRONIZATION_STATE get(int value) {

        return get(value, SYNCHRONIZATION_STATE.class);
    }
}

