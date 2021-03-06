package discover.vdis.enums;

import java.util.List;

/**
 * IED_PRESENCE: This class is auto-generated by vdis.EnumGenerator
 */
public final class IED_PRESENCE extends Value {

    public static final IED_PRESENCE
       NONE = new IED_PRESENCE(0, "NONE", "None", true),
       VISIBLE = new IED_PRESENCE(1, "VISIBLE", "Visible", true),
       PARTIALLY_HIDDEN = new IED_PRESENCE(2, "PARTIALLY_HIDDEN", "Partially Hidden", true),
       COMPLETELY_HIDDEN = new IED_PRESENCE(3, "COMPLETELY_HIDDEN", "Completely Hidden", true);

    private IED_PRESENCE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, IED_PRESENCE.class);
    }

    /** @see Value#values(Class) */
    public static List<IED_PRESENCE> values() {

        return values(IED_PRESENCE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<IED_PRESENCE> values(boolean known) {

        return values(IED_PRESENCE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static IED_PRESENCE get(int value) {

        return get(value, IED_PRESENCE.class);
    }
}

