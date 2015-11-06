package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum REPAIR_TYPE implements EnumInterface {

    REPAIR_TYPE_NO_REPAIRS_PERFORMED(0, "No Repairs Performed"),
    REPAIR_TYPE_ALL_REQUESTED_REPAIRS_PERFORMED(1, "All Requested Repairs Performed"),
    REPAIR_TYPE_DRIVE_TRAIN_MOTOR_ENGINE(10, "Drive Train - Motor, Engine"),
    REPAIR_TYPE_DRIVE_TRAIN_STARTER(20, "Drive Train - Starter"),
    REPAIR_TYPE_DRIVE_TRAIN_ALTERNATOR(30, "Drive Train - Alternator"),
    REPAIR_TYPE_DRIVE_TRAIN_GENERATOR(40, "Drive Train - Generator"),
    REPAIR_TYPE_DRIVE_TRAIN_BATTERY(50, "Drive Train - Battery"),
    REPAIR_TYPE_DRIVE_TRAIN_ENGINE_COOLANT_LEAK(60, "Drive Train - Engine Coolant Leak"),
    REPAIR_TYPE_DRIVE_TRAIN_FUEL_FILTER(70, "Drive Train - Fuel Filter"),
    REPAIR_TYPE_DRIVE_TRAIN_TRANSMISSION_OIL_LEAK(80, "Drive Train - Transmission Oil Leak"),
    REPAIR_TYPE_DRIVE_TRAIN_ENGINE_OIL_LEAK(90, "Drive Train - Engine Oil Leak"),
    REPAIR_TYPE_DRIVE_TRAIN_PUMPS(100, "Drive Train - Pumps"),
    REPAIR_TYPE_DRIVE_TRAIN_FILTERS(110, "Drive Train - Filters"),
    REPAIR_TYPE_DRIVE_TRAIN_TRANSMISSION(120, "Drive Train - Transmission"),
    REPAIR_TYPE_DRIVE_TRAIN_BRAKES(130, "Drive Train - Brakes"),
    REPAIR_TYPE_DRIVE_TRAIN_SUSPENSION_SYSTEM(140, "Drive Train - Suspension System"),
    REPAIR_TYPE_DRIVE_TRAIN_OIL_FILTER(150, "Drive Train - Oil Filter"),
    REPAIR_TYPE_HULL_AIRFRAME_B_HULL(1000, "Hull/Airframe/B - Hull"),
    REPAIR_TYPE_HULL_AIRFRAME_B_AIRFRAME(1010, "Hull/Airframe/B - Airframe"),
    REPAIR_TYPE_HULL_AIRFRAME_B_TRUCK_BODY(1020, "Hull/Airframe/B - Truck Body"),
    REPAIR_TYPE_HULL_AIRFRAME_B_TANK_BODY(1030, "Hull/Airframe/B - Tank Body"),
    REPAIR_TYPE_HULL_AIRFRAME_B_TRAILER_BODY(1040, "Hull/Airframe/B - Trailer Body"),
    REPAIR_TYPE_HULL_AIRFRAME_B_TURRET(1050, "Hull/Airframe/B - Turret"),
    REPAIR_TYPE_ENVIRON_PROPELLER(1500, "Environ - Propeller"),
    REPAIR_TYPE_ENVIRON_FILTERS(1520, "Environ - Filters"),
    REPAIR_TYPE_ENVIRON_WHEELS(1540, "Environ - Wheels"),
    REPAIR_TYPE_ENVIRON_TIRE(1550, "Environ - Tire"),
    REPAIR_TYPE_ENVIRON_TRACK(1560, "Environ - Track"),
    REPAIR_TYPE_WEAPONS_GUN_ELEVATION_DRIVE(2000, "Weapons - Gun Elevation Drive"),
    REPAIR_TYPE_WEAPONS_GUN_STABILIZATION_SYSTEM(2010, "Weapons - Gun Stabilization System"),
    REPAIR_TYPE_WEAPONS_GPS(2020, "Weapons - Gunner Primary Sight (Gps)"),
    REPAIR_TYPE_WEAPONS_CMDR_EXTENSION_TO_THE_GPS(2030, "Weapons - Commander Extension To The Gps"),
    REPAIR_TYPE_WEAPONS_LOADING_MECHANISM(2040, "Weapons - Loading Mechanism"),
    REPAIR_TYPE_WEAPONS_GUNNER_AUXILIARY_SIGHT(2050, "Weapons - Gunner Auxiliary Sight"),
    REPAIR_TYPE_WEAPONS_GUNNER_CONTROL_PANEL(2060, "Weapons - Gunner Control Panel"),
    REPAIR_TYPE_WEAPONS_GUNNER_CONTROL_ASSEMBLY_HANDLE(2070, "Weapons - Gunner Control Assembly Handle"),
    REPAIR_TYPE_WEAPONS_CMDR_CTRL_HANDLES_ASSEMBLY(2090, "Weapons - Commander Control Handles, Assembly"),
    REPAIR_TYPE_WEAPONS_CMDR_WEAPON_STATION(2100, "Weapons - Commander Weapon Station"),
    REPAIR_TYPE_WEAPONS_CITV(2110, "Weapons - Commander Independent Thermal Viewer (Citv)"),
    REPAIR_TYPE_WEAPONS_GENERAL(2120, "Weapons - General"),
    REPAIR_TYPE_FUEL_SYSTEMS_FUEL_TRANSFER_PUMP(4000, "Fuel Systems - Fuel Transfer Pump"),
    REPAIR_TYPE_FUEL_SYSTEMS_FUEL_LINES(4010, "Fuel Systems - Fuel Lines"),
    REPAIR_TYPE_FUEL_SYSTEMS_GAUGES(4020, "Fuel Systems - Gauges"),
    REPAIR_TYPE_FUEL_SYSTEMS_GENERAL_FUEL_SYSTEM(4030, "Fuel Systems - General Fuel System"),
    REPAIR_TYPE_ELECTRONICS_ELECTRONIC_WARFARE_SYSTEMS(4500, "Electronics - Electronic Warfare Systems"),
    REPAIR_TYPE_ELECTRONICS_DETECTION_SYSTEMS(4600, "Electronics - Detection Systems"),
    REPAIR_TYPE_ELECTRONICS_DET_SYS_RADIO_FREQUENCY(4610, "Electronics Det Sys - Radio Frequency"),
    REPAIR_TYPE_ELECTRONICS_DET_SYS_MICROWAVE(4620, "Electronics Det Sys - Microwave"),
    REPAIR_TYPE_ELECTRONICS_DET_SYS_INFRARED(4630, "Electronics Det Sys - Infrared"),
    REPAIR_TYPE_ELECTRONICS_DET_SYS_LASER(4640, "Electronics Det Sys - Laser"),
    REPAIR_TYPE_ELECTRONICS_RANGE_FINDERS(4700, "Electronics - Range Finders"),
    REPAIR_TYPE_ELECTRONICS_RANGE_ONLY_RADAR(4710, "Electronics - Range Only Radar"),
    REPAIR_TYPE_ELECTRONICS_LASER_RANGE_FINDER(4720, "Electronics - Laser Range Finder"),
    REPAIR_TYPE_ELECTRONICS_ELECTRONIC_SYSTEMS(4800, "Electronics - Electronic Systems"),
    REPAIR_TYPE_ELECTRONICS_SYS_RADIO_FREQUENCY(4810, "Electronics Sys - Radio Frequency"),
    REPAIR_TYPE_ELECTRONICS_SYS_MICROWAVE(4820, "Electronics Sys - Microwave"),
    REPAIR_TYPE_ELECTRONICS_SYS_INFRARED(4830, "Electronics Sys - Infrared"),
    REPAIR_TYPE_ELECTRONICS_SYS_LASER(4840, "Electronics Sys - Laser"),
    REPAIR_TYPE_ELECTRONICS_RADIOS(5000, "Electronics - Radios"),
    REPAIR_TYPE_ELECTRONICS_COMM_SYSTEMS(5010, "Electronics - Communication Systems"),
    REPAIR_TYPE_ELECTRONICS_INTERCOMS(5100, "Electronics - Intercoms"),
    REPAIR_TYPE_ELECTRONICS_ENCODERS(5200, "Electronics - Encoders"),
    REPAIR_TYPE_ELECTRONICS_ENCRYPTION_DEVICES(5250, "Electronics - Encryption Devices"),
    REPAIR_TYPE_ELECTRONICS_DECODERS(5300, "Electronics - Decoders"),
    REPAIR_TYPE_ELECTRONICS_DECRYPTION_DEVICES(5350, "Electronics - Decryption Devices"),
    REPAIR_TYPE_ELECTRONICS_COMPUTERS(5500, "Electronics - Computers"),
    REPAIR_TYPE_ELECTRONICS_NAV_AND_CONTROL_SYSTEMS(6000, "Electronics - Navigation And Control Systems"),
    REPAIR_TYPE_ELECTRONICS_FIRE_CONTROL_SYSTEMS(6500, "Electronics - Fire Control Systems"),
    REPAIR_TYPE_LIFE_SUPPORT_AIR_SUPPLY(8000, "Life Support - Air Supply"),
    REPAIR_TYPE_LIFE_SUPPORT_FILTERS(8010, "Life Support - Filters"),
    REPAIR_TYPE_LIFE_SUPPORT_WATER_SUPPLY(8020, "Life Support - Water Supply"),
    REPAIR_TYPE_LIFE_SUPPORT_REFRIGERATION_SYSTEM(8030, "Life Support - Refrigeration System"),
    REPAIR_TYPE_LIFE_SUPPORT_CHEM_BIO_RAD_PROTECTION(8040, "Life Support - Chemical, Biological, And Radiological Protection"),
    REPAIR_TYPE_LIFE_SUPPORT_WATER_WASH_DOWN_SYSTEMS(8050, "Life Support - Water Wash Down Systems"),
    REPAIR_TYPE_LIFE_SUPPORT_DECONTAMINATION_SYSTEMS(8060, "Life Support - Decontamination Systems"),
    REPAIR_TYPE_HYDRAULIC_WATER_SUPPLY(9000, "Hydraulic - Water Supply"),
    REPAIR_TYPE_HYDRAULIC_COOLING_SYSTEM(9010, "Hydraulic - Cooling System"),
    REPAIR_TYPE_HYDRAULIC_WINCHES(9020, "Hydraulic - Winches"),
    REPAIR_TYPE_HYDRAULIC_CATAPULTS(9030, "Hydraulic - Catapults"),
    REPAIR_TYPE_HYDRAULIC_CRANES(9040, "Hydraulic - Cranes"),
    REPAIR_TYPE_HYDRAULIC_LAUNCHERS(9050, "Hydraulic - Launchers"),
    REPAIR_TYPE_AUXILIARY_LIFE_BOATS(10000, "Auxiliary - Life Boats"),
    REPAIR_TYPE_AUXILIARY_LANDING_CRAFT(10010, "Auxiliary - Landing Craft"),
    REPAIR_TYPE_AUXILIARY_EJECTION_SEATS(10020, "Auxiliary - Ejection Seats");

    private final int value;
    private final String description;

    private REPAIR_TYPE(int value, String description) {

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

