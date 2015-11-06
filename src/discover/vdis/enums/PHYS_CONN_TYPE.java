package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PHYS_CONN_TYPE implements EnumInterface {

    EAVP_PHYS_CONN_TYPE_NOT_SPECIFIED(0, "Not Specified"),
    EAVP_PHYS_CONN_TYPE_ATTACHED_TO_SURFACE(1, "Attached Directly to Surface"),
    EAVP_PHYS_CONN_TYPE_CABLE_WIRE(2, "Cable Wire"),
    EAVP_PHYS_CONN_TYPE_ROPE(3, "Rope"),
    EAVP_PHYS_CONN_TYPE_CHAIN(4, "Chain"),
    EAVP_PHYS_CONN_TYPE_POWER_LINE(5, "Power Line"),
    EAVP_PHYS_CONN_TYPE_TELEPHONE_LINE(6, "Telephone Line"),
    EAVP_PHYS_CONN_TYPE_CABLE_LINE(7, "Cable Line"),
    EAVP_PHYS_CONN_TYPE_REFUELING_DROGUE(8, "Refueling Drogue"),
    EAVP_PHYS_CONN_TYPE_REFUELING_BOOM(9, "Refueling Boom"),
    EAVP_PHYS_CONN_TYPE_HANDCUFFS(10, "Handcuffs"),
    EAVP_PHYS_CONN_TYPE_IN_CONTACT_WITH(11, "In Contact With"),
    EAVP_PHYS_CONN_TYPE_FAST_ROPE(12, "Fast Rope");

    private final int value;
    private final String description;

    private PHYS_CONN_TYPE(int value, String description) {

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

