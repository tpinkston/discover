package discover.vdis.enums;

import java.util.List;

/**
 * TERMINAL_SECONDARY: This class is auto-generated by vdis.EnumGenerator
 */
public final class TERMINAL_SECONDARY extends Value {

    public static final TERMINAL_SECONDARY
       NONE = new TERMINAL_SECONDARY(0, "NONE", "None ", true),
       NET_POSITION_REFERENCE = new TERMINAL_SECONDARY(1, "NET_POSITION_REFERENCE", "Net Position Reference ", true),
       PRIMARY_NAVIGATION_CONTROLLER = new TERMINAL_SECONDARY(2, "PRIMARY_NAVIGATION_CONTROLLER", "Primary Navigation Controller ", true),
       SECONDARY_NAVIGATION_CONTROLLER = new TERMINAL_SECONDARY(3, "SECONDARY_NAVIGATION_CONTROLLER", "Secondary Navigation Controller", true);

    private TERMINAL_SECONDARY(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, TERMINAL_SECONDARY.class);
    }

    /** @see Value#values(Class) */
    public static List<TERMINAL_SECONDARY> values() {

        return values(TERMINAL_SECONDARY.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<TERMINAL_SECONDARY> values(boolean known) {

        return values(TERMINAL_SECONDARY.class, known);
    }

    /** @see Value#get(int, Class) */
    public static TERMINAL_SECONDARY get(int value) {

        return get(value, TERMINAL_SECONDARY.class);
    }
}

