package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum DEAD_RECKONING implements EnumInterface {

    DEAD_RECK_OTHER(0, "Other"),
    DEAD_RECK_STATIC(1, "Static (Entity does not move)"),
    DEAD_RECK_DRM_F_P_W(2, "DRM(F, P, W)"),
    DEAD_RECK_DRM_R_P_W(3, "DRM(R, P, W)"),
    DEAD_RECK_DRM_R_V_W(4, "DRM(R, V, W)"),
    DEAD_RECK_DRM_F_V_W(5, "DRM(F, V, W)"),
    DEAD_RECK_DRM_F_P_B(6, "DRM(F, P, B)"),
    DEAD_RECK_DRM_R_P_B(7, "DRM(R, P, B)"),
    DEAD_RECK_DRM_R_V_B(8, "DRM(R, V, B)"),
    DEAD_RECK_DRM_F_V_B(9, "DRM(F, V, B)");

    private final int value;
    private final String description;

    private DEAD_RECKONING(int value, String description) {

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

