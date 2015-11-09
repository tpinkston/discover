package discover.vdis.enums;

import discover.vdis.EnumInterface;

public enum APP_CTRL_CONTROL_TYPE implements EnumInterface {

    OTHER(1, "Other"),
    SHUTDOWN(2, "Shutdown"),
    DATA_QUERY(3, "Data Query"),
    DATA(4, "Data"),
    SET_DATA(5, "Set Data"),
    ADD_DATA(6, "Add Data"),
    REMOVE_DATA(7, "Remove Data"),
    STATUS(8, "Status");

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
}

