package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DESIG_SPOT_TYPE implements EnumInterface {

    DES_SPOT_TYPE_NOT_SPECIFIED(0, "Not Specified"),
    DES_SPOT_TYPE_TERRAIN_MODEL(1, "Terrain - terrain model"),
    DES_SPOT_TYPE_NO_TERRAIN_MODEL(2, "Terrain - no terrain model (zero-elevation)"),
    DES_SPOT_TYPE_WATER_SURFACE(3, "Water Surface"),
    DES_SPOT_TYPE_FOLIAGE(4, "Foliage or other natural object attached to terrain"),
    DES_SPOT_TYPE_MAN_MADE_OBJECT(5, "Man-made object attached to terrain"),
    DES_SPOT_TYPE_CLOUDS(6, "Clouds"),
    DES_SPOT_TYPE_OBSCURANT(7, "Obscurant (e.g. smoke)"),
    DES_SPOT_TYPE_NON_INCIDENT(8, "Non-incident (e.g. clear sky)");

    private final int value;
    private final String description;

    private DESIG_SPOT_TYPE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, DESIG_SPOT_TYPE.class);
    }
}

