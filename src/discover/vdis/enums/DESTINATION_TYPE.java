package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DESTINATION_TYPE implements EnumInterface {

    DEST_TYPE_URN(0, "URN"),
    DEST_TYPE_IPV4(1, "IPV4"),
    DEST_TYPE_SUBSCRIBER_ID(2, "Subscriber ID");

    private final int value;
    private final String description;

    private DESTINATION_TYPE(int value, String description) {

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

