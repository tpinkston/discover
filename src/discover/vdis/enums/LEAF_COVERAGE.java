package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum LEAF_COVERAGE implements EnumInterface {

    LEAF_COVERAGE_NORMAL(0, "Normal"),
    LEAF_COVERAGE_BARE(1, "Bare");

    private final int value;
    private final String description;

    private LEAF_COVERAGE(int value, String description) {

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

