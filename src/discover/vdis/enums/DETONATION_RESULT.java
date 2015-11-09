package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DETONATION_RESULT implements EnumInterface {

    DET_RESULT_OTHER(0, "Other"),
    DET_RESULT_ENTITY_IMPACT(1, "Entity Impact"),
    DET_RESULT_ENTITY_PROXIMATE(2, "Entity Proximate Detonation"),
    DET_RESULT_GROUND_IMPACT(3, "Ground Impact"),
    DET_RESULT_GROUND_PROXIMATE(4, "Ground Proximate Detonation"),
    DET_RESULT_DETONATION(5, "Detonation"),
    DET_RESULT_NONE(6, "None Or No Detonation"),
    DET_RESULT_HE_HIT_SMALL(7, "He Hit, Small"),
    DET_RESULT_HE_HIT_MEDIUM(8, "He Hit, Medium"),
    DET_RESULT_HE_HIT_LARGE(9, "He Hit, Large"),
    DET_RESULT_ARMOR_PIERCING_HIT(10, "Armor Piercing Hit"),
    DET_RESULT_DIRT_BLAST_SMALL(11, "Dirt Blast, Small"),
    DET_RESULT_DIRT_BLAST_MEDIUM(12, "Dirt Blast, Medium"),
    DET_RESULT_DIRT_BLAST_LARGE(13, "Dirt Blast, Large"),
    DET_RESULT_WATER_BLAST_SMALL(14, "Water Blast, Small"),
    DET_RESULT_WATER_BLAST_MEDIUM(15, "Water Blast, Medium"),
    DET_RESULT_WATER_BLAST_LARGE(16, "Water Blast, Large"),
    DET_RESULT_AIR_HIT(17, "Air Hit"),
    DET_RESULT_BUILDING_HIT_SMALL(18, "Building Hit, Small"),
    DET_RESULT_BUILDING_HIT_MEDIUM(19, "Building Hit, Medium"),
    DET_RESULT_BUILDING_HIT_LARGE(20, "Building Hit, Large"),
    DET_RESULT_MICLIC(21, "Mine Clearing Line Charge"),
    DET_RESULT_ENV_OBJ_IMPACT(22, "Environment Object Impact"),
    DET_RESULT_ENV_OBJ_PROXIMATE(23, "Environment Object Proximate Detonation"),
    DET_RESULT_WATER_IMPACT(24, "Water Impact"),
    DET_RESULT_AIR_BURST(25, "Air Burst"),
    DET_RESULT_KILL_FRAG_TYPE_1(26, "Kill With Fragment Type 1"),
    DET_RESULT_KILL_FRAG_TYPE_2(27, "Kill With Fragment Type 2"),
    DET_RESULT_KILL_FRAG_TYPE_3(28, "Kill With Fragment Type 3"),
    DET_RESULT_KILL_FRAG_TYPE_1_FLY_OUT_FAILURE(29, "Kill With Fragment Type 1 After Fly Out Failure"),
    DET_RESULT_KILL_FRAG_TYPE_2_FLY_OUT_FAILURE(30, "Kill With Fragment Type 2 After Fly Out Failure"),
    DET_RESULT_MISS_FLY_OUT_FAILURE(31, "Miss Due to Fly Out Failure"),
    DET_RESULT_MISS_END_GAME_FAILURE(32, "Miss Due to End Game Failure"),
    DET_RESULT_MISS_FLY_OUT_END_GAME_FAILURE(33, "Miss Due to Fly Out and End Game Failure");

    private final int value;
    private final String description;

    private DETONATION_RESULT(int value, String description) {

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

        return Enumerations.getUnknownValue(value, DETONATION_RESULT.class);
    }
}

