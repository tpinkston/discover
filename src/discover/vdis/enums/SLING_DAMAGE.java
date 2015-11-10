package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author tpinkston
 */
public enum SLING_DAMAGE implements EnumInterface {

    NONE(0, "None"),
    SLING_DAMAGED(1, "Sling Damaged"),
    LINE_DAMAGED(2, "Line Damaged"),
    SLING_OR_LINE_DAMAGED(3, "Sling or Line Destroyed");

    private final int value;
    private final String description;

    private SLING_DAMAGE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, SLING_DAMAGE.class);
    }
}

