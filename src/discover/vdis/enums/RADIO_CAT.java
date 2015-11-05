package discover.vdis.enums;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum RADIO_CAT {

    VTR(1, "Voice Transmission/Reception"),
    DLTR(2, "Data Link Transmission/Reception"),
    VDLTR(3, "Voice and Data Link Transmission/Reception"),
    ILSGT(4, "Instrumented Landing System (ILS) Glideslope Transmitter"),
    ILSLT(5, "Instrumented Landing System (ILS) Localizer Transmitter"),
    ILSOMB(6, "Instrumented Landing System (ILS) Outer Marker Beacon"),
    ILSMMB(7, "Instrumented Landing System (ILS) Middle Marker Beacon"),
    ILSIMB(8, "Instrumented Landing System (ILS) Inner Marker Beacon"),
    ILSR(9, "Instrumented Landing System (ILS) Receiver (Platform Radio)"),
    TACANT(10, "Tactical Air Navigation (TACAN) Transmitter (Ground Fixed Equipment)"),
    TACANR(11, "Tactical Air Navigation (TACAN) Receiver (Moving Platform Equipment)"),
    TACANTR(12, "Tactical Air Navigation (TACAN) Transmitter/Receiver (Moving Platform Equipment)"),
    VORT(13, "Variable Omni-Ranging (VOR) Transmitter (Ground Fixed Equipment)"),
    VORDME(14, "Variable Omni-Ranging (VOR) with Distance Measuring Equipment (DME) Transmitter (Ground Fixed Equipment)"),
    VORR(15, "Combined VOR/ILS Receiver (Moving Platform Equipment)"),
    VORTAC(16, "Combined VOR & TACAN (VORTAC) Transmitter"),
    NDBT(17, "Non-Directional Beacon (NDB) Transmitter"),
    NDBR(18, "Non-Directional Beacon (NDB) Receiver"),
    NDBDME(19, "Non-Directional Beacon (NDB) with Distance Measuring Equipment (DME) Transmitter"),
    DME(20, "Distance Measuring Equipment (DME)"),
    L16(21, "Link 16 Terminal"),
    L11(22, "Link 11 Terminal"),
    L11B(23, "Link 11B Terminal"),
    EPLRSSADL(24, "EPLRS/SADL Terminal"),
    IFDL(25, "F-22 Intra-Flight Data Link (IFDL)"),
    MADL(26, "F-35 Multifunction Advanced Data Link (MADL)"),
    SINCGARS(27, "SINCGARS Terminal"),
    LBANDSAT(28, "L-Band SATCOM Terminal"),
    IBSIS(29, "IBS-I/S Terminal"),
    GPS(30, "GPS");

    private final int value;
    private final String description;

    private RADIO_CAT(int value, String description) {

        this.value = value;
        this.description = description;
    }

    public int getValue() {

        return value;
    }

    public String getDescription() {

        return description;
    }
}

