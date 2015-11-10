package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author tpinkston
 */
public enum LAND_SMOKE implements EnumInterface {

    NONE(0, "No Damage"),
    RISING(1, "Rising Smoke"),
    ENGINE(2, "Emitting Engine Smoke"),
    ENGINE_RISING(3, "Emitting Engine Smoke and Rising Smoke");

    private final int value;
    private final String description;

    private LAND_SMOKE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LAND_SMOKE.class);
    }
}

