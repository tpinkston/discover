package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum VP_RECORD_TYPE implements EnumInterface {

    VP_RECORD_TYPE_ARTICULATED_PART(0, "Articulated Part"),
    VP_RECORD_TYPE_ATTACHED_PART(1, "Attached Part"),
    VP_RECORD_TYPE_SEPARATION(2, "Separation"),
    VP_RECORD_TYPE_ENTITY_TYPE(3, "Entity Type"),
    VP_RECORD_TYPE_ENTITY_ASSOC(4, "Entity Association"),
    VP_RECORD_TYPE_EXT_PLATFORM_APP(20, "Extended Platform Appearance"),
    VP_RECORD_TYPE_EXT_LIFEFORM_APP(21, "Extended Lifeform Appearance"),
    VP_RECORD_TYPE_HIFI_LIGHTS(22, "High Fidelity Lights"),
    VP_RECORD_TYPE_CHEVRON_MARKING(23, "Chevron Marking"),
    VP_RECORD_TYPE_HIFI_THERMAL(24, "High Fidelity Thermal Sensor"),
    VP_RECORD_TYPE_ENTITY_OFFSET(25, "Entity Offset"),
    VP_RECORD_TYPE_DEAD_RECKONING(26, "Dead Reckoning"),
    VP_RECORD_TYPE_ARMY_TASK_ORG(27, "Army Task Organization"),
    VP_RECORD_TYPE_HEAD_GAZING_WPN_AIM(28, "Head Gazing / Weapon Aiming"),
    VP_RECORD_TYPE_LIFEFORM_ACT_SEQ(29, "Lifeform Action Sequence"),
    VP_RECORD_TYPE_LEGACY_EXT_LIFEFORM_APP(30, "Legacy Extended Lifeform Appearance"),
    VP_RECORD_TYPE_EXT_CULT_FEAT_APP(31, "Extended Cultural Feature Appearance"),
    VP_RECORD_TYPE_EXT_SUPPLY_APP(32, "Extended Supply Appearance");

    private final int value;
    private final String description;

    private VP_RECORD_TYPE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, VP_RECORD_TYPE.class);
    }
}

