package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum RECORD_TYPE implements EnumInterface {

    RECORD_ENTITY_DESTINATION_RECORD(1, "Entity Destination Record"),
    RECORD_GROUP_DESTINATION_RECORD(2, "Group Destination Record"),
    RECORD_GROUP_ASSIGNMENT_RECORD(3, "Group Assignment Record");

    private final int value;
    private final String description;

    private RECORD_TYPE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, RECORD_TYPE.class);
    }
}

