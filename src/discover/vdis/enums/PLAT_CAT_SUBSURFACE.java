package discover.vdis.enums;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum PLAT_CAT_SUBSURFACE implements VdisEnum {

    SSBN(1, "SSBN (Nuclear Ballistic Missile)"),
    SSGN(2, "SSGN (Nuclear Guided Missile)"),
    SSN(3, "SSN (Nuclear Attack - Torpedo)"),
    SSG(4, "SSG (Conventional Guided Missile)"),
    SS(5, "SS (Conventional Attack - Torpedo, Patrol)"),
    SSAN(6, "SSAN (Nuclear Auxiliary)"),
    SSA(7, "SSA (Conventional Auxiliary)");

    private final int value;
    private final String description;

    private PLAT_CAT_SUBSURFACE(int value, String description) {

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

