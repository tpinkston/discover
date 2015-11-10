package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author tpinkston
 */
public enum LF_WEAPON_STATE implements EnumInterface {

    NONE(0, "None"),
    STOWED(1, "Stowed"),
    DEPLOYED(2, "Deployed"),
    FIRING_POSITION(3, "Firing Position");

    private final int value;
    private final String description;

    private LF_WEAPON_STATE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LF_WEAPON_STATE.class);
    }
}

