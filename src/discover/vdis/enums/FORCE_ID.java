package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum FORCE_ID implements EnumInterface {

    FORCE_ID_OTHER(0, "Other"),
    FORCE_ID_FRIENDLY(1, "Friendly"),
    FORCE_ID_OPPOSING(2, "Opposing"),
    FORCE_ID_NEUTRAL(3, "Neutral"),
    FORCE_ID_FRIENDLY_2(4, "Friendly 2"),
    FORCE_ID_OPPOSING_2(5, "Opposing 2"),
    FORCE_ID_NEUTRAL_2(6, "Neutral 2"),
    FORCE_ID_FRIENDLY_3(7, "Friendly 3"),
    FORCE_ID_OPPOSING_3(8, "Opposing 3"),
    FORCE_ID_NEUTRAL_3(9, "Neutral 3"),
    FORCE_ID_FRIENDLY_4(10, "Friendly 4"),
    FORCE_ID_OPPOSING_4(11, "Opposing 4"),
    FORCE_ID_NEUTRAL_4(12, "Neutral 4"),
    FORCE_ID_FRIENDLY_5(13, "Friendly 5"),
    FORCE_ID_OPPOSING_5(14, "Opposing 5"),
    FORCE_ID_NEUTRAL_5(15, "Neutral 5"),
    FORCE_ID_FRIENDLY_6(16, "Friendly 6"),
    FORCE_ID_OPPOSING_6(17, "Opposing 6"),
    FORCE_ID_NEUTRAL_6(18, "Neutral 6"),
    FORCE_ID_FRIENDLY_7(19, "Friendly 7"),
    FORCE_ID_OPPOSING_7(20, "Opposing 7"),
    FORCE_ID_NEUTRAL_7(21, "Neutral 7"),
    FORCE_ID_FRIENDLY_8(22, "Friendly 8"),
    FORCE_ID_OPPOSING_8(23, "Opposing 8"),
    FORCE_ID_NEUTRAL_8(24, "Neutral 8"),
    FORCE_ID_FRIENDLY_9(25, "Friendly 9"),
    FORCE_ID_OPPOSING_9(26, "Opposing 9"),
    FORCE_ID_NEUTRAL_9(27, "Neutral 9"),
    FORCE_ID_FRIENDLY_10(28, "Friendly 10"),
    FORCE_ID_OPPOSING_10(29, "Opposing 10"),
    FORCE_ID_NEUTRAL_10(30, "Neutral 10");

    private final int value;
    private final String description;

    private FORCE_ID(int value, String description) {

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

        return Enumerations.getUnknownValue(value, FORCE_ID.class);
    }
}

