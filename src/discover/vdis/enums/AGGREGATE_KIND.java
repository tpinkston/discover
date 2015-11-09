package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum AGGREGATE_KIND implements EnumInterface {

    AGGREGATE_KIND_OTHER(0, "Other"),
    AGGREGATE_KIND_MILITARY_HIERARCHY(1, "Military Hierarchy"),
    AGGREGATE_KIND_COMMON_TYPE(2, "Common Type"),
    AGGREGATE_KIND_COMMON_MISSION(3, "Common Mission"),
    AGGREGATE_KIND_SIMILAR_CAPABILITIES(4, "Similar Capabilities"),
    AGGREGATE_KIND_COMMON_LOCATION(5, "Common Location");

    private final int value;
    private final String description;

    private AGGREGATE_KIND(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getName() {

        return name();
    }

    @Override
    public String getDescription() {

        return description;
    }

    public static EnumInterface getValue(int value) {

        for(EnumInterface element : values()) {

            if (element.getValue() == value) {

                return element;
            }
        }

        return Enumerations.getUnknownValue(value, AGGREGATE_KIND.class);
    }
}

