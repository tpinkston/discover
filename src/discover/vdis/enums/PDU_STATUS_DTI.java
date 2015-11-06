package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PDU_STATUS_DTI implements EnumInterface {

    PDU_STATUS_DTI_MUNITION(0, "Munition"),
    PDU_STATUS_DTI_EXPENDABLE(1, "Expendable"),
    PDU_STATUS_DTI_NON_MUNITION_EXPLOSION(2, "Non-Munition Explosion");

    private final int value;
    private final String description;

    private PDU_STATUS_DTI(int value, String description) {

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

