package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum IFF_SYSTEM_TYPE implements EnumInterface {

    IFF_SYS_TYPE_NOT_USED(0, "Not Used"),
    IFF_SYS_TYPE_MARK_X_XII_ATCRBS_TRANS(1, "Mark X/XII/ATCRBS Transponder"),
    IFF_SYS_TYPE_MARK_X_XII_ATCRBS_INT(2, "Mark X/XII/ATCRBS Interrogator"),
    IFF_SYS_TYPE_SOVIET_TRANS(3, "Soviet Transponder"),
    IFF_SYS_TYPE_SOVIET_INT(4, "Soviet Interrogator"),
    IFF_SYS_TYPE_RRB_TRANS(5, "RRB Transponder"),
    IFF_SYS_TYPE_MARK_XIIA_INT(6, "Mark XIIA Interrogator"),
    IFF_SYS_TYPE_MODE_5_INT(7, "Mode 5 Interrogator"),
    IFF_SYS_TYPE_MODE_S_INT(8, "Mode S Interrogator"),
    IFF_SYS_TYPE_MARK_XIIA_TRANS(9, "Mark XIIA Transponder"),
    IFF_SYS_TYPE_MODE_5_TRANS(10, "Mode 5 Transponder"),
    IFF_SYS_TYPE_MODE_S_TRANS(11, "Mode S Transponder"),
    IFF_SYS_TYPE_MARK_XIIA_CIT(12, "Mark XIIA Combined Interrogator/Transponder (CIT)"),
    IFF_SYS_TYPE_MARK_XII_CIT(13, "Mark XIA Combined Interrogator/Transponder (CIT)"),
    IFF_SYS_TYPE_TCAS_ACAS_TRANSCEIVER(14, "TCAS/ACAS Transceiver");

    private final int value;
    private final String description;

    private IFF_SYSTEM_TYPE(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getName() {

        return name();
    }

    @Override
    public String getDescription() {

        return description;
    }

    public static EnumInterface getValue(int value) {

        for(EnumInterface element : values()) {

            if (element.getValue() == value) {

                return element;
            }
        }

        return Enumerations.getUnknownValue(value, IFF_SYSTEM_TYPE.class);
    }
}

