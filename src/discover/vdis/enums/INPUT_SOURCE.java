package discover.vdis.enums;

import discover.vdis.EnumInterface;

/**
 * This file is auto-generated (see vdis.EnumGenerator)
 */
public enum INPUT_SOURCE implements EnumInterface {

    INPUT_SRC_OTHER(0, "Other"),
    INPUT_SRC_PILOT(1, "Pilot"),
    INPUT_SRC_COPILOT(2, "Copilot"),
    INPUT_SRC_FIRST_OFFICER(3, "First Officer"),
    INPUT_SRC_DRIVER(4, "Driver"),
    INPUT_SRC_LOADER(5, "Loader"),
    INPUT_SRC_GUNNER(6, "Gunner"),
    INPUT_SRC_COMMANDER(7, "Commander"),
    INPUT_SRC_DIGITAL_DATA_DEVICE(8, "Digital Data Device"),
    INPUT_SRC_INTERCOM(9, "Intercom"),
    INPUT_SRC_AUDIO_JAMMER(10, "Audio Jammer");

    private final int value;
    private final String description;

    private INPUT_SOURCE(int value, String description) {

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

