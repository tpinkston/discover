package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author tpinkston
 */
public enum ENT_KIND implements EnumInterface {

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

    private ENT_KIND(int value, String description) {

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

