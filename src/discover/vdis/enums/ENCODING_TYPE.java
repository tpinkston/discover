package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum ENCODING_TYPE implements EnumInterface {

    ENCODING_TYPE_8_BIT_MU_LAW(1, "8-Bit Mu-Law"),
    ENCODING_TYPE_CVSD_PER_MIL_STD_188_113(2, "CVSD Per MIL-STD-188-113"),
    ENCODING_TYPE_ADPCM_PER_CCITT_G_721(3, "ADPCM Per CCITT G.721"),
    ENCODING_TYPE_16_BIT_LINEAR_PCM(4, "16-Bit Linear PCM"),
    ENCODING_TYPE_8_BIT_LINEAR_PCM(5, "8-Bit Linear PCM"),
    ENCODING_TYPE_VQ_VECTOR_QUANTIZATION(6, "VQ (Vector Quantization)");

    private final int value;
    private final String description;

    private ENCODING_TYPE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, ENCODING_TYPE.class);
    }
}

