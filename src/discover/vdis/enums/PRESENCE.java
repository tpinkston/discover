package discover.vdis.enums;

import discover.vdis.EnumInterface;

public enum PRESENCE implements EnumInterface {

    NOT_PRESENT(0, "Not Present"),
    PRESENT(1, "Present");

    private final int value;
    private final String description;

    private PRESENCE(int value, String description) {

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

