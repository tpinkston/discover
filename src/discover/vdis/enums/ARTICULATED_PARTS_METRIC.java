package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author tpinkston
 */
public enum ARTICULATED_PARTS_METRIC implements EnumInterface {

    EMPTY(0, "Empty"),
    POSITION(1, "Position"),
    POSITION_RATE(2, "Position Rate"),
    EXTENSION(3, "Extension"),
    EXTENSION_RATE(4, "Extension Rate"),
    X(5, "X"),
    X_RAT(6, ",X Rate"),
    Y(7, "Y"),
    Y_RATE(8, "Y Rate"),
    Z(9, "Z"),
    Z_RATE(10, "Z Rate"),
    AZIMUTH(11, "Azimuth"),
    AZIMUTH_RATE(12, "Azimuth Rate"),
    ELEVATION(13, "Elevation"),
    ELEVATION_RATE(14, "Elevation Rate"),
    ROTATION(15, "Rotation"),
    ROTATION_RATE(16, "Rotation Rate");

    private final int value;
    private final String description;

    private ARTICULATED_PARTS_METRIC(int value, String description) {

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

        return Enumerations.getUnknownValue(value, ARTICULATED_PARTS_METRIC.class);
    }
}

