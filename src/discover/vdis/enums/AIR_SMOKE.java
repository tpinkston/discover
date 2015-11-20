package discover.vdis.enums;

import java.util.List;

/**
 * AIR_SMOKE: This class is auto-generated by vdis.EnumGenerator
 */
public final class AIR_SMOKE extends Value {

    public static final AIR_SMOKE
       NONE = new AIR_SMOKE(0, "NONE", "No Damage", true),
       TRAILING = new AIR_SMOKE(1, "TRAILING", "Trailing Smoke", true),
       ENGINE = new AIR_SMOKE(2, "ENGINE", "Emitting Engine Smoke", true),
       ENGINE_TRAILING = new AIR_SMOKE(3, "ENGINE_TRAILING", "Emitting Engine Smoke and Trailing Smoke", true);

    private AIR_SMOKE(int value, String name, String description, boolean known) {

        super(value, name, description, known);

        cache(this, AIR_SMOKE.class);
    }

    /** @see Value#values(Class) */
    public static List<AIR_SMOKE> values() {

        return values(AIR_SMOKE.class);
    }

    /** @see Value#values(Class, boolean) */
    public static List<AIR_SMOKE> values(boolean known) {

        return values(AIR_SMOKE.class, known);
    }

    /** @see Value#get(int, Class) */
    public static AIR_SMOKE get(int value) {

        return get(value, AIR_SMOKE.class);
    }
}

