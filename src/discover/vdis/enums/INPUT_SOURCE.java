package discover.vdis.enums;

import java.util.List;

/**
 * INPUT_SOURCE: This class is auto-generated by vdis.EnumGenerator
 */
public final class INPUT_SOURCE extends Value {

    public static final INPUT_SOURCE
       INPUT_SRC_OTHER = new INPUT_SOURCE(0, "INPUT_SRC_OTHER", "Other", true),
       INPUT_SRC_PILOT = new INPUT_SOURCE(1, "INPUT_SRC_PILOT", "Pilot", true),
       INPUT_SRC_COPILOT = new INPUT_SOURCE(2, "INPUT_SRC_COPILOT", "Copilot", true),
       INPUT_SRC_FIRST_OFFICER = new INPUT_SOURCE(3, "INPUT_SRC_FIRST_OFFICER", "First Officer", true),
       INPUT_SRC_DRIVER = new INPUT_SOURCE(4, "INPUT_SRC_DRIVER", "Driver", true),
       INPUT_SRC_LOADER = new INPUT_SOURCE(5, "INPUT_SRC_LOADER", "Loader", true),
       INPUT_SRC_GUNNER = new INPUT_SOURCE(6, "INPUT_SRC_GUNNER", "Gunner", true),
       INPUT_SRC_COMMANDER = new INPUT_SOURCE(7, "INPUT_SRC_COMMANDER", "Commander", true),
       INPUT_SRC_DIGITAL_DATA_DEVICE = new INPUT_SOURCE(8, "INPUT_SRC_DIGITAL_DATA_DEVICE", "Digital Data Device", true),
       INPUT_SRC_INTERCOM = new INPUT_SOURCE(9, "INPUT_SRC_INTERCOM", "Intercom", true),
       INPUT_SRC_AUDIO_JAMMER = new INPUT_SOURCE(10, "INPUT_SRC_AUDIO_JAMMER", "Audio Jammer", true);

    private INPUT_SOURCE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, INPUT_SOURCE.class);
    }

    /** @see Value#values(Class) */
    public static List<INPUT_SOURCE> values() {

        return values(INPUT_SOURCE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<INPUT_SOURCE> values(boolean known) {

        return values(INPUT_SOURCE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static INPUT_SOURCE get(int value) {

        return get(value, INPUT_SOURCE.class);
    }
}

