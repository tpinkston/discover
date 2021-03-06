package discover.vdis.enums;

import java.util.List;

/**
 * WS_CEL_SEASON: This class is auto-generated by vdis.EnumGenerator
 */
public final class WS_CEL_SEASON extends Value {

    public static final WS_CEL_SEASON
       WSC_SEASON_SUMMER = new WS_CEL_SEASON(0, "WSC_SEASON_SUMMER", "Summer", true),
       WSC_SEASON_WINTER = new WS_CEL_SEASON(1, "WSC_SEASON_WINTER", "Winter", true),
       WSC_SEASON_SPRING = new WS_CEL_SEASON(2, "WSC_SEASON_SPRING", "Spring", true),
       WSC_SEASON_AUTUMN = new WS_CEL_SEASON(3, "WSC_SEASON_AUTUMN", "Autumn", true);

    private WS_CEL_SEASON(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, WS_CEL_SEASON.class);
    }

    /** @see Value#values(Class) */
    public static List<WS_CEL_SEASON> values() {

        return values(WS_CEL_SEASON.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<WS_CEL_SEASON> values(boolean known) {

        return values(WS_CEL_SEASON.class, known);
    }

    /** @see Value#get(int, Class) */
    public static WS_CEL_SEASON get(int value) {

        return get(value, WS_CEL_SEASON.class);
    }
}

