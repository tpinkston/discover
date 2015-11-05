package discover.vdis.enums;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PLAT_CAT_AIR implements VdisEnum {

    FAD(1, "Fighter/Air Defense"),
    ATK(2, "Attack/Strike"),
    BOMB(3, "Bomber"),
    CARGO(4, "Cargo/Tanker"),
    ASW(5, "ASW/Patrol/Observation"),
    EW(6, "Electronic Warfare (EW)"),
    RECON(7, "Reconnaissance"),
    SURV(8, "Surveillance/C2 (Airborne Early Warning)"),
    AH(20, "Attack Helicopter"),
    UH(21, "Utility Helicopter"),
    ASUB(22, "Antisubmarine Warfare/Patrol Helicopter"),
    CARH(23, "Cargo Helicopter"),
    OBSH(24, "Observation Helicopter"),
    SOPSH(25, "Special Operations Helicopter"),
    TRAIN(40, "Trainer"),
    UAV(50, "Unmanned"),
    CIV(57, "Non-Combatant Commercial Aircraft");

    private final int value;
    private final String description;

    private PLAT_CAT_AIR(int value, String description) {

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

