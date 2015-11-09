package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum STATION_NAME implements EnumInterface {

    STATION_OTHER(0, "Other"),
    STATION_AIRCRAFT_WINGSTATION(1, "Aircraft Wingstation"),
    STATION_SHIPS_FORWARD_GUNMOUNT_STARBOARD(2, "Ships Forward Gunmount (Starboard)"),
    STATION_SHIPS_FORWARD_GUNMOUNT_PORT(3, "Ships Forward Gunmount (Port)"),
    STATION_SHIPS_FORWARD_GUNMOUNT_CENTERLINE(4, "Ships Forward Gunmount (Centerline)"),
    STATION_SHIPS_AFT_GUNMOUNT_STARBOARD(5, "Ships Aft Gunmount (Starboard)"),
    STATION_SHIPS_AFT_GUNMOUNT_PORT(6, "Ships Aft Gunmount (Port)"),
    STATION_SHIPS_AFT_GUNMOUNT_CENTERLINE(7, "Ships Aft Gunmount (Centerline)"),
    STATION_FORWARD_TORPEDO_TUBE(8, "Forward Torpedo Tube"),
    STATION_AFT_TORPEDO_TUBE(9, "Aft Torpedo Tube"),
    STATION_BOMB_BAY(10, "Bomb Bay"),
    STATION_CARGO_BAY(11, "Cargo Bay"),
    STATION_TRUCK_BED(12, "Truck Bed"),
    STATION_TRAILER_BED(13, "Trailer Bed"),
    STATION_WELL_DECK(14, "Well Deck"),
    STATION_ON_STATION_RNG_BRG(15, "On Station - (Rng/Brg)"),
    STATION_ON_STATION_XYZ(16, "On Station - (X,Y,Z)");

    private final int value;
    private final String description;

    private STATION_NAME(int value, String description) {

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

        return Enumerations.getUnknownValue(value, STATION_NAME.class);
    }
}

