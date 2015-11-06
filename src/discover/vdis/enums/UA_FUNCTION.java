package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum UA_FUNCTION implements EnumInterface {

    UA_FUNC_OTHER(0, "Other"),
    UA_FUNC_PLATFORM_SEARCH(1, "Platform Search/Detect/Track"),
    UA_FUNC_NAVIGATION(2, "Navigation"),
    UA_FUNC_MINE_HUNTING(3, "Mine Hunting"),
    UA_FUNC_WEAPON_SEARCH(4, "Weapon Search/Detect/Track/Detect");

    private final int value;
    private final String description;

    private UA_FUNCTION(int value, String description) {

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

