package discover.vdis.enums;

import java.util.List;

/**
 * WS_CEL_TOD_MODE: This class is auto-generated by vdis.EnumGenerator
 */
public final class WS_CEL_TOD_MODE extends Value {

    public static final WS_CEL_TOD_MODE
       WSC_TODMODE_SCENE = new WS_CEL_TOD_MODE(0, "WSC_TODMODE_SCENE", "Scene", true),
       WSC_TODMODE_STATIC = new WS_CEL_TOD_MODE(1, "WSC_TODMODE_STATIC", "Static", true),
       WSC_TODMODE_CONTINUOUS = new WS_CEL_TOD_MODE(2, "WSC_TODMODE_CONTINUOUS", "Continuous", true);

    private WS_CEL_TOD_MODE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, WS_CEL_TOD_MODE.class);
    }

    /** @see Value#values(Class) */
    public static List<WS_CEL_TOD_MODE> values() {

        return values(WS_CEL_TOD_MODE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<WS_CEL_TOD_MODE> values(boolean known) {

        return values(WS_CEL_TOD_MODE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static WS_CEL_TOD_MODE get(int value) {

        return get(value, WS_CEL_TOD_MODE.class);
    }
}

