package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author Tony Pinkston
 */
public enum LF_CAMOUFLAGE_TYPE implements EnumInterface {

    DESERT(0, "Desert Camouflage"),
    WINTER(1, "Winter Camouflage"),
    FOREST(2, "Forest Camouflage"),
    NONE(3, "No Camouflage");

    private final int value;
    private final String description;

    private LF_CAMOUFLAGE_TYPE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LF_CAMOUFLAGE_TYPE.class);
    }
}

