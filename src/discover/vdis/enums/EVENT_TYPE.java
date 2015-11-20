package discover.vdis.enums;

import java.util.List;

/**
 * EVENT_TYPE: This class is auto-generated by vdis.EnumGenerator
 */
public final class EVENT_TYPE extends Value {

    public static final EVENT_TYPE
       OTHER = new EVENT_TYPE(0, "OTHER", "Other", true),
       UNUSED = new EVENT_TYPE(1, "UNUSED", "Unused", true),
       RAN_OUT_OF_AMMO = new EVENT_TYPE(2, "RAN_OUT_OF_AMMO", "Ran Out of Ammunition", true),
       KILLED_IN_ACTION = new EVENT_TYPE(3, "KILLED_IN_ACTION", "Killed in Action (KIA)", true),
       DAMAGE = new EVENT_TYPE(4, "DAMAGE", "Damage", true),
       MOBILITY_DISABLED = new EVENT_TYPE(5, "MOBILITY_DISABLED", "Mobility Disabled", true),
       RAN_OUT_OF_FUEL = new EVENT_TYPE(7, "RAN_OUT_OF_FUEL", "Fire Disabled", true),
       FIRE_DISABLED = new EVENT_TYPE(6, "FIRE_DISABLED", "Ran Out of Fuel", true),
       ENTITY_INITIALIZATION = new EVENT_TYPE(8, "ENTITY_INITIALIZATION", "Entity Initialization", true),
       REQ_INDIRECT_FIRE_OR_CAS_MISSION = new EVENT_TYPE(9, "REQ_INDIRECT_FIRE_OR_CAS_MISSION", "Request for Indirect Fire or CAS Mission", true),
       INDIRECT_FIRE_OR_CAS_FIRE = new EVENT_TYPE(10, "INDIRECT_FIRE_OR_CAS_FIRE", "Indirect Fire or CAS Fire", true),
       MINEFIELD_ENTRY = new EVENT_TYPE(11, "MINEFIELD_ENTRY", "Minefield Entry", true),
       MINEFIELD_DETONATION = new EVENT_TYPE(12, "MINEFIELD_DETONATION", "Minefield Detonation", true),
       VEHICLE_MASTER_POWER_ON = new EVENT_TYPE(13, "VEHICLE_MASTER_POWER_ON", "Vehicle Master Power On", true),
       VEHICLE_MASTER_POWER_OFF = new EVENT_TYPE(14, "VEHICLE_MASTER_POWER_OFF", "Vehicle Master Power Off", true),
       AGGREGATE_STATE_CHANGE_REQ = new EVENT_TYPE(15, "AGGREGATE_STATE_CHANGE_REQ", "Aggregate State Change Requested", true),
       PREVENT_COLLISION_DETONATION = new EVENT_TYPE(16, "PREVENT_COLLISION_DETONATION", "Prevent Collision / Detonation", true),
       OWNERSHIP_REPORT = new EVENT_TYPE(17, "OWNERSHIP_REPORT", "Ownership Report", true),
       RADAR_PERCEPTION = new EVENT_TYPE(18, "RADAR_PERCEPTION", "Radar Perception", true);

    private EVENT_TYPE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, EVENT_TYPE.class);
    }

    /** @see Value#values(Class) */
    public static List<EVENT_TYPE> values() {

        return values(EVENT_TYPE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<EVENT_TYPE> values(boolean known) {

        return values(EVENT_TYPE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static EVENT_TYPE get(int value) {

        return get(value, EVENT_TYPE.class);
    }
}

