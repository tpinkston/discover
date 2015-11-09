package discover.vdis.enums;

import discover.vdis.EnumInterface;

public enum ON_OFF implements EnumInterface {

    OFF(0, "Off"),
    ON(1, "ON");

    private final int value;
    private final String description;

    private ON_OFF(int value, String description) {

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

