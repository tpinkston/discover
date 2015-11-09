package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum GROUPED_ENTITY implements EnumInterface {

    GROUPED_ENTITY_UNDEFINED(0, "Undefined"),
    GROUPED_ENTITY_BASIC_GROUND_COMBAT_VEHICLE(1, "Basic Ground Combat Vehicle"),
    GROUPED_ENTITY_ENHANCED_GROUND_COMBAT_VEHICLE(2, "Enhanced Ground Combat Vehicle"),
    GROUPED_ENTITY_BASIC_GROUND_COMBAT_SOLDIER(3, "Basic Ground Combat Soldier"),
    GROUPED_ENTITY_ENHANCED_GROUND_COMBAT_SOLDIER(4, "Enhanced Ground Combat Soldier"),
    GROUPED_ENTITY_BASIC_ROTOR_WING_AIRCRAFT(5, "Basic Rotor Wing Aircraft"),
    GROUPED_ENTITY_ENHANCED_ROTOR_WING_AIRCRAFT(6, "Enhanced Rotor Wing Aircraft"),
    GROUPED_ENTITY_BASIC_FIXED_WING_AIRCRAFT(7, "Basic Fixed Wing Aircraft"),
    GROUPED_ENTITY_ENHANCED_FIXED_WING_AIRCRAFT(8, "Enhanced Fixed Wing Aircraft"),
    GROUPED_ENTITY_GROUND_LOGISTICS_VEHICLE(9, "Ground Logistics Vehicle");

    private final int value;
    private final String description;

    private GROUPED_ENTITY(int value, String description) {

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

        return Enumerations.getUnknownValue(value, GROUPED_ENTITY.class);
    }
}

