package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author Tony Pinkston
 */
public enum APP_CTRL_CONTROL_TYPE implements EnumInterface {

    OTHER(0, "Other"),
    SHUTDOWN(1, "Shutdown"),
    DATA_QUERY(2, "Data Query"),
    DATA(3, "Data"),
    SET_DATA(4, "Set Data"),
    ADD_DATA(5, "Add Data"),
    REMOVE_DATA(6, "Remove Data"),
    STATUS(7, "Status");

    private final int value;
    private final String description;

    private APP_CTRL_CONTROL_TYPE(int value, String description) {

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

