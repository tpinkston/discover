package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author tpinkston
 */
public enum ENT_DOMAIN implements EnumInterface {

    OTHER(0, "Other"),
    LAND(1, "Land"),
    AIR(2, "Air"),
    SURFACE(3, "Surface"),
    SUBSURFACE(4, "Subsurface"),
    SPACE(5, "Space");

    private final int value;
    private final String description;

    private ENT_DOMAIN(int value, String description) {

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

        return Enumerations.getUnknownValue(value, FORCE_ID.class);
    }
}

