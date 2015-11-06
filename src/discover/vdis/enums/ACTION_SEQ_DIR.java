package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ACTION_SEQ_DIR implements EnumInterface {

    ACTN_SEQ_DIR_NOT_SPECIFIED(0, "Not Specified"),
    ACTN_SEQ_DIR_UP(1, "Up"),
    ACTN_SEQ_DIR_DOWN(2, "Down"),
    ACTN_SEQ_DIR_LEFT(3, "Left"),
    ACTN_SEQ_DIR_RIGHT(4, "Right"),
    ACTN_SEQ_DIR_FORWARD(5, "Forward"),
    ACTN_SEQ_DIR_BACKWARD(6, "Backward"),
    ACTN_SEQ_DIR_STATION(7, "Station"),
    ACTN_SEQ_DIR_SPECIFIED_GLOBAL_LOCATION(8, "Specified Global Location"),
    ACTN_SEQ_DIR_SPECIFIED_ORIENTATION_VECTOR(9, "Specified Orientation Vector");

    private final int value;
    private final String description;

    private ACTION_SEQ_DIR(int value, String description) {

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

