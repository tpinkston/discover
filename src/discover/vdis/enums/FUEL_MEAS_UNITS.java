package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum FUEL_MEAS_UNITS implements EnumInterface {

    ENG_FUEL_FMU_OTHER(0, "Other"),
    ENG_FUEL_FMU_LITER(1, "Liter"),
    ENG_FUEL_FMU_KILOGRAM(2, "Kilogram");

    private final int value;
    private final String description;

    private FUEL_MEAS_UNITS(int value, String description) {

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

        return Enumerations.getUnknownValue(value, FUEL_MEAS_UNITS.class);
    }
}

