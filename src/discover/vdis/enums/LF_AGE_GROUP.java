package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author Tony Pinkston
 */
public enum LF_AGE_GROUP implements EnumInterface {

    NOT_SPECIFIED(0, "Not Specified"),
    NEWBORN(1, "Newborn (0-3 months)"),
    INFANT(2, "Infant (3 months-1 year)"),
    TODDLER(3, "Toddler (1-3 years)"),
    SMALL_CHILD(4, "Small Child (3-6 years)"),
    CHILD(5, "Child (6-10 years)"),
    ADOLESCENT(6, "Adolescent (10-12 years)"),
    TEENAGER(7, "Teenager (13-16 years)"),
    EARLY_ADULTHOOD(8, "Early Adulthood (17-25 years)"),
    MIDDLE_ADULTHOOD(9, "Middle Adulthood (26-40 years)"),
    LATE_ADULTHOOD(10, "Late Adulthood (41-55 years)"),
    SENIOR(11, "Senior (55-70 years)"),
    ELDERLY(12, "Elderly (71+ years)"),
    CENTURION(13, "Centurion (100+ years)");

    private final int value;
    private final String description;

    private LF_AGE_GROUP(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LF_AGE_GROUP.class);
    }
}

