package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PROTOCOL_VERSION implements EnumInterface {

    PTCL_VER_OTHER(0, "Other"),
    PTCL_VER_DIS_1_0_MAY_1992(1, "DIS PDU version 1.0 (May 92)"),
    PTCL_VER_IEEE_1278_1993(2, "IEEE 1278-1993"),
    PTCL_VER_DIS_2_0_THIRD_DRAFT(3, "DIS PDU version 2.0 - third draft (May 93)"),
    PTCL_VER_DIS_2_0_FOURTH_DRAFT(4, "DIS PDU version 2.0 - fourth draft (revised) March 16, 1994"),
    PTCL_VER_IEEE_1278_1_1995(5, "IEEE 1278.1-1995"),
    PTCL_VER_IEEE_1278_1A_1998(6, "IEEE 1278.1A-1998"),
    PTCL_VER_IEEE_1278_1_2012(7, "IEEE 1278.1-2012");

    private final int value;
    private final String description;

    private PROTOCOL_VERSION(int value, String description) {

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

        return Enumerations.getUnknownValue(value, PROTOCOL_VERSION.class);
    }
}

