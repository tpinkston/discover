package discover.vdis.enums;

import discover.vdis.EnumInterface;

public enum APP_CTRL_APPLICATION_TYPE implements EnumInterface {

    OTHER(0, "Other"),
    RESOURCE_MANAGER(1, "Resource Manager"),
    SIMULATION_MANAGER(2, "Simulation Manager"),
    GATEWAY(3, "Gateway"),
    STEALTH(4, "Stealth"),
    TACTICAL_INTEGERNET_INTERFACE(5, "Tactical Integernet Interface");

    private final int value;
    private final String description;

    private APP_CTRL_APPLICATION_TYPE(int value, String description) {

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
}

