package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum REST_STATUS implements EnumInterface {

    REST_NOT_RESTED(0, "Not Rested (Has Not Slept In The Last Three Days)"),
    REST_HAS_SLEPT_AN_AVERAGE_OF_1_HOUR(1, "Has Slept An Average Of 1 Hour Per Day In The Last Three Days"),
    REST_HAS_SLEPT_AN_AVERAGE_OF_2_HOURS(2, "Has Slept An Average Of 2 Hours Per Day In The Last Three Days"),
    REST_HAS_SLEPT_AN_AVERAGE_OF_3_HOURS(3, "Has Slept An Average Of 3 Hours Per Day In The Last Three Days"),
    REST_HAS_SLEPT_AN_AVERAGE_OF_4_HOURS(4, "Has Slept An Average Of 4 Hours Per Day In The Last Three Days"),
    REST_HAS_SLEPT_AN_AVERAGE_OF_5_HOURS(5, "Has Slept An Average Of 5 Hours Per Day In The Last Three Days"),
    REST_HAS_SLEPT_AN_AVERAGE_OF_6_HOURS(6, "Has Slept An Average Of 6 Hours Per Day In The Last Three Days"),
    REST_HAS_SLEPT_AN_AVERAGE_OF_7_HOURS(7, "Has Slept An Average Of 7 Hours Per Day In The Last Three Days"),
    REST_FULLY_RESTED(8, "Fully Rested (Has Slept An Average Of 8 Hours Per Day In The Last Three Days)");

    private final int value;
    private final String description;

    private REST_STATUS(int value, String description) {

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

