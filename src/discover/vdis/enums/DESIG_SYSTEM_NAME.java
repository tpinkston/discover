package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DESIG_SYSTEM_NAME implements EnumInterface {

    DES_SYS_NAME_NO_STATEMENT(0, "No Statement"),
    DES_SYS_NAME_AN_AAQ_16(1, "AN/AAQ-16: FLIR Imaging System"),
    DES_SYS_NAME_AN_AAQ_22A(2, "AN/AAQ-22A: SAFIRE-LRF (USMC UH1N, MH60G)"),
    DES_SYS_NAME_AN_AAQ_22B(3, "AN/AAQ-22B: SAFIRE-LP (USAF)"),
    DES_SYS_NAME_AN_AAQ_22C(4, "AN/AAQ-22C: Star SAFIRE-I LRF (USMC)"),
    DES_SYS_NAME_AN_AAQ_22D(5, "AN/AAQ-22D: BRITE Star (USMC)"),
    DES_SYS_NAME_AN_AAQ_28_LITENING(6, "AN/AAQ-28: LITENING Airborne Targeting and Navigation POD"),
    DES_SYS_NAME_AN_AAQ_7(7, "AN/AAQ-7 Laser Range Finder (AC-130A/H)"),
    DES_SYS_NAME_AN_AAS_35V_PAVE_PENNY(8, "AN/AAS-35(V): Pave Penny Airborne Ground Attack Pod"),
    DES_SYS_NAME_AN_AAS_37(9, "AN/AAS-37 Laser Range Finder and Designator (OV-10 and OV-1D)"),
    DES_SYS_NAME_AN_AAS_38(10, "AN/AAS-38 NITE HAWK FLIR and Laser Designator Pod (F/A-18)"),
    DES_SYS_NAME_AN_AAS_44(11, "AN/AAS-44 IR Laser Detecting/Ranging/Tracking Set (SH-60B)"),
    DES_SYS_NAME_AN_AAS_49(12, "AN/AAS-49 IR Laser Detecting/Ranging/Tracking Set (derivative of AN/AAS-44)"),
    DES_SYS_NAME_AN_AAS_51(13, "AN/AAS-51 IR Laser Detecting/Ranging/Tracking Set (derivative of AN/AAS-44 used in helicopters)"),
    DES_SYS_NAME_AN_AAS_53(14, "AN/AAS-53 Electro Optical/IR/Laser sensor turret for Army ARH (AN/AAS-52 in MQ-1)"),
    DES_SYS_NAME_AN_GVS_5(15, "AN/GVS-5: Hand Held LRF"),
    DES_SYS_NAME_AN_TVQ_2(16, "AN/TVQ-2: Ground Vehicle Laser Locator Designator (GVLLD)"),
    DES_SYS_NAME_DAK_1_SAGE_GLOSS(17, "DAK-1: Russian LRF Sage Gloss (artillery weapons)"),
    DES_SYS_NAME_DHY_307(18, "DHY 307: Ground Laser Designator"),
    DES_SYS_NAME_SACLOS(19, "Semi Automatic Command Line of Sight (SACLOS) - Wired / Beam Rider / Laser Designator"),
    DES_SYS_NAME_TPDK_1(20, "TPDK-1: Russian LRF"),
    DES_SYS_NAME_1D15(21, "1D15: Russian Laser Target Designator"),
    DES_SYS_NAME_1D20(22, "1D20: Russian Laser Target Designator"),
    DES_SYS_NAME_1D22(23, "1D22: Russian Laser Target Designator"),
    DES_SYS_NAME_IZLID_428P_W(24, "IZLID 428P-W"),
    DES_SYS_NAME_AIM_1_SLX_SUPER_LONG_RANGE_IR_AIMING_LASER(25, "AIM-1/SLX Super Long Range IR Aiming Laser (Helicopter mounted)"),
    DES_SYS_NAME_AIM_1_SLR_SUPER_LONG_RANGE_IR_AIMING_LASER(26, "AIM-1/SLR Super Long Range IR Aiming Laser"),
    DES_SYS_NAME_AN_PEQ_2A_IR_AIMING_LASER(27, "AN/PEQ-2A IR Aiming Laser"),
    DES_SYS_NAME_ACP_2A_AIR_COMMANDER_POINTER_IR_AIMING_LASER(28, "ACP-2A Air Commander Pointer IR Aiming Laser");

    private final int value;
    private final String description;

    private DESIG_SYSTEM_NAME(int value, String description) {

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

