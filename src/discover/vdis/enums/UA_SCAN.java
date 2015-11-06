package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum UA_SCAN implements EnumInterface {

    UA_SCAN_UNUSED(0, "Unused"),
    UA_SCAN_CONICAL(1, "Conical"),
    UA_SCAN_HELICAL(2, "Helical"),
    UA_SCAN_RASTER(3, "Raster"),
    UA_SCAN_SECTOR_SEARCH(4, "Sector Search"),
    UA_SCAN_CONTINUOUS_SEARCH(5, "Continuous Search");

    private final int value;
    private final String description;

    private UA_SCAN(int value, String description) {

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

