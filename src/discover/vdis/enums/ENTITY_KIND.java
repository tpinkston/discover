package discover.vdis.enums;

public enum ENTITY_KIND {

    PLATFORMS(1, "Platforms"),
    MUNITIONS(2, "Munitions"),
    LIFEFORMS(3, "Life Forms"),
    ENVIRONMENTALS(4, "Environmentals"),
    CULTURAL_FEATURES(5, "Cultural Features"),
    SUPPLIES(6, "Supplies"),
    RADIOS(7, "Radios"),
    EXPENDABLES(8, "Expendables"),
    SENSOR_EMITTERS(9, "Sensor Emitters");

    private final int value;
    private final String description;

    private ENTITY_KIND(int value, String description) {

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

