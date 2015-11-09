package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PHYS_ASSOC_TYPE implements EnumInterface {

    EAVP_PHYS_ASSOC_TYPE_NOT_SPECIFIED(0, "Not Specified"),
    EAVP_PHYS_ASSOC_TYPE_TOWED_IN_AIR(1, "Towed in Air (Single Hook, Not Specified)"),
    EAVP_PHYS_ASSOC_TYPE_TOWED_ON_LAND(2, "Towed on Land"),
    EAVP_PHYS_ASSOC_TYPE_TOWED_ON_WATER_SURFACE(3, "Towed on Water Surface"),
    EAVP_PHYS_ASSOC_TYPE_TOWED_UNDERWATER(4, "Towed Underwater"),
    EAVP_PHYS_ASSOC_TYPE_MOUNTED_ATTACHED(5, "Mounted Attached"),
    EAVP_PHYS_ASSOC_TYPE_MOUNTED_UNATTACH_UNSUPP(6, "Mounted Unattached and Unsupported"),
    EAVP_PHYS_ASSOC_TYPE_MOUNTED_UNATTACH_SUPP(7, "Mounted Unattached and Supported"),
    EAVP_PHYS_ASSOC_TYPE_TOWED_IN_AIR_CENTER_HOOK(8, "Towed in Air (Center Hook)"),
    EAVP_PHYS_ASSOC_TYPE_TOWED_IN_AIR_FWD_HOOK(9, "Towed in Air (Forward Hook)"),
    EAVP_PHYS_ASSOC_TYPE_TOWED_IN_AIR_AFT_HOOK(10, "Towed in Air (Aft Hook)"),
    EAVP_PHYS_ASSOC_TYPE_TOWED_IN_AIR_TANDEM_HOOK(11, "Towed in Air (Tandem Hook - Fore and Aft)"),
    EAVP_PHYS_ASSOC_TYPE_TOWED_IN_AIR_MISMAN_TANDEM_FORE_CENTER(12, "Towed in Air (Mismanaged Tandem - Fore and Center)"),
    EAVP_PHYS_ASSOC_TYPE_TOWED_IN_AIR_MISMAN_TANDEM_CENTER_AFT(13, "Towed in Air (Mismanaged Tandem - Center and Aft)"),
    EAVP_PHYS_ASSOC_TYPE_TOWED_IN_AIR_ALL_HOOKS(14, "Towed in Air (All Hooks)"),
    EAVP_PHYS_ASSOC_TYPE_HOISTED(15, "Hoisted"),
    EAVP_PHYS_ASSOC_TYPE_RESTRAINED_TO_A_LIFEFORM(30, "Restrained to a Lifeform"),
    EAVP_PHYS_ASSOC_TYPE_RESTRAINED_TO_A_PLATFORM(31, "Restrained to a Platform"),
    EAVP_PHYS_ASSOC_TYPE_RESTRAINED_TO_AN_OBJECT(32, "Restrained to an Object"),
    EAVP_PHYS_ASSOC_TYPE_REFUELING_OPERATION(61, "Refueling Operation"),
    EAVP_PHYS_ASSOC_TYPE_SAR_BASKET(62, "Search and Rescue Basket"),
    EAVP_PHYS_ASSOC_TYPE_SAR_RESCUE_COLLAR(63, "Search and Rescue Rescue Collar"),
    EAVP_PHYS_ASSOC_TYPE_ENGAGEMENT(64, "Engagement/Object 2 is Being Engaged"),
    EAVP_PHYS_ASSOC_TYPE_RETURN_TO_BASE(65, "Return To Base/Object 2 is the Destination Object"),
    EAVP_PHYS_ASSOC_TYPE_LINE_BETWEEN_COMM_TOWERS(90, "Line between Communication Towers"),
    EAVP_PHYS_ASSOC_TYPE_LINE_BETWEEN_POWER_TOWERS(91, "Line Between Power Towers"),
    EAVP_PHYS_ASSOC_TYPE_INDOORS(92, "Indoors"),
    EAVP_PHYS_ASSOC_TYPE_ROOF(93, "Roof");

    private final int value;
    private final String description;

    private PHYS_ASSOC_TYPE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, PHYS_ASSOC_TYPE.class);
    }
}

