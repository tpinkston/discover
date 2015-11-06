package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum OBJECT_KIND implements EnumInterface {

    OBJECT_KIND_OTHER(0, "Other"),
    OBJECT_KIND_OBSTACLE(1, "Obstacle"),
    OBJECT_KIND_PREPARED_POSITION(2, "Prepared Position"),
    OBJECT_KIND_CULTURAL_FEATURE(3, "Cultural Feature"),
    OBJECT_KIND_PASSAGEWAY(4, "Passageway"),
    OBJECT_KIND_TACTICAL_SMOKE(5, "Tactical Smoke"),
    OBJECT_KIND_OBSTACLE_MARKER(6, "Obstacle Marker"),
    OBJECT_KIND_OBSTACLE_BREACH(7, "Obstacle Breach"),
    OBJECT_KIND_ENV_OBJECT(8, "Environmental Object");

    private final int value;
    private final String description;

    private OBJECT_KIND(int value, String description) {

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

