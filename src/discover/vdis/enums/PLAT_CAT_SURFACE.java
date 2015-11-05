package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PLAT_CAT_SURFACE implements EnumInterface {

    OTH(0, "Other"),
    CAR(1, "Carrier"),
    COM(2, "Command Ship/Cruiser"),
    GMC(3, "Guided Missile Cruiser"),
    DDG(4, "Guided Missile Destroyer (DDG)"),
    DD(5, "Destroyer (DD)"),
    FFG(6, "Guided Missile Frigate (FFG)"),
    LPC(7, "Light/Patrol Craft"),
    MCS(8, "Mine Countermeasure Ship/Craft"),
    DLS(9, "Dock Landing Ship"),
    TLS(10, "Tank Landing Ship"),
    LAND(11, "Landing Craft"),
    LCAR(12, "Light Carrier"),
    CHCAR(13, "Cruiser/Helicopter Carrier"),
    HYD(14, "Hydrofoil"),
    ACSEF(15, "Air Cushion/Surface Effect"),
    AUX(16, "Auxiliary"),
    AUXMM(17, "Auxiliary, Merchant Marine"),
    UTIL(18, "Utility"),
    FRG(50, "Frigate (including Corvette)"),
    BAT(51, "Battleship"),
    HC(52, "Heavy Cruiser"),
    DT(53, "Destroyer Tender"),
    AAS(54, "Amphibious Assault Ship"),
    ACS(55, "Amphibious Cargo Ship"),
    ATD(56, "Amphibious Transport Dock"),
    AMMO(57, "Ammunition Ship"),
    CSS(58, "Combat Stores Ship"),
    SURTASS(59, "Surveillance Towed Array Sonar System (SURTASS)"),
    FCSS(60, "Fast Combat Support Ship"),
    CIV(61, "Non-Combatant Ship"),
    CGC(62, "Coast Guard Cutters"),
    CGB(63, "Coast Guard Boats");

    private final int value;
    private final String description;

    private PLAT_CAT_SURFACE(int value, String description) {

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

