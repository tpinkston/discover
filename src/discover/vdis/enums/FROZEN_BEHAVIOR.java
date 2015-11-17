package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author Tony Pinkston
 */
public enum FROZEN_BEHAVIOR implements EnumInterface {

    RUN_INTERNAL_SIMULATION_CLOCK(0, "Run Internal Simulation Clock"),
    TRANSMIT_PDUS(1, "Transmit PDUs"),
    UPDATE_MODELS_OF_OTHER_ENTITIES(2, "Update Models of Other Entities"),
    CONTINUE_TRANSMIT_PDUS(3, "Continue Transmit PDU"),
    CEASE_MODELS_OF_OTHER_ENTITIES(4, "Cease Update Models of Other Entities"),
    CONTINUE_MODELS_OF_OTHER_ENTITIES(5, "Continue Update Models of Other Entities");

    private final int value;
    private final String description;

    private FROZEN_BEHAVIOR(int value, String description) {

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

        return Enumerations.getUnknownValue(value, FROZEN_BEHAVIOR.class);
    }
}

