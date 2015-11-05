package discover.vdis.enums;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PLAT_CAT_LAND {

    TANK(1, "Tank"),
    AFV(2, "Armored Fighting Vehicle"),
    AUV(3, "Armored Utility Vehicle"),
    SPA(4, "Self-propelled Artillery"),
    TOW(5, "Towed Artillery"),
    SWUV(6, "Small Wheeled Utility Vehicle"),
    LWUV(7, "Large Wheeled Utility Vehicle"),
    STUV(8, "Small Tracked Utility Vehicle"),
    LTUV(9, "Large Tracked Utility Vehicle"),
    MORT(10, "Mortar"),
    PLOW(11, "Mine plow"),
    RAKE(12, "Mine rake"),
    ROLL(13, "Mine roller"),
    CARGO(14, "Cargo trailer"),
    FUEL(15, "Fuel trailer"),
    GEN(16, "Generator trailer"),
    WATER(17, "Water trailer"),
    ENG(18, "Engineer equipment"),
    HET(19, "Heavy equipment transport trailer"),
    MET(20, "Maintenance equipment trailer"),
    LIMB(21, "Limber"),
    CDEC(22, "Chemical decontamination trailer"),
    WARN(23, "Warning System"),
    TREN(24, "Train - Engine"),
    TRCAR(25, "Train - Car"),
    TRCAB(26, "Train - Caboose"),
    CIV(27, "Civilian Vehicle"),
    ADUE(28, "Air Defense / Missile Defense Unit Equipment"),
    C3I(29, "Command, Control, Communications, and Intelligence (C3I) System"),
    OPS(30, "Operations Facility"),
    INT(31, "Intelligence Facility"),
    SURV(32, "Surveillance Facility"),
    COMU(33, "Communications Facility"),
    COM(34, "Command Facility"),
    C4I(35, "C4I Facility"),
    CTRL(36, "Control Facility"),
    FIRE(37, "Fire Control Facility"),
    MDEF(38, "Missile Defense Facility"),
    FCOM(39, "Field Command Post"),
    OBS(40, "Observation Post"),
    GWV(41, "General Wheeled Vehicles"),
    MOTORCYCLE(80, "null"),
    CAR_SEDAN(81, "null"),
    BUS(82, "null"),
    SINGLE_UNIT_CARGO_TRUCK(83, "null"),
    SINGLE_UNIT_UTILITY_EMERGENCY_TRUCK(84, "null"),
    MULTIPLE_UNIT_CARGO_TRUCK(85, "null"),
    MULTIPLE_UNIT_UTILITY_EMERGENCY_TRUCK(86, "null"),
    CONSTRUCTION_SPECIALTY_VEHICLE(87, "null"),
    FARM_SPECIALTY_VEHICLE(88, "null"),
    TRAILER(89, "null"),
    CAR_COUPE(90, "null"),
    CAR_3_DOOR_HATCHBACK(91, "null"),
    CAR_5_DOOR_HATCHBACK(92, "null"),
    RECREATIONAL(93, "null"),
    NON_MOTORIZED(94, "null"),
    TRAINS(95, "null"),
    UTILITY_EMERGENCY_CAR(96, "null");

    private final int value;
    private final String description;

    private PLAT_CAT_LAND(int value, String description) {

        this.value = value;
        this.description = description;
    }

    public int getValue() {

        return value;
    }

    public String getDescription() {

        return description;
    }
}

