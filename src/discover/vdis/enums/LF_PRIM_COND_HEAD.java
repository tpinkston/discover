package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum LF_PRIM_COND_HEAD implements EnumInterface {

    LIFE_PRIM_COND_HEAD_NOT_SPECIFIED(0, "Not Specified"),
    LIFE_PRIM_COND_HEAD_BALD(1, "Bald"),
    LIFE_PRIM_COND_HEAD_SHORT(2, "Short"),
    LIFE_PRIM_COND_HEAD_LONG(3, "Long");

    private final int value;
    private final String description;

    private LF_PRIM_COND_HEAD(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LF_PRIM_COND_HEAD.class);
    }
}

