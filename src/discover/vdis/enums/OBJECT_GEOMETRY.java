package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author tpinkston
 */
public enum OBJECT_GEOMETRY implements EnumInterface {

    UNKNOWN(0, "Unknown"),
    POINT(1, "Point"),
    LINEAR(1, "Linear"),
    AREAL(1, "Areal");

    private final int value;
    private final String description;

    private OBJECT_GEOMETRY(int value, String description) {

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

        return Enumerations.getUnknownValue(value, OBJECT_GEOMETRY.class);
    }
}

