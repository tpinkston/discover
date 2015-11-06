package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum VMF_VERSION_ENUM implements EnumInterface {

    VMF_VERSION_TIDP_TE_R2(0, "TIDP-TE R2"),
    VMF_VERSION_TIDP_TE_R3(1, "TIDP-TE R3"),
    VMF_VERSION_TIDP_TE_R4(2, "TIDP-TE R4"),
    VMF_VERSION_TIDP_TE_R5(3, "TIDP-TE R5"),
    VMF_VERSION_TIDP_TE_R6(4, "TIDP-TE R6"),
    VMF_VERSION_TIDP_TE_R7(5, "TIDP-TE R7"),
    VMF_VERSION_6017(6, "6017"),
    VMF_VERSION_6017A(7, "6017A"),
    VMF_VERSION_6017B(8, "6017B"),
    VMF_VERSION_6017C(9, "6017C"),
    VMF_VERSION_DCX2_AV(16, "DCX2 AV");

    private final int value;
    private final String description;

    private VMF_VERSION_ENUM(int value, String description) {

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

