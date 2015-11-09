package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DE_BEAM_SHAPE implements EnumInterface {

    DE_FIRE_BEAM_SHAPE_OTHER(0, "Other"),
    DE_FIRE_BEAM_SHAPE_GAUSSIAN(1, "Gaussian"),
    DE_FIRE_BEAM_SHAPE_TOP_HAT(2, "Top Hat");

    private final int value;
    private final String description;

    private DE_BEAM_SHAPE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, DE_BEAM_SHAPE.class);
    }
}

