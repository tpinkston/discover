package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum CFS_TETHER implements EnumInterface {

    CFS_TETHER_INIT_TETHER_WHOLE_GROUP_BY_LEAD(0, "Initiate Tether Whole Group by Lead"),
    CFS_TETHER_INIT_TETHER_SUBGROUP_BY_LEAD(1, "Initiate Tether Sub-Group by Lead"),
    CFS_TETHER_INIT_TETHER_BY_FOLLOW(2, "Initiate Tether by Follow"),
    CFS_TETHER_UNTETHER_FORM_NOT_SPECIFIED(3, "Untether - Formation Not Specified"),
    CFS_TETHER_UNTETHER_FORM_SERVICE_STATION(4, "Untether - Service Station Formation"),
    CFS_TETHER_UNTETHER_FORM_TAILGATE_RESUPPLY(5, "Untether - Tailgate Resupply Formation");

    private final int value;
    private final String description;

    private CFS_TETHER(int value, String description) {

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

