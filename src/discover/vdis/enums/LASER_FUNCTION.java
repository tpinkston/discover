package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum LASER_FUNCTION implements EnumInterface {

    LASER_FUNCTION_DESIGNATING(0, "Designating"),
    LASER_FUNCTION_RANGING(1, "Ranging"),
    LASER_FUNCTION_IR_DESIGNATING(2, "IR Designating"),
    LASER_FUNCTION_BEAM_RIDING(3, "Beam Riding"),
    LASER_FUNCTION_LASER_POINTING(4, "Laser Pointing"),
    LASER_FUNCTION_IR_MARKING(5, "IR Marking"),
    LASER_FUNCTION_NVG_MARKING(6, "NVG Marking");

    private final int value;
    private final String description;

    private LASER_FUNCTION(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LASER_FUNCTION.class);
    }
}

