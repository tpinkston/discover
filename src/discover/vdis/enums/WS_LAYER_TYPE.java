package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_LAYER_TYPE implements EnumInterface {

    WSLY_LAYER_TYPE_GROUND_FOG(0, "Ground Fog"),
    WSLY_LAYER_TYPE_CLOUD_LAYER_1(1, "Cloud Layer 1"),
    WSLY_LAYER_TYPE_CLOUD_LAYER_2(2, "Cloud Layer 2"),
    WSLY_LAYER_TYPE_CLOUD_LAYER_3(3, "Cloud Layer 3"),
    WSLY_LAYER_TYPE_RAIN(4, "Rain"),
    WSLY_LAYER_TYPE_SNOW(5, "Snow"),
    WSLY_LAYER_TYPE_SLEET(6, "Sleet"),
    WSLY_LAYER_TYPE_HAIL(7, "Hail"),
    WSLY_LAYER_TYPE_SAND_(8, "Sand "),
    WSLY_LAYER_TYPE_DUST(9, "Dust"),
    WSLY_LAYER_TYPE_HAZE(10, "Haze");

    private final int value;
    private final String description;

    private WS_LAYER_TYPE(int value, String description) {

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

