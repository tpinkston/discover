package discover.vdis.enums;

public enum ENTITY_DOMAIN {

    OTHER(0, "Other"),
    LAND(1, "Land"),
    AIR(2, "Air"),
    SURFACE(3, "Surface"),
    SUBSURFACE(4, "Subsurface"),
    SPACE(5, "Space");

    private final int value;
    private final String description;

    private ENTITY_DOMAIN(int value, String description) {

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

