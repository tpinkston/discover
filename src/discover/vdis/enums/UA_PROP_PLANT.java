package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum UA_PROP_PLANT implements EnumInterface {

    UA_PROP_PLANT_OTHER(0, "Other"),
    UA_PROP_PLANT_DIESEL_ELECTRIC(1, "Diesel/Electric"),
    UA_PROP_PLANT_DIESEL(2, "Diesel"),
    UA_PROP_PLANT_BATTERY(3, "Battery"),
    UA_PROP_PLANT_TURBINE_REDUCTION(4, "Turbine Reduction"),
    UA_PROP_PLANT_STEAM(6, "Steam"),
    UA_PROP_PLANT_GAS_TURBINE(7, "Gas Turbine");

    private final int value;
    private final String description;

    private UA_PROP_PLANT(int value, String description) {

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

