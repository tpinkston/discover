package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum AREAL_MINEFIELD_BREACH implements EnumInterface {

    AREAL_MINEFIELD_BREACH_NO_BREACHING(0, "No breaching"),
    AREAL_MINEFIELD_BREACH_BREACHED(1, "Breached"),
    AREAL_MINEFIELD_BREACH_CLEARED(2, "Cleared");

    private final int value;
    private final String description;

    private AREAL_MINEFIELD_BREACH(int value, String description) {

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

        return Enumerations.getUnknownValue(value, AREAL_MINEFIELD_BREACH.class);
    }
}

