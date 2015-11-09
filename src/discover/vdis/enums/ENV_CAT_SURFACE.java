package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ENV_CAT_SURFACE implements EnumInterface {

    OTH(0, "Other"),
    SEA(1, "Sea State"),
    OIL(2, "Oil Slick"),
    ICE(3, "Ice"),
    DEB(4, "Debris"),
    SURNO(5, "Surf Noise"),
    ISLE(6, "Island");

    private final int value;
    private final String description;

    private ENV_CAT_SURFACE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, ENV_CAT_SURFACE.class);
    }
}

