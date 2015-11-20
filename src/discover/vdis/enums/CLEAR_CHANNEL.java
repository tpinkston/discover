package discover.vdis.enums;

import java.util.List;

/**
 * CLEAR_CHANNEL: This class is auto-generated by vdis.EnumGenerator
 */
public final class CLEAR_CHANNEL extends Value {

    public static final CLEAR_CHANNEL
       CLEAR_NOT_CLEAR_CHANNEL = new CLEAR_CHANNEL(0, "CLEAR_NOT_CLEAR_CHANNEL", "Not Clear Channel", true),
       CLEAR_CLEAR_CHANNEL = new CLEAR_CHANNEL(1, "CLEAR_CLEAR_CHANNEL", "Clear Channel", true);

    private CLEAR_CHANNEL(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, CLEAR_CHANNEL.class);
    }

    /** @see Value#values(Class) */
    public static List<CLEAR_CHANNEL> values() {

        return values(CLEAR_CHANNEL.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<CLEAR_CHANNEL> values(boolean known) {

        return values(CLEAR_CHANNEL.class, known);
    }

    /** @see Value#get(int, Class) */
    public static CLEAR_CHANNEL get(int value) {

        return get(value, CLEAR_CHANNEL.class);
    }
}

