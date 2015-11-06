package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum EVENT_TYPE implements EnumInterface {

    EVENT_TYPE_OTHER(0, "Other"),
    EVENT_TYPE_UNUSED(1, "Unused"),
    EVENT_TYPE_RAN_OUT_OF_AMMO(2, "Ran Out of Ammunition"),
    EVENT_TYPE_KILLED_IN_ACTION(3, "Killed in Action (KIA)"),
    EVENT_TYPE_DAMAGE(4, "Damage"),
    EVENT_TYPE_MOBILITY_DISABLED(5, "Mobility Disabled"),
    EVENT_TYPE_RAN_OUT_OF_FUEL(7, "Fire Disabled"),
    EVENT_TYPE_FIRE_DISABLED(6, "Ran Out of Fuel"),
    EVENT_TYPE_ENTITY_INITIALIZATION(8, "Entity Initialization"),
    EVENT_TYPE_REQ_INDIRECT_FIRE_OR_CAS_MISSION(9, "Request for Indirect Fire or CAS Mission"),
    EVENT_TYPE_INDIRECT_FIRE_OR_CAS_FIRE(10, "Indirect Fire or CAS Fire"),
    EVENT_TYPE_MINEFIELD_ENTRY(11, "Minefield Entry"),
    EVENT_TYPE_MINEFIELD_DETONATION(12, "Minefield Detonation"),
    EVENT_TYPE_VEHICLE_MASTER_POWER_ON(13, "Vehicle Master Power On"),
    EVENT_TYPE_VEHICLE_MASTER_POWER_OFF(14, "Vehicle Master Power Off"),
    EVENT_TYPE_AGGREGATE_STATE_CHANGE_REQ(15, "Aggregate State Change Requested"),
    EVENT_TYPE_PREVENT_COLLISION_DETONATION(16, "Prevent Collision / Detonation"),
    EVENT_TYPE_OWNERSHIP_REPORT(17, "Ownership Report"),
    EVENT_TYPE_RADAR_PERCEPTION(18, "Radar Perception");

    private final int value;
    private final String description;

    private EVENT_TYPE(int value, String description) {

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

