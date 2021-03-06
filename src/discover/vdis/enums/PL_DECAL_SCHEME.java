package discover.vdis.enums;

import java.util.List;

/**
 * PL_DECAL_SCHEME: This class is auto-generated by vdis.EnumGenerator
 */
public final class PL_DECAL_SCHEME extends Value {

    public static final PL_DECAL_SCHEME
       PLAT_DECAL_SCHEME_NONE = new PL_DECAL_SCHEME(0, "PLAT_DECAL_SCHEME_NONE", "None", true),
       PLAT_DECAL_SCHEME_UN = new PL_DECAL_SCHEME(1, "PLAT_DECAL_SCHEME_UN", "United Nations (UN)", true),
       PLAT_DECAL_SCHEME_FED_EX = new PL_DECAL_SCHEME(2, "PLAT_DECAL_SCHEME_FED_EX", "Fed Ex", true),
       PLAT_DECAL_SCHEME_COCA_COLA = new PL_DECAL_SCHEME(3, "PLAT_DECAL_SCHEME_COCA_COLA", "Coca-Cola", true),
       PLAT_DECAL_SCHEME_PEPSI_COLA = new PL_DECAL_SCHEME(4, "PLAT_DECAL_SCHEME_PEPSI_COLA", "Pepsi-Cola", true),
       PLAT_DECAL_SCHEME_YELLOW_CAB = new PL_DECAL_SCHEME(5, "PLAT_DECAL_SCHEME_YELLOW_CAB", "Yellow Cab", true),
       PLAT_DECAL_SCHEME_CHANNEL_5_NEWS = new PL_DECAL_SCHEME(6, "PLAT_DECAL_SCHEME_CHANNEL_5_NEWS", "Channel 5 News", true);

    private PL_DECAL_SCHEME(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, PL_DECAL_SCHEME.class);
    }

    /** @see Value#values(Class) */
    public static List<PL_DECAL_SCHEME> values() {

        return values(PL_DECAL_SCHEME.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<PL_DECAL_SCHEME> values(boolean known) {

        return values(PL_DECAL_SCHEME.class, known);
    }

    /** @see Value#get(int, Class) */
    public static PL_DECAL_SCHEME get(int value) {

        return get(value, PL_DECAL_SCHEME.class);
    }
}

