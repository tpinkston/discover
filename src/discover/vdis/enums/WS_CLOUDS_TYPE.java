package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_CLOUDS_TYPE implements EnumInterface {

    WSC_TYPE_NONE(0, "None"),
    WSC_TYPE_ALTOCUMULUS(1, "Altocumulus"),
    WSC_TYPE_ALTOSTRATUS(2, "Altostratus"),
    WSC_TYPE_CIRROCUMULUS(3, "Cirrocumulus"),
    WSC_TYPE_CIRROSTRATUS(4, "Cirrostratus"),
    WSC_TYPE_CIRRUS(5, "Cirrus"),
    WSC_TYPE_CUMULONIMBUS(6, "Cumulonimbus"),
    WSC_TYPE_CUMULUS(7, "Cumulus"),
    WSC_TYPE_NIMBOSTRATUS(8, "Nimbostratus"),
    WSC_TYPE_STRATOCUMULUS(9, "Stratocumulus"),
    WSC_TYPE_STRATUS(10, "Stratus");

    private final int value;
    private final String description;

    private WS_CLOUDS_TYPE(int value, String description) {

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

