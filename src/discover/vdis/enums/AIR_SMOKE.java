package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author tpinkston
 */
public enum AIR_SMOKE implements EnumInterface {

    NONE(0, "No Damage"),
    TRAILING(1, "Trailing Smoke"),
    ENGINE(2, "Emitting Engine Smoke"),
    ENGINE_TRAILING(3, "Emitting Engine Smoke and Trailing Smoke");

    private final int value;
    private final String description;

    private AIR_SMOKE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, AIR_SMOKE.class);
    }
}

