package discover.vdis.enums;

import java.util.List;

/**
 * FROZEN_BEHAVIOR: This class is auto-generated by vdis.EnumGenerator
 */
public final class FROZEN_BEHAVIOR extends Value {

    public static final FROZEN_BEHAVIOR
       RUN_INTERNAL_SIMULATION_CLOCK = new FROZEN_BEHAVIOR(0, "RUN_INTERNAL_SIMULATION_CLOCK", "Run Internal Simulation Clock", true),
       TRANSMIT_PDUS = new FROZEN_BEHAVIOR(1, "TRANSMIT_PDUS", "Transmit PDUs", true),
       UPDATE_MODELS_OF_OTHER_ENTITIES = new FROZEN_BEHAVIOR(2, "UPDATE_MODELS_OF_OTHER_ENTITIES", "Update Models of Other Entities", true),
       CONTINUE_TRANSMIT_PDUS = new FROZEN_BEHAVIOR(3, "CONTINUE_TRANSMIT_PDUS", "Continue Transmit PDU", true),
       CEASE_MODELS_OF_OTHER_ENTITIES = new FROZEN_BEHAVIOR(4, "CEASE_MODELS_OF_OTHER_ENTITIES", "Cease Update Models of Other Entities", true),
       CONTINUE_MODELS_OF_OTHER_ENTITIES = new FROZEN_BEHAVIOR(5, "CONTINUE_MODELS_OF_OTHER_ENTITIES", "Continue Update Models of Other Entities", true);

    private FROZEN_BEHAVIOR(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, FROZEN_BEHAVIOR.class);
    }

    /** @see Value#values(Class) */
    public static List<FROZEN_BEHAVIOR> values() {

        return values(FROZEN_BEHAVIOR.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<FROZEN_BEHAVIOR> values(boolean known) {

        return values(FROZEN_BEHAVIOR.class, known);
    }

    /** @see Value#get(int, Class) */
    public static FROZEN_BEHAVIOR get(int value) {

        return get(value, FROZEN_BEHAVIOR.class);
    }
}

