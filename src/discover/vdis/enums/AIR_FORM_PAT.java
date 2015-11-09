package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum AIR_FORM_PAT implements EnumInterface {

    AIRPLAT_FORM_PAT_OFF(0, "Off"),
    AIRPLAT_FORM_PAT_FORMATION_1(1, "Formation 1"),
    AIRPLAT_FORM_PAT_FORMATION_2(2, "Formation 2"),
    AIRPLAT_FORM_PAT_FORMATION_3(3, "Formation 3"),
    AIRPLAT_FORM_PAT_FORMATION_4(4, "Formation 4"),
    AIRPLAT_FORM_PAT_FORMATION_5(5, "Formation 5");

    private final int value;
    private final String description;

    private AIR_FORM_PAT(int value, String description) {

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

        return Enumerations.getUnknownValue(value, AIR_FORM_PAT.class);
    }
}

