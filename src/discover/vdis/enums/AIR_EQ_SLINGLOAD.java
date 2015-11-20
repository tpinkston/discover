package discover.vdis.enums;

import java.util.List;

/**
 * AIR_EQ_SLINGLOAD: This class is auto-generated by vdis.EnumGenerator
 */
public final class AIR_EQ_SLINGLOAD extends Value {

    public static final AIR_EQ_SLINGLOAD
       AIRPLAT_EQ_SLNGLD_NOT_PRESENT = new AIR_EQ_SLINGLOAD(0, "AIRPLAT_EQ_SLNGLD_NOT_PRESENT", "Not Present", true),
       AIRPLAT_EQ_SLNGLD_EMPTY = new AIR_EQ_SLINGLOAD(1, "AIRPLAT_EQ_SLNGLD_EMPTY", "Empty", true),
       AIRPLAT_EQ_SLNGLD_LOADED = new AIR_EQ_SLINGLOAD(2, "AIRPLAT_EQ_SLNGLD_LOADED", "Loaded", true),
       AIRPLAT_EQ_SLNGLD_DAMAGED = new AIR_EQ_SLINGLOAD(3, "AIRPLAT_EQ_SLNGLD_DAMAGED", "Damaged", true);

    private AIR_EQ_SLINGLOAD(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, AIR_EQ_SLINGLOAD.class);
    }

    /** @see Value#values(Class) */
    public static List<AIR_EQ_SLINGLOAD> values() {

        return values(AIR_EQ_SLINGLOAD.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<AIR_EQ_SLINGLOAD> values(boolean known) {

        return values(AIR_EQ_SLINGLOAD.class, known);
    }

    /** @see Value#get(int, Class) */
    public static AIR_EQ_SLINGLOAD get(int value) {

        return get(value, AIR_EQ_SLINGLOAD.class);
    }
}

