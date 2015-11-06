package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum GRP_MEM_TYPE implements EnumInterface {

    GRP_MEM_TYPE_NOT_SPECIFIED(0, "Not Specified"),
    GRP_MEM_TYPE_GROUP_LEADER(1, "Group Leader"),
    GRP_MEM_TYPE_GROUP_MEMBER(2, "Group Member"),
    GRP_MEM_TYPE_FORMATION_LEADER(3, "Formation Leader"),
    GRP_MEM_TYPE_FORMATION_MEMBER(4, "Formation Member"),
    GRP_MEM_TYPE_CONVOY_LEADER(5, "Convoy Leader"),
    GRP_MEM_TYPE_CONVOY_MEMBER(6, "Convoy Member");

    private final int value;
    private final String description;

    private GRP_MEM_TYPE(int value, String description) {

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

