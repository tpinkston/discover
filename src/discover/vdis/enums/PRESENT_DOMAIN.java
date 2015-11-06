package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PRESENT_DOMAIN implements EnumInterface {

    PRESENT_DOM_ENUM_UNCHANGED(0, "Unchanged"),
    PRESENT_DOM_ENUM_LAND(1, "Land"),
    PRESENT_DOM_ENUM_AIR(2, "Air"),
    PRESENT_DOM_ENUM_SURFACE(3, "Surface"),
    PRESENT_DOM_ENUM_SUBSURFACE(4, "Subsurface"),
    PRESENT_DOM_ENUM_SPACE(5, "Space"),
    PRESENT_DOM_ENUM_SUBTERRANEAN_LAND(6, "Subterranean Land"),
    PRESENT_DOM_ENUM_SUBTERRANEAN_AIR(7, "Subterranean Air"),
    PRESENT_DOM_ENUM_SUBTERRANEAN_SURFACE(8, "Subterranean Surface"),
    PRESENT_DOM_ENUM_SUBTERRANEAN_SUBSURFACE(9, "Subterranean Subsurface"),
    PRESENT_DOM_ENUM_SUBSURFACE_TERRAIN(10, "Subsurface Terrain"),
    PRESENT_DOM_ENUM_SUBSURFACE_CAVES(11, "Subsurface Caves"),
    PRESENT_DOM_ENUM_INTERIOR_OTHER_ENTITY(12, "Interior Other Entity/Object"),
    PRESENT_DOM_ENUM_EXTERIOR_OTHER_ENTITY(13, "Exterior Other Entity/Object"),
    PRESENT_DOM_ENUM_ADMIN_INACTIVE(14, "Administratively Inactive");

    private final int value;
    private final String description;

    private PRESENT_DOMAIN(int value, String description) {

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

