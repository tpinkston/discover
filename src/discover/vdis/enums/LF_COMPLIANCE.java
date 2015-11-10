package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author tpinkston
 */
public enum LF_COMPLIANCE implements EnumInterface {

    OTHER(0, "Other"),
    DETAINED(1, "Detained"),
    SURRENDER(2, "Surrender"),
    USING_FISTS(3, "Using Fists"),
    VERBAL_ABUSE_LEVEL_1(4, "Verbal Abuse Level 1"),
    VERBAL_ABUSE_LEVEL_2(5, "Verbal Abuse Level 2"),
    VERBAL_ABUSE_LEVEL_3(6, "Verbal Abuse Level 3"),
    PASSIVE_RESISTANCE_LEVEL_1(7, "Passive Resistance Level 1"),
    PASSIVE_RESISTANCE_LEVEL_2(8, "Passive Resistance Level 2"),
    PASSIVE_RESISTANCE_LEVEL_3(9, "Passive Resistance Level 3"),
    USING_NON_LETHAL_WEAPON_1(10, "Using Non-lethal Weapon 1"),
    USING_NON_LETHAL_WEAPON_2(11, "Using Non-lethal Weapon 2"),
    USING_NON_LETHAL_WEAPON_3(12, "Using Non-lethal Weapon 3"),
    USING_NON_LETHAL_WEAPON_4(13, "Using Non-lethal Weapon 4"),
    USING_NON_LETHAL_WEAPON_5(14, "Using Non-lethal Weapon 5"),
    USING_NON_LETHAL_WEAPON_6(15, "Using Non-lethal Weapon 6");

    private final int value;
    private final String description;

    private LF_COMPLIANCE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LF_COMPLIANCE.class);
    }
}

