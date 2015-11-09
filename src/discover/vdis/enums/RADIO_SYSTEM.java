package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum RADIO_SYSTEM implements EnumInterface {

    RADIO_OTHER(0, "Other"),
    RADIO_GENERIC(1, "Generic"),
    RADIO_HQ(2, "HQ"),
    RADIO_HQII(3, "HQII"),
    RADIO_HQIIA(4, "HQIIA"),
    RADIO_SINCGARS(5, "SINCGARS"),
    RADIO_CCTT_SINCGARS(6, "CCTT SINCGARS"),
    RADIO_EPLRS(7, "EPLRS (Enhanced Position Location Reporting System)"),
    RADIO_JTIDS_MIDS_(8, "JTIDS/MIDS "),
    RADIO_LINK_11(9, "Link 11"),
    RADIO_LINK_11B(10, "Link 11B"),
    RADIO_L_BAND_SATCOM(11, "L-Band SATCOM"),
    RADIO_ENHANCED_SINCGARS_7_3(12, "Enhanced SINCGARS 7.3"),
    RADIO_NAVIGATION_AID(13, "Navigation Aid");

    private final int value;
    private final String description;

    private RADIO_SYSTEM(int value, String description) {

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

        return Enumerations.getUnknownValue(value, RADIO_SYSTEM.class);
    }
}

