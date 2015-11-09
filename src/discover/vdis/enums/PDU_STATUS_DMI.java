package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PDU_STATUS_DMI implements EnumInterface {

    PDU_STATUS_DMI_GUISE_MODE(0, "Guise Mode"),
    PDU_STATUS_DMI_DISGUISE_MODE(1, "Disguise Mode");

    private final int value;
    private final String description;

    private PDU_STATUS_DMI(int value, String description) {

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

        return Enumerations.getUnknownValue(value, PDU_STATUS_DMI.class);
    }
}

