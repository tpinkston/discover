package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author Tony Pinkston
 */
public enum LF_ETHNICITY implements EnumInterface {

    NOT_SPECIFIED(0, "Not Specified"),
    ASIAN(1, "Asian"),
    PACIFIC_ISLANDER(2, "Pacific Islander"),
    BLACK(3, "Black"),
    EAST_ASIAN(4, "East Asian"),
    HISPANIC(5, "Hispanic"),
    WHITE(6, "White"),
    ARAB(7, "Arab"),
    HOMOGENOUS_CTRY(8, "Homogenous Country Code"),
    INDIGENOUS_CTRY(9, "Indigenous Country Code");

    private final int value;
    private final String description;

    private LF_ETHNICITY(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LF_ETHNICITY.class);
    }
}

