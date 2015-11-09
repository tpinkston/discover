package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ENT_ASSOC_STATUS implements EnumInterface {

    EAVP_ENT_ASSOC_STATUS_NOT_SPECIFIED(0, "Not Specified"),
    EAVP_ENT_ASSOC_STATUS_PHYS_ASSOC_TARGET(1, "Physical Association Target"),
    EAVP_ENT_ASSOC_STATUS_NON_PHYSICAL_ASSOC(2, "Non Physical Association"),
    EAVP_ENT_ASSOC_STATUS_ASSOCIATION_BROKEN(3, "Association Broken"),
    EAVP_ENT_ASSOC_STATUS_PHYS_ASSOC_CARRIER(4, "Physical Association Carrier");

    private final int value;
    private final String description;

    private ENT_ASSOC_STATUS(int value, String description) {

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

        return Enumerations.getUnknownValue(value, ENT_ASSOC_STATUS.class);
    }
}

