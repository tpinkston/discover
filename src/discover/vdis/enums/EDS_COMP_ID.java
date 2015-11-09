package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum EDS_COMP_ID implements EnumInterface {

    EDS_COMP_ID_OTHER(0, "Other"),
    EDS_COMP_ID_ENTITY_STRUCTURE(1, "Entity Structure"),
    EDS_COMP_ID_CTRL_SYSTEM(2, "Control System"),
    EDS_COMP_ID_CTRL_SURFACE(3, "Control Surface"),
    EDS_COMP_ID_ENGINE_PROP_SYS(4, "Engine / Propulsion System"),
    EDS_COMP_ID_CREW_MEMBER(5, "Crew Member"),
    EDS_COMP_ID_FUSE(6, "Fuse"),
    EDS_COMP_ID_ACQ_SENSOR(7, "Acquisition Sensor"),
    EDS_COMP_ID_TRACKING_SENSOR(8, "Tracking Sensor"),
    EDS_COMP_ID_FUEL_TANK_SOLID_ROCKET_MOTOR(9, "Fuel Tank / Solid Rocket Motor");

    private final int value;
    private final String description;

    private EDS_COMP_ID(int value, String description) {

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

        return Enumerations.getUnknownValue(value, EDS_COMP_ID.class);
    }
}

