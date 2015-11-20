package discover.vdis.enums;

import java.util.List;

/**
 * PDU_FAMILY: This class is auto-generated by vdis.EnumGenerator
 */
public final class PDU_FAMILY extends Value {

    public static final PDU_FAMILY
       OTHER = new PDU_FAMILY(0, "OTHER", "Other", true),
       ENTITY_INFORMATION_INTERACTION = new PDU_FAMILY(1, "ENTITY_INFORMATION_INTERACTION", "Entity Information/Interaction", true),
       WARFARE = new PDU_FAMILY(2, "WARFARE", "Warfare", true),
       LOGISTICS = new PDU_FAMILY(3, "LOGISTICS", "Logistics", true),
       RADIO_COMMUNICATION = new PDU_FAMILY(4, "RADIO_COMMUNICATION", "Radio Communication", true),
       SIMULATION_MANAGEMENT = new PDU_FAMILY(5, "SIMULATION_MANAGEMENT", "Simulation Management", true),
       DISTRIBUTED_EMISSION_REGENERATION = new PDU_FAMILY(6, "DISTRIBUTED_EMISSION_REGENERATION", "Distributed Emission Regeneration", true),
       ENTITY_MANAGEMENT = new PDU_FAMILY(7, "ENTITY_MANAGEMENT", "Entity Management", true),
       MINEFIELD = new PDU_FAMILY(8, "MINEFIELD", "Minefield", true),
       SYNTHETIC_ENVIRONMENT = new PDU_FAMILY(9, "SYNTHETIC_ENVIRONMENT", "Synthetic Environment", true),
       SIMULATION_MANAGEMENT_WITH_RELIABILITY = new PDU_FAMILY(10, "SIMULATION_MANAGEMENT_WITH_RELIABILITY", "Simulation Management with Reliability", true),
       LIVE_ENTITY = new PDU_FAMILY(11, "LIVE_ENTITY", "Live Entity", true),
       NON_REAL_TIME = new PDU_FAMILY(12, "NON_REAL_TIME", "Non-Real Time", true),
       INFO_OPS = new PDU_FAMILY(13, "INFO_OPS", "Information Operations", true),
       EXPERIMENTAL_V_DIS = new PDU_FAMILY(130, "EXPERIMENTAL_V_DIS", "Experimental V-DIS", true);

    private PDU_FAMILY(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, PDU_FAMILY.class);
    }

    /** @see Value#values(Class) */
    public static List<PDU_FAMILY> values() {

        return values(PDU_FAMILY.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<PDU_FAMILY> values(boolean known) {

        return values(PDU_FAMILY.class, known);
    }

    /** @see Value#get(int, Class) */
    public static PDU_FAMILY get(int value) {

        return get(value, PDU_FAMILY.class);
    }
}

