package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PL_COND_MTL implements EnumInterface {

    PLAT_COND_MTL_NOT_SPECIFIED(0, "Not Specified"),
    PLAT_COND_MTL_METALLIC(1, "Metallic (shiny)"),
    PLAT_COND_MTL_PLASTIC(2, "Plastic (matte)"),
    PLAT_COND_MTL_ROUGH(3, "Rough (coarse)");

    private final int value;
    private final String description;

    private PL_COND_MTL(int value, String description) {

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

        return Enumerations.getUnknownValue(value, PL_COND_MTL.class);
    }
}

