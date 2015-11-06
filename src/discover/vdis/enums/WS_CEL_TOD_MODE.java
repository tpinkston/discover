package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_CEL_TOD_MODE implements EnumInterface {

    WSC_TODMODE_SCENE(0, "Scene"),
    WSC_TODMODE_STATIC(1, "Static"),
    WSC_TODMODE_CONTINUOUS(2, "Continuous");

    private final int value;
    private final String description;

    private WS_CEL_TOD_MODE(int value, String description) {

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

