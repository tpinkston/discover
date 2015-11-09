package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum HOOK_TYPE implements EnumInterface {

    HOOK_TYPE_NOT_SPECIFIED(0, "Not Specified"),
    HOOK_TYPE_SINGLE(1, "Single"),
    HOOK_TYPE_FORWARD(2, "Forward"),
    HOOK_TYPE_CENTER(3, "Center"),
    HOOK_TYPE_AFT(4, "Aft"),
    HOOK_TYPE_TANDEM(5, "Tandem (Fore/Aft)"),
    HOOK_TYPE_TANDEM_MISMANAGED_FORE_CENTER(6, "Mismanaged Tandem (Fore/Center)"),
    HOOK_TYPE_TANDEM_MISMANAGED_CENTER_AFT(7, "Mismanaged Tandem (Center/Aft)"),
    HOOK_TYPE_ALL(8, "All");

    private final int value;
    private final String description;

    private HOOK_TYPE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, HOOK_TYPE.class);
    }
}

