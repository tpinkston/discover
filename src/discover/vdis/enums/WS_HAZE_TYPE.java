package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_HAZE_TYPE implements EnumInterface {

    WSH_TYPE_NO_HAZE(0, "No Haze"),
    WSH_TYPE_OTHER_HAZE(1, "Other Haze"),
    WSH_TYPE_RURUAL_HAZE(2, "Rurual Haze"),
    WSH_TYPE_MARITIME_HAZE(3, "Maritime Haze"),
    WSH_TYPE_URBAN_HAZE(4, "Urban Haze"),
    WSH_TYPE_TROPOSPHERIC_HAZE(5, "Tropospheric Haze"),
    WSH_TYPE_ADVECTIVE_FOG_HAZE(6, "Advective Fog Haze"),
    WSH_TYPE_RADIATIVE_FOG_HAZE(7, "Radiative Fog Haze"),
    WSH_TYPE_DESERT_HAZE(8, "Desert Haze"),
    WSH_TYPE_DESERT_SUMMER_HAZE(9, "Desert Summer Haze"),
    WSH_TYPE_DESERT_WINTER_HAZE(10, "Desert Winter Haze"),
    WSH_TYPE_TEMPERATE_SUMMER_DAY(11, "Temperate Summer Day"),
    WSH_TYPE_TEMPERATE_SUMMER_NIGHT(12, "Temperate Summer Night"),
    WSH_TYPE_TEMPERATE_WINTER(13, "Temperate Winter"),
    WSH_TYPE_DUST_STORM_HAZE(14, "Dust Storm Haze"),
    WSH_TYPE_SNOW_HAZE_(15, "Snow Haze "),
    WSH_TYPE_BLOWING_SNOW_HAZE(16, "Blowing Snow Haze"),
    WSH_TYPE_FOG_AND_SNOW_HAZE(17, "Fog and Snow Haze");

    private final int value;
    private final String description;

    private WS_HAZE_TYPE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, WS_HAZE_TYPE.class);
    }
}

