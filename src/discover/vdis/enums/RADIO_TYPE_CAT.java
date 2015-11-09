package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum RADIO_TYPE_CAT implements EnumInterface {

    RT_CAT_OTHER(0, "Other"),
    RT_CAT_VOICE_TRANS_REC(1, "Voice Transmission/Reception"),
    RT_CAT_DATA_LINK_TRANS_REC(2, "Data Link Transmission/Reception"),
    RT_CAT_VOICE_AND_DATA_LINK_TRANS_REC(3, "Voice and Data Link Transmission/Reception"),
    RT_CAT_ILS_GLIDESLOPE_TRANS(4, "Instrumented Landing System (ILS) Glideslope Transmitter"),
    RT_CAT_ILS_LOCALIZER_TRANS(5, "Instrumented Landing System (ILS) Localizer Transmitter"),
    RT_CAT_ILS_OUTER_MARKER_BEACON(6, "Instrumented Landing System (ILS) Outer Marker Beacon"),
    RT_CAT_ILS_MIDDLE_MARKER_BEACON(7, "Instrumented Landing System (ILS) Middle Marker Beacon"),
    RT_CAT_ILS_INNER_MARKER_BEACON(8, "Instrumented Landing System (ILS) Inner Marker Beacon"),
    RT_CAT_ILS_REC_PLATFORM_RADIO(9, "Instrumented Landing System (ILS) Receiver (Platform Radio)"),
    RT_CAT_TACAN_TRANS_GFE(10, "Tactical Air Navigation (TACAN) Transmitter (Ground Fixed Equipment)"),
    RT_CAT_TACAN_REC_MPE(11, "Tactical Air Navigation (TACAN) Receiver (Moving Platform Equipment)"),
    RT_CAT_TACAN_TRANS_REC_MPE(12, "Tactical Air Navigation (TACAN) Transmitter/Receiver (Moving Platform Equipment)"),
    RT_CAT_VOR_TRANS_GFE(13, "Variable Omni-Ranging (VOR) Transmitter (Ground Fixed Equipment)"),
    RT_CAT_VOR_WITH_DME_TRANS_GFE(14, "Variable Omni-Ranging (VOR) with Distance Measuring Equipment (DME) Transmitter (Ground Fixed Equipment)"),
    RT_CAT_COMBINED_VOR_ILS_REC_MPE(15, "Combined VOR/ILS Receiver (Moving Platform Equipment)"),
    RT_CAT_COMBINED_VOR_TACAN_TRANS(16, "Combined VOR/TACAN (VORTAC) Transmitter"),
    RT_CAT_NDB_TRANS(17, "Non-Directional Beacon (NDB) Transmitter"),
    RT_CAT_NDB_REC(18, "Non-Directional Beacon (NDB) Receiver"),
    RT_CAT_NDB_WITH_DME_TRANS(19, "Non-Directional Beacon (NDB) with Distance Measuring Equipment (DME) Transmitter"),
    RT_CAT_DME(20, "Distance Measuring Equipment (DME)"),
    RT_CAT_LINK_11_TERMINAL(22, "Link 11 Terminal"),
    RT_CAT_LINK_11B_TERMINAL(23, "Link 11B Terminal"),
    RT_CAT_EPLRS_SADL_TERMINAL(24, "EPLRS/SADL Terminal"),
    RT_CAT_IFDL(25, "F-22 Intra-Flight Data Link (IFDL)"),
    RT_CAT_F35_MADL(26, "F-35 Multifunction Advanced Data Link (MADL)"),
    RT_CAT_SINCGARS_TERMINAL(27, "SINCGARS Terminal"),
    RT_CAT_L_BAND_SATCOM_TERMINAL(28, "L-Band SATCOM Terminal"),
    RT_CAT_IBS_IS_TERMINAL(29, "IBS-I/S Terminal"),
    RT_CAT_GPS(30, "GPS");

    private final int value;
    private final String description;

    private RADIO_TYPE_CAT(int value, String description) {

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

        return Enumerations.getUnknownValue(value, RADIO_TYPE_CAT.class);
    }
}

