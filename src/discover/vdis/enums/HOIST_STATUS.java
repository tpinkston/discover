package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author Tony Pinkston
 */
public enum HOIST_STATUS implements EnumInterface {

    NOT_PRESENT(0, "Not Present"),
    UNAVAILABLE(1, "Unavailable"),
    AVAILABLE(2, "Available");

    private final int value;
    private final String description;

    private HOIST_STATUS(int value, String description) {

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

        return Enumerations.getUnknownValue(value, HOIST_STATUS.class);
    }
}

