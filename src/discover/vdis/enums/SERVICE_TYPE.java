package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum SERVICE_TYPE implements EnumInterface {

    SERV_TYPE_OTHER(0, "Other"),
    SERV_TYPE_RESUPPLY(1, "Resupply"),
    SERV_TYPE_REPAIR(2, "Repair");

    private final int value;
    private final String description;

    private SERVICE_TYPE(int value, String description) {

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

