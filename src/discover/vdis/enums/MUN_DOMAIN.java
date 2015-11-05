package discover.vdis.enums;

public enum MUNITION_DOMAIN {

    OTHER(0, "Other"),
    ANTI_AIR(1, "Anti-Air"),
    ANTI_ARMOR(2, "Anti-Armor"),
    ANTI_GUIDED_WEAPON(3, "Anti-Guided Weapon"),
    ANTI_RADAR(4, "Anti-Radar"),
    ANTI_SATELLITE(5, "Anti-Satellite"),
    ANTI_SHIP(6, "Anti-Ship"),
    ANTI_SUBMARINE(7, "Anti-Submarine"),
    ANTI_PERSONNEL(8, "Anti-Personnel"),
    BATTLEFIELD_SUPPORT(9, "Battlefield Support"),
    STRATEGIC(10, "Strategic"),
    TACTICAL(11, "Tactical"),
    DIRECT_ENERGY_WEAPON(12, "Direct Energy Weapon");

    private final int value;
    private final String description;

    private MUNITION_DOMAIN(int value, String description) {

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

