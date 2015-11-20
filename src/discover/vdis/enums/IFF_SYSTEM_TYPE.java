package discover.vdis.enums;

import java.util.List;

/**
 * IFF_SYSTEM_TYPE: This class is auto-generated by vdis.EnumGenerator
 */
public final class IFF_SYSTEM_TYPE extends Value {

    public static final IFF_SYSTEM_TYPE
       IFF_SYS_TYPE_NOT_USED = new IFF_SYSTEM_TYPE(0, "IFF_SYS_TYPE_NOT_USED", "Not Used", true),
       IFF_SYS_TYPE_MARK_X_XII_ATCRBS_TRANS = new IFF_SYSTEM_TYPE(1, "IFF_SYS_TYPE_MARK_X_XII_ATCRBS_TRANS", "Mark X/XII/ATCRBS Transponder", true),
       IFF_SYS_TYPE_MARK_X_XII_ATCRBS_INT = new IFF_SYSTEM_TYPE(2, "IFF_SYS_TYPE_MARK_X_XII_ATCRBS_INT", "Mark X/XII/ATCRBS Interrogator", true),
       IFF_SYS_TYPE_SOVIET_TRANS = new IFF_SYSTEM_TYPE(3, "IFF_SYS_TYPE_SOVIET_TRANS", "Soviet Transponder", true),
       IFF_SYS_TYPE_SOVIET_INT = new IFF_SYSTEM_TYPE(4, "IFF_SYS_TYPE_SOVIET_INT", "Soviet Interrogator", true),
       IFF_SYS_TYPE_RRB_TRANS = new IFF_SYSTEM_TYPE(5, "IFF_SYS_TYPE_RRB_TRANS", "RRB Transponder", true),
       IFF_SYS_TYPE_MARK_XIIA_INT = new IFF_SYSTEM_TYPE(6, "IFF_SYS_TYPE_MARK_XIIA_INT", "Mark XIIA Interrogator", true),
       IFF_SYS_TYPE_MODE_5_INT = new IFF_SYSTEM_TYPE(7, "IFF_SYS_TYPE_MODE_5_INT", "Mode 5 Interrogator", true),
       IFF_SYS_TYPE_MODE_S_INT = new IFF_SYSTEM_TYPE(8, "IFF_SYS_TYPE_MODE_S_INT", "Mode S Interrogator", true),
       IFF_SYS_TYPE_MARK_XIIA_TRANS = new IFF_SYSTEM_TYPE(9, "IFF_SYS_TYPE_MARK_XIIA_TRANS", "Mark XIIA Transponder", true),
       IFF_SYS_TYPE_MODE_5_TRANS = new IFF_SYSTEM_TYPE(10, "IFF_SYS_TYPE_MODE_5_TRANS", "Mode 5 Transponder", true),
       IFF_SYS_TYPE_MODE_S_TRANS = new IFF_SYSTEM_TYPE(11, "IFF_SYS_TYPE_MODE_S_TRANS", "Mode S Transponder", true),
       IFF_SYS_TYPE_MARK_XIIA_CIT = new IFF_SYSTEM_TYPE(12, "IFF_SYS_TYPE_MARK_XIIA_CIT", "Mark XIIA Combined Interrogator/Transponder (CIT)", true),
       IFF_SYS_TYPE_MARK_XII_CIT = new IFF_SYSTEM_TYPE(13, "IFF_SYS_TYPE_MARK_XII_CIT", "Mark XIA Combined Interrogator/Transponder (CIT)", true),
       IFF_SYS_TYPE_TCAS_ACAS_TRANSCEIVER = new IFF_SYSTEM_TYPE(14, "IFF_SYS_TYPE_TCAS_ACAS_TRANSCEIVER", "TCAS/ACAS Transceiver", true);

    private IFF_SYSTEM_TYPE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, IFF_SYSTEM_TYPE.class);
    }

    /** @see Value#values(Class) */
    public static List<IFF_SYSTEM_TYPE> values() {

        return values(IFF_SYSTEM_TYPE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<IFF_SYSTEM_TYPE> values(boolean known) {

        return values(IFF_SYSTEM_TYPE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static IFF_SYSTEM_TYPE get(int value) {

        return get(value, IFF_SYSTEM_TYPE.class);
    }
}

