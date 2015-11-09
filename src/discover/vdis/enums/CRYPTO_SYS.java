package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum CRYPTO_SYS implements EnumInterface {

    CRYPTO_OTHER(0, "Other"),
    CRYPTO_KY_28(1, "KY-28"),
    CRYPTO_VINSON(2, "VINSON (KY-57, KY-58, SINCGARS ICOM)"),
    CRYPTO_NSVE(3, "Narrow Spectrum Secure Voice (NSVE)"),
    CRYPTO_WSVE(4, "Wide Spectrum Secure Voice (WSVE)");

    private final int value;
    private final String description;

    private CRYPTO_SYS(int value, String description) {

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

        return Enumerations.getUnknownValue(value, CRYPTO_SYS.class);
    }
}

