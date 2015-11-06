package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum LF_EQ_CHUTE implements EnumInterface {

    LF_EQUIP_CHUTE_DOES_NOT_HAVE(0, "Does not have"),
    LF_EQUIP_CHUTE_NOT_DEPLOYED(1, "Not Deployed"),
    LF_EQUIP_CHUTE_DEPLOYED(2, "Deployed");

    private final int value;
    private final String description;

    private LF_EQ_CHUTE(int value, String description) {

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

