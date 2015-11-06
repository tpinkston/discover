package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_CEL_SEASON implements EnumInterface {

    WSC_SEASON_SUMMER(0, "Summer"),
    WSC_SEASON_WINTER(1, "Winter"),
    WSC_SEASON_SPRING(2, "Spring"),
    WSC_SEASON_AUTUMN(3, "Autumn");

    private final int value;
    private final String description;

    private WS_CEL_SEASON(int value, String description) {

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

