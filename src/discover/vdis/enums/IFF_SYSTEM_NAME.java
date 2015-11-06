package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum IFF_SYSTEM_NAME implements EnumInterface {

    IFF_SYS_NAME_OTHER(0, "Not Used"),
    IFF_SYS_NAME_MARK_X(1, "Generic Mark X"),
    IFF_SYS_NAME_MARK_XII(2, "Generic Mark XII"),
    IFF_SYS_NAME_ATCRBS(3, "Generic ATCRBS"),
    IFF_SYS_NAME_SOVIET(4, "Generic Soviet"),
    IFF_SYS_NAME_MODE_S(5, "Generic Mode S"),
    IFF_SYS_NAME_MARK_X_XII_ATCRBS(6, "Generic Mark X/XII/ATCRBS"),
    IFF_SYS_NAME_MARK_X_XII_ATCRBS_MODE_S(7, "Generic Mark X/XII/ATCRBS/Mode S"),
    IFF_SYS_NAME_ARI_5954(8, "ARI 5954 (RRB)"),
    IFF_SYS_NAME_ARI_5983(9, "ARI 5983 (RRB)"),
    IFF_SYS_NAME_RRB(10, "Generic RRB "),
    IFF_SYS_NAME_MARK_XIIA(11, "Generic Mark XIIA   "),
    IFF_SYS_NAME_MODE_5(12, "Generic Mode 5"),
    IFF_SYS_NAME_MARK_XIIA_CIT(13, "Generic Mark XIIA Combined Interrogator/Transponder (CIT) "),
    IFF_SYS_NAME_MARK_XII_CIT(14, "Generic Mark XII Combined Interrogator/Transponder (CIT) "),
    IFF_SYS_NAME_TCAS_I_ACAS_I_TRANSCEIVER(15, "Generic TCAS I/ACAS I Transceiver"),
    IFF_SYS_NAME_TCAS_II_ACAS_II_TRANSCEIVER(16, "Generic TCAS II/ACAS II Transceiver"),
    IFF_SYS_NAME_MARK_X_A(17, "Generic Mark X (A)"),
    IFF_SYS_NAME_MARK_X_SIF(18, "Generic Mark X (SIF)");

    private final int value;
    private final String description;

    private IFF_SYSTEM_NAME(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getDescription() {

        return description;
    }
}

