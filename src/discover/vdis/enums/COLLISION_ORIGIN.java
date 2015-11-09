package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum COLLISION_ORIGIN implements EnumInterface {

    COLL_ORIGIN_NO_STATEMENT(0, "No Statement"),
    COLL_ORIGIN_COLL_ATTACHED_PART(1, "Collision with Attached Part"),
    COLL_ORIGIN_COLL_ARTICULATED_PART(2, "Collision with Articulated Part"),
    COLL_ORIGIN_COLL_MOUNTED_ENTITY(3, "Collision with Mounted Entity"),
    COLL_ORIGIN_COLL_SLING_LOAD(4, "Collision with Sling Load"),
    COLL_ORIGIN_COLL_TOWED_ENTITY(5, "Collision with Towed Entity");

    private final int value;
    private final String description;

    private COLLISION_ORIGIN(int value, String description) {

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

        return Enumerations.getUnknownValue(value, COLLISION_ORIGIN.class);
    }
}

