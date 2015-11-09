package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum WS_CEL_TOD_SCENE implements EnumInterface {

    WSC_TODSCENE_DAWN(0, "Dawn"),
    WSC_TODSCENE_DAY(1, "Day"),
    WSC_TODSCENE_DUSK(2, "Dusk"),
    WSC_TODSCENE_NIGHT(3, "Night");

    private final int value;
    private final String description;

    private WS_CEL_TOD_SCENE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, WS_CEL_TOD_SCENE.class);
    }
}

