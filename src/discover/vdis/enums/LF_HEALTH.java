package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author tpinkston
 */
public enum LF_HEALTH implements EnumInterface {

    NO_INJURY(0, "No Injury"),
    SLIGHT_INJURY(1, "Slight Injury"),
    MODERATE_INJURY(2, "Moderate Injury"),
    FATAL_INJURY(3, "Fatal Injury");

    private final int value;
    private final String description;

    private LF_HEALTH(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LF_HEALTH.class);
    }
}

