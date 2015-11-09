package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum TRANSFER_TYPE implements EnumInterface {

    TRANSFER_TYPE_OTHER(0, "Other"),
    TRANSFER_TYPE_PUSH_TRANSFER_ENTITY(1, "Push - Transfer Entity"),
    TRANSFER_TYPE_AUTO_PULL_TRANSFER_ENTITY(2, "Automatic Pull - Transfer Entity"),
    TRANSFER_TYPE_MUTUAL_XCH_ENTITY(3, "Mutual Exchange Entity"),
    TRANSFER_TYPE_PUSH_TRANSFER_ENV_PROC(4, "Push - Transfer Environmental Process"),
    TRANSFER_TYPE_AUTO_PULL_TRANSFER_ENV_PROC(5, "Automatic Pull - Transfer Environmental Process"),
    TRANSFER_TYPE_MUTUAL_XCH_ENV_PROC(6, "Mutual Exchange Environmental Process"),
    TRANSFER_TYPE_CANCEL_TRANSFER(7, "Cancel Transfer"),
    TRANSFER_TYPE_MAN_PULL_TRANSFER_ENTITY(8, "Manual Pull - Transfer Entity"),
    TRANSFER_TYPE_MAN_PULL_TRANSFER_ENV_PROC(9, "Manual Pull - Transfer Environmental Process"),
    TRANSFER_TYPE_REMOVE_ENTITY(10, "Remove Entity");

    private final int value;
    private final String description;

    private TRANSFER_TYPE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, TRANSFER_TYPE.class);
    }
}

