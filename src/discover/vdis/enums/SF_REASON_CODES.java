package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum SF_REASON_CODES implements EnumInterface {

    SF_REASON_OTHER(0, "Other"),
    SF_REASON_RECESS(1, "Recess"),
    SF_REASON_TERMINATION(2, "Termination"),
    SF_REASON_SYSTEM_FAILURE(3, "System Failure"),
    SF_REASON_SECURITY_VIOLATION(4, "Security Violation"),
    SF_REASON_ENTITY_RECONSTITUTION(5, "Entity Reconstitution"),
    SF_REASON_STOP_FOR_RESET(6, "Stop For Reset"),
    SF_REASON_STOP_FOR_RESTART(7, "Stop For Restart"),
    SF_REASON_ABORT_TRAINING_RETURN_TAC_OPS(8, "Abort Training Return to Tactical Operations");

    private final int value;
    private final String description;

    private SF_REASON_CODES(int value, String description) {

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

        return Enumerations.getUnknownValue(value, SF_REASON_CODES.class);
    }
}

