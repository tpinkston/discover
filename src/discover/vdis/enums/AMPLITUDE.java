package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum AMPLITUDE implements EnumInterface {

    AMPLITUDE_OTHER(0, "Other"),
    AMPLITUDE_AFSK(1, "AFSK (Audio Frequency Shift Keying)"),
    AMPLITUDE_AM(2, "AM (Amplitude Modulation)"),
    AMPLITUDE_CW(3, "CW (Continuous Wave Modulation)"),
    AMPLITUDE_DSB(4, "DSB (Double Sideband)"),
    AMPLITUDE_ISB(5, "ISB (Independent Sideband)"),
    AMPLITUDE_LSB(6, "LSB (Single Band Suppressed Carrier, Lower Sideband Mode)"),
    AMPLITUDE_SSB_FULL(7, "SSB-Full (Single Sideband Full Carrier)"),
    AMPLITUDE_SSB_REDUC(8, "SSB-Reduc (Single Band Reduced Carrier)"),
    AMPLITUDE_USB(9, "USB (Single Band Suppressed Carrier, Upper Sideband Mode)"),
    AMPLITUDE_VSB(10, "VSB (Vestigial Sideband)");

    private final int value;
    private final String description;

    private AMPLITUDE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, AMPLITUDE.class);
    }
}

