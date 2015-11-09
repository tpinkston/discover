package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum REPAIR_RESPONSE implements EnumInterface {

    REPAIR_RESP_OTHER(0, "Other"),
    REPAIR_RESP_REPAIR_ENDED(1, "Repair Ended"),
    REPAIR_RESP_INVALID_REPAIR(2, "Invalid Repair"),
    REPAIR_RESP_REPAIR_INTERRUPTED(3, "Repair Interrupted"),
    REPAIR_RESP_SERVICE_CANCELED_BY_SUPPLIER(4, "Service Canceled By Supplier");

    private final int value;
    private final String description;

    private REPAIR_RESPONSE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, REPAIR_RESPONSE.class);
    }
}

