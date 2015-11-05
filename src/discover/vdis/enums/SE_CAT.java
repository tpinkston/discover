package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum SE_CAT implements EnumInterface {

    OTH(0, "Other"),
    MSPEC(1, "Multi-spectral"),
    RFA(2, "RF Active"),
    RFP(3, "RF Passive (intercept and DF)"),
    OPT(4, "Optical (direct viewing with or without optics)"),
    EO(5, "Electro-Optical"),
    SEIS(6, "Seismic"),
    CHEMPD(7, "Chemical, point detector"),
    CHEMS(8, "Chemical, standoff"),
    THERM(9, "Thermal (temperature sensing)"),
    ACUA(10, "Acoustic, Active"),
    ACUP(11, "Acoustic, Passive"),
    CP(12, "Contact/Pressure (physical, hydrostatic, barometric)"),
    EMRAD(13, "Electro-Magnetic Radiation (gamma radiation)"),
    PRAD(14, "Particle Radiation (Neutrons, alpha, beta particles)"),
    MAG(15, "Magnetic"),
    GRAV(16, "Gravitational");

    private final int value;
    private final String description;

    private SE_CAT(int value, String description) {

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

