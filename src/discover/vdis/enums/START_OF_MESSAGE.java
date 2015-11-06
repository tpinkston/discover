package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum START_OF_MESSAGE implements EnumInterface {

    START_NOT_START_OF_MESSAGE(0, "Not Start Of Message"),
    START_START_OF_MESSAGE(1, "Start Of Message");

    private final int value;
    private final String description;

    private START_OF_MESSAGE(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getDescription() {

        return description;
    }
}

