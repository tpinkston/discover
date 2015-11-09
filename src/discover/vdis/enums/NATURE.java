package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum NATURE implements EnumInterface {

    NATURE_OTHER(0, "Other"),
    NATURE_HOST_FIREABLE_MUNITION(1, "Host-Fireable Munition"),
    NATURE_MUNITION_CARRIED_AS_CARGO(2, "Munition Carried As Cargo"),
    NATURE_FUEL_CARRIED_AS_CARGO(3, "Fuel Carried As Cargo"),
    NATURE_GUNMOUNT_ATTACHED_TO_HOST(4, "Gunmount Attached To Host"),
    NATURE_COMPUTER_GENERATED_FORCES_CARRIED_AS_CARGO(5, "Computer Generated Forces Carried As Cargo"),
    NATURE_VEHICLE_CARRIED_AS_CARGO(6, "Vehicle Carried As Cargo"),
    NATURE_EMITTER_MOUNTED_ON_HOST(7, "Emitter Mounted On Host"),
    NATURE_MOBILE_COMMAND_AND_CONTROL_ENTITY_CARRIED_ABOARD_HOST(8, "Mobile Command And Control Entity Carried Aboard Host"),
    NATURE_ENTITY_STATIONED_AT_WITH_RESPECT_TO_HOST(9, "Entity Stationed At With Respect To Host"),
    NATURE_TEAM_MEMBER_IN_FORMATION_WITH(10, "Team Member In Formation With");

    private final int value;
    private final String description;

    private NATURE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, NATURE.class);
    }
}

