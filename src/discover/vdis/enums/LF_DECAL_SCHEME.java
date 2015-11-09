package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum LF_DECAL_SCHEME implements EnumInterface {

    LF_DECAL_SCHEME_NONE(0, "None"),
    LF_DECAL_SCHEME_BOSTON_RED_SOX(1, "Boston Red Sox"),
    LF_DECAL_SCHEME_NEW_ENGLAND_PATRIOTS(2, "New England Patriots"),
    LF_DECAL_SCHEME_ORLANDO_MAGIC(3, "Orlando Magic"),
    LF_DECAL_SCHEME_NIKE(4, "Nike"),
    LF_DECAL_SCHEME_DISNEY_WORLD(5, "Disney World"),
    LF_DECAL_SCHEME_POLICE(6, "Police"),
    LF_DECAL_SCHEME_FBI(7, "FBI"),
    LF_DECAL_SCHEME_US_MARSHAL(8, "US Marshal");

    private final int value;
    private final String description;

    private LF_DECAL_SCHEME(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LF_DECAL_SCHEME.class);
    }
}

