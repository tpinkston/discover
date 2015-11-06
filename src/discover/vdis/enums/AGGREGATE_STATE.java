package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum AGGREGATE_STATE implements EnumInterface {

    AGGREGATE_STATE_OTHER(0, "Other"),
    AGGREGATE_STATE_AGGREGATED(1, "Aggregated"),
    AGGREGATE_STATE_DISAGGREGATED(2, "Disaggregated"),
    AGGREGATE_STATE_FULLY_DISAGGREGATED(3, "Fully Disaggregated"),
    AGGREGATE_STATE_PSEUDO_DISAGGREGATED(4, "Pseudo-Disaggregated"),
    AGGREGATE_STATE_PARTIALLY_DISAGGREGATED(5, "Partially-Disaggregated");

    private final int value;
    private final String description;

    private AGGREGATE_STATE(int value, String description) {

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

