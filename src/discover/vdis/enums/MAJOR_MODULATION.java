package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum MAJOR_MODULATION implements EnumInterface {

    MAJ_MOD_OTHER(0, "Other"),
    MAJ_MOD_AMPLITUDE(1, "Amplitude"),
    MAJ_MOD_AMPLITUDE_AND_ANGLE(2, "Amplitude And Angle"),
    MAJ_MOD_ANGLE(3, "Angle"),
    MAJ_MOD_COMBINATION(4, "Combination"),
    MAJ_MOD_PULSE(5, "Pulse"),
    MAJ_MOD_UNMODULATED(6, "Unmodulated"),
    MAJ_MOD_CPSM(7, "Carrier Phase Shift Modulation (CPSM)");

    private final int value;
    private final String description;

    private MAJOR_MODULATION(int value, String description) {

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

        return Enumerations.getUnknownValue(value, MAJOR_MODULATION.class);
    }
}

