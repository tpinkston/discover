package discover.vdis.enums;

import java.util.List;

/**
 * NATURE: This class is auto-generated by vdis.EnumGenerator
 */
public final class NATURE extends Value {

    public static final NATURE
       OTHER = new NATURE(0, "OTHER", "Other", true),
       HOST_FIREABLE_MUNITION = new NATURE(1, "HOST_FIREABLE_MUNITION", "Host-Fireable Munition", true),
       MUNITION_CARRIED_AS_CARGO = new NATURE(2, "MUNITION_CARRIED_AS_CARGO", "Munition Carried As Cargo", true),
       FUEL_CARRIED_AS_CARGO = new NATURE(3, "FUEL_CARRIED_AS_CARGO", "Fuel Carried As Cargo", true),
       GUNMOUNT_ATTACHED_TO_HOST = new NATURE(4, "GUNMOUNT_ATTACHED_TO_HOST", "Gunmount Attached To Host", true),
       COMPUTER_GENERATED_FORCES_CARRIED_AS_CARGO = new NATURE(5, "COMPUTER_GENERATED_FORCES_CARRIED_AS_CARGO", "Computer Generated Forces Carried As Cargo", true),
       VEHICLE_CARRIED_AS_CARGO = new NATURE(6, "VEHICLE_CARRIED_AS_CARGO", "Vehicle Carried As Cargo", true),
       EMITTER_MOUNTED_ON_HOST = new NATURE(7, "EMITTER_MOUNTED_ON_HOST", "Emitter Mounted On Host", true),
       MOBILE_COMMAND_AND_CONTROL_ENTITY_CARRIED_ABOARD_HOST = new NATURE(8, "MOBILE_COMMAND_AND_CONTROL_ENTITY_CARRIED_ABOARD_HOST", "Mobile Command And Control Entity Carried Aboard Host", true),
       ENTITY_STATIONED_AT_WITH_RESPECT_TO_HOST = new NATURE(9, "ENTITY_STATIONED_AT_WITH_RESPECT_TO_HOST", "Entity Stationed At With Respect To Host", true),
       TEAM_MEMBER_IN_FORMATION_WITH = new NATURE(10, "TEAM_MEMBER_IN_FORMATION_WITH", "Team Member In Formation With", true);

    private NATURE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, NATURE.class);
    }

    /** @see Value#values(Class) */
    public static List<NATURE> values() {

        return values(NATURE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<NATURE> values(boolean known) {

        return values(NATURE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static NATURE get(int value) {

        return get(value, NATURE.class);
    }
}

