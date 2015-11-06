package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PDU_FAMILY implements EnumInterface {

    PDU_FAMILY_OTHER(0, "Other"),
    PDU_FAMILY_ENTITY_INFORMATION_INTERACTION(1, "Entity Information/Interaction"),
    PDU_FAMILY_WARFARE(2, "Warfare"),
    PDU_FAMILY_LOGISTICS(3, "Logistics"),
    PDU_FAMILY_RADIO_COMMUNICATION(4, "Radio Communication"),
    PDU_FAMILY_SIMULATION_MANAGEMENT(5, "Simulation Management"),
    PDU_FAMILY_DISTRIBUTED_EMISSION_REGENERATION(6, "Distributed Emission Regeneration"),
    PDU_FAMILY_ENTITY_MANAGEMENT(7, "Entity Management"),
    PDU_FAMILY_MINEFIELD(8, "Minefield"),
    PDU_FAMILY_SYNTHETIC_ENVIRONMENT(9, "Synthetic Environment"),
    PDU_FAMILY_SIMULATION_MANAGEMENT_WITH_RELIABILITY(10, "Simulation Management with Reliability"),
    PDU_FAMILY_LIVE_ENTITY(11, "Live Entity"),
    PDU_FAMILY_NON_REAL_TIME(12, "Non-Real Time"),
    PDU_FAMILY_INFO_OPS(13, "Information Operations"),
    PDU_FAMILY_EXPERIMENTAL_V_DIS(130, "Experimental V-DIS");

    private final int value;
    private final String description;

    private PDU_FAMILY(int value, String description) {

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

