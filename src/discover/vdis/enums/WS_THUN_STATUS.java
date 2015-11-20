package discover.vdis.enums;

import java.util.List;

/**
 * WS_THUN_STATUS: This class is auto-generated by vdis.EnumGenerator
 */
public final class WS_THUN_STATUS extends Value {

    public static final WS_THUN_STATUS
       WST_THUNDER_STATUS_OFF = new WS_THUN_STATUS(0, "WST_THUNDER_STATUS_OFF", "Off", true),
       WST_THUNDER_STATUS_INSTANTANEOUS = new WS_THUN_STATUS(1, "WST_THUNDER_STATUS_INSTANTANEOUS", "Instantaneous Thunder", true),
       WST_THUNDER_STATUS_RANDOM = new WS_THUN_STATUS(2, "WST_THUNDER_STATUS_RANDOM", "Random Thunder", true),
       WST_THUNDER_STATUS_COINCIDENT_LIGHTNING = new WS_THUN_STATUS(3, "WST_THUNDER_STATUS_COINCIDENT_LIGHTNING", "Thunder Coincident with Lightning", true),
       WST_THUNDER_STATUS_OVERCAST = new WS_THUN_STATUS(4, "WST_THUNDER_STATUS_OVERCAST", "Overcast", true);

    private WS_THUN_STATUS(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, WS_THUN_STATUS.class);
    }

    /** @see Value#values(Class) */
    public static List<WS_THUN_STATUS> values() {

        return values(WS_THUN_STATUS.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<WS_THUN_STATUS> values(boolean known) {

        return values(WS_THUN_STATUS.class, known);
    }

    /** @see Value#get(int, Class) */
    public static WS_THUN_STATUS get(int value) {

        return get(value, WS_THUN_STATUS.class);
    }
}

