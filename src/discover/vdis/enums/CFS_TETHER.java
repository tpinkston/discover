package discover.vdis.enums;

import java.util.List;

/**
 * CFS_TETHER: This class is auto-generated by vdis.EnumGenerator
 */
public final class CFS_TETHER extends Value {

    public static final CFS_TETHER
       INIT_TETHER_WHOLE_GROUP_BY_LEAD = new CFS_TETHER(0, "INIT_TETHER_WHOLE_GROUP_BY_LEAD", "Initiate Tether Whole Group by Lead", true),
       INIT_TETHER_SUBGROUP_BY_LEAD = new CFS_TETHER(1, "INIT_TETHER_SUBGROUP_BY_LEAD", "Initiate Tether Sub-Group by Lead", true),
       INIT_TETHER_BY_FOLLOW = new CFS_TETHER(2, "INIT_TETHER_BY_FOLLOW", "Initiate Tether by Follow", true),
       UNTETHER_FORM_NOT_SPECIFIED = new CFS_TETHER(3, "UNTETHER_FORM_NOT_SPECIFIED", "Untether - Formation Not Specified", true),
       UNTETHER_FORM_SERVICE_STATION = new CFS_TETHER(4, "UNTETHER_FORM_SERVICE_STATION", "Untether - Service Station Formation", true),
       UNTETHER_FORM_TAILGATE_RESUPPLY = new CFS_TETHER(5, "UNTETHER_FORM_TAILGATE_RESUPPLY", "Untether - Tailgate Resupply Formation", true);

    private CFS_TETHER(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, CFS_TETHER.class);
    }

    /** @see Value#values(Class) */
    public static List<CFS_TETHER> values() {

        return values(CFS_TETHER.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<CFS_TETHER> values(boolean known) {

        return values(CFS_TETHER.class, known);
    }

    /** @see Value#get(int, Class) */
    public static CFS_TETHER get(int value) {

        return get(value, CFS_TETHER.class);
    }
}

