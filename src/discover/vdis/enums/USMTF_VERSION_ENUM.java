package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum USMTF_VERSION_ENUM implements EnumInterface {

    USMTF_VERSION_1993(0, "USMTF 1993"),
    USMTF_VERSION_1995(1, "USMTF 1995"),
    USMTF_VERSION_1997(2, "USMTF 1997"),
    USMTF_VERSION_1998(3, "USMTF 1998"),
    USMTF_VERSION_1999(4, "USMTF 1999"),
    USMTF_VERSION_2000(5, "USMTF 2000"),
    USMTF_VERSION_2001(6, "USMTF 2001"),
    USMTF_VERSION_2002(7, "USMTF 2002"),
    USMTF_VERSION_2003(8, "USMTF 2003"),
    USMTF_VERSION_2004(9, "USMTF 2004");

    private final int value;
    private final String description;

    private USMTF_VERSION_ENUM(int value, String description) {

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

