package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author Tony Pinkston
 */
public enum IED_PRESENCE implements EnumInterface {

    NONE(0, "None"),
    VISIBLE(1, "Visible"),
    PARTIALLY_HIDDEN(2, "Partially Hidden"),
    COMPLETELY_HIDDEN(3, "Completely Hidden");

    private final int value;
    private final String description;

    private IED_PRESENCE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, IED_PRESENCE.class);
    }
}

