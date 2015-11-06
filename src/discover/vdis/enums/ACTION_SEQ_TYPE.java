package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ACTION_SEQ_TYPE implements EnumInterface {

    ACTN_SEQ_TYPE_NOT_SPECIFIED(0, "Not Specified"),
    ACTN_SEQ_TYPE_STANDING_UP(1, "Standing Up Unspecified"),
    ACTN_SEQ_TYPE_STANDING_UP_HANDS(2, "Standing Up Using Only Hands"),
    ACTN_SEQ_TYPE_STANDING_UP_KNEES(3, "Standing Up Using Only Knees"),
    ACTN_SEQ_TYPE_STANDING_UP_ELBOWS(4, "Standing Up Using Only Elbows"),
    ACTN_SEQ_TYPE_KIP_UP(99, "Kip Up"),
    ACTN_SEQ_TYPE_FALLING_DOWN(100, "Falling Down Unspecified"),
    ACTN_SEQ_TYPE_FALLING_DOWN_FACE_FIRST(101, "Falling Down Face First"),
    ACTN_SEQ_TYPE_FALLING_DOWN_BACKWARD(102, "Falling Down Backward"),
    ACTN_SEQ_TYPE_FALLING_DOWN_LEFT_SIDE(103, "Falling Down Left Side"),
    ACTN_SEQ_TYPE_FALLING_DOWN_RIGHT_SIDE(104, "Falling Down Right Side"),
    ACTN_SEQ_TYPE_FALLING_DOWN_CRUMPLING(105, "Falling Down Crumpling"),
    ACTN_SEQ_TYPE_KNEELING(200, "Kneeling Unspecified"),
    ACTN_SEQ_TYPE_KNEELING_BOTH_OR_REAR_KNEES(201, "Kneeling Both/Rear Knees"),
    ACTN_SEQ_TYPE_KNEELING_RIGHT_KNEE(202, "Kneeling Right Knee"),
    ACTN_SEQ_TYPE_KNEELING_LEFT_KNEE(203, "Kneeling Left Knee"),
    ACTN_SEQ_TYPE_KNEELING_ALL_KNEES(204, "Kneeling All Knees For 4 Legged Lifeform"),
    ACTN_SEQ_TYPE_CROUCHING_BENDING(205, "Crouching Bending"),
    ACTN_SEQ_TYPE_CROUCHING_SQUATTING(206, "Crouching Squatting"),
    ACTN_SEQ_TYPE_WALKING(300, "Walking Unspecified"),
    ACTN_SEQ_TYPE_WALKING_UNHEEDED(301, "Walking Unheeded"),
    ACTN_SEQ_TYPE_WALKING_CAREFUL(302, "Walking Careful"),
    ACTN_SEQ_TYPE_WALKING_TIP_TOE(303, "Walking Tip Toe"),
    ACTN_SEQ_TYPE_WALKING_BACKWARDS(304, "Walking Backwards"),
    ACTN_SEQ_TYPE_WALKING_UPSTAIRS(305, "Walking Upstairs"),
    ACTN_SEQ_TYPE_WALKING_DOWNSTAIRS(306, "Walking Downstairs"),
    ACTN_SEQ_TYPE_WALKING_LIMPING(307, "Walking Limping"),
    ACTN_SEQ_TYPE_WALKING_DRAGGING_INJURED_LEG(308, "Walking Dragging Injured Unspecified Leg"),
    ACTN_SEQ_TYPE_WALKING_DRAGGING_INJURED_RIGHT_LEG(309, "Walking Dragging Injured Right Leg"),
    ACTN_SEQ_TYPE_WALKING_DRAGGING_INJURED_LEFT_LEG(310, "Walking Dragging Injured Left Leg"),
    ACTN_SEQ_TYPE_RUNNING(400, "Running Unspecified"),
    ACTN_SEQ_TYPE_RUNNING_FULL(401, "Running Full"),
    ACTN_SEQ_TYPE_RUNNING_JOGGING(402, "Running Jogging"),
    ACTN_SEQ_TYPE_RUNNING_BACKWARDS(403, "Running Backwards"),
    ACTN_SEQ_TYPE_RUNNING_UPSTAIRS(404, "Running Upstairs"),
    ACTN_SEQ_TYPE_RUNNING_DOWNSTAIRS(405, "Running Downstairs"),
    ACTN_SEQ_TYPE_CRAWLING(500, "Crawling Unspecified"),
    ACTN_SEQ_TYPE_CRAWLING_HANDS_AND_KNEES(501, "Crawling Hands And Knees"),
    ACTN_SEQ_TYPE_CRAWLING_ELBOWS(502, "Crawling Elbows"),
    ACTN_SEQ_TYPE_CRAWLING_HANDS_DRAGGING_FEET(503, "Crawling Hands Dragging Feet"),
    ACTN_SEQ_TYPE_VOMITING(600, "Vomiting Unspecified"),
    ACTN_SEQ_TYPE_VOMITING_HANDS_AND_KNEES(601, "Vomiting Hands And Knees"),
    ACTN_SEQ_TYPE_VOMITING_BENDING_OVER(602, "Vomiting Bending Over"),
    ACTN_SEQ_TYPE_EATING(700, "Eating Unspecified"),
    ACTN_SEQ_TYPE_DRINKING(750, "Drinking Unspecified"),
    ACTN_SEQ_TYPE_SLEEPING(800, "Sleeping Unspecified"),
    ACTN_SEQ_TYPE_BREATHING(900, "Breathing Unspecified"),
    ACTN_SEQ_TYPE_BREATHING_HARD(901, "Breathing Hard"),
    ACTN_SEQ_TYPE_BREATHING_SLOW(902, "Breathing Slow"),
    ACTN_SEQ_TYPE_BREATHING_PANTING(903, "Breathing Panting"),
    ACTN_SEQ_TYPE_URINATING(910, "Urinating"),
    ACTN_SEQ_TYPE_DEFECATING(920, "Defecating"),
    ACTN_SEQ_TYPE_MOUNTING_VEHICLE(1000, "Mounting a Vehicle"),
    ACTN_SEQ_TYPE_DISMOUNTING_VEHICLE(2000, "Dismounting a Vehicle"),
    ACTN_SEQ_TYPE_FIRING_PORTABLE_WEAPON(3000, "Firing/Using a Portable Weapon (including knives)"),
    ACTN_SEQ_TYPE_THROWING_PORTABLE_WEAPON_OBJECT(4000, "Throwing a Portable Weapon Object"),
    ACTN_SEQ_TYPE_USING_CARRIED_EQUIPMENT(5000, "Using Carried Equipment"),
    ACTN_SEQ_TYPE_USING_OTHER_EQUIPMENT(10000, "Using Other Equipment (Not Operating a Vehicle)"),
    ACTN_SEQ_TYPE_OPERATING_VEHICLE(11000, "Operating a Vehicle"),
    ACTN_SEQ_TYPE_RIDING_LIFEFORM(12000, "Riding a Lifeform"),
    ACTN_SEQ_TYPE_MOUNTING_LIFEFORM(13000, "Mounting a Lifeform"),
    ACTN_SEQ_TYPE_DISMOUNTING_LIFEFORM(14000, "Dismounting a Lifeform"),
    ACTN_SEQ_TYPE_RELOADING_PORTABLE_WEAPON(15000, "Reloading a Portable Weapon"),
    ACTN_SEQ_TYPE_RELOADING_NON_PORTABLE_WEAPON(17000, "Reloading a Non Portable Weapon");

    private final int value;
    private final String description;

    private ACTION_SEQ_TYPE(int value, String description) {

        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {

        return value;
    }

    @Override
    public String getDescription() {

        return description;
    }
}

