package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author tpinkston
 */
public enum LF_EQ_LASER implements EnumInterface {

    NONE(0, "None"),
    LASER_POINTER(1, "Laser Pointer"),
    LASER_DESIGNATOR(2, "Laser Designator"),
    LASER_RANGE_FINDER(3, "Laser Range Finder");

    private final int value;
    private final String description;

    private LF_EQ_LASER(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LF_EQ_LASER.class);
    }
}

