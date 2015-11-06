package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum EDS_DMG_STATUS implements EnumInterface {

    EDS_DMG_STATUS_NO_DAMAGE(0, "No Damage"),
    EDS_DMG_STATUS_MINOR_DAMAGE(1, "Minor Damage"),
    EDS_DMG_STATUS_MEDIUM_DAMAGE(2, "Medium Damage"),
    EDS_DMG_STATUS_MAJOR_DAMAGE(3, "Major Damage"),
    EDS_DMG_STATUS_DESTROYED(4, "Destroyed");

    private final int value;
    private final String description;

    private EDS_DMG_STATUS(int value, String description) {

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

