package discover.vdis.enums;

import discover.vdis.EnumInterface;

public enum YES_NO implements EnumInterface {

    NO(0, "No"),
    YES(1, "Yes");

    private final int value;
    private final String description;

    private YES_NO(int value, String description) {

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

