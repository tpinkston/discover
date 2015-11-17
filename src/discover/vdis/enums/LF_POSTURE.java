package discover.vdis.enums;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * Not auto-generated, updated manually.
 *
 * @author Tony Pinkston
 */
public enum LF_POSTURE implements EnumInterface {

    OTHER(0, "Other"),
    UPRIGHT_STANDING_STILL(1, "Upright Standing Still"),
    UPRIGHT_WALKING(2, "Upright Walking"),
    UPRIGHT_RUNNING(3, "Upright Running"),
    KNEELING(4, "Kneeling"),
    PRONE(5, "Prone"),
    CRAWLING(6, "Crawling"),
    SWIMMING(7, "Swimming"),
    PARACHUTING(8, "Parachuting"),
    JUMPING(9, "Jumping"),
    SITTING(10, "Sitting"),
    SQUATTING(11, "Squatting"),
    CROUCHING(12, "Crouching"),
    WADING(13, "Wading"),
    SURRENDER(14, "Surrender"),
    DETAINED(15, "Detained");

    private final int value;
    private final String description;

    private LF_POSTURE(int value, String description) {

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

        return Enumerations.getUnknownValue(value, LF_POSTURE.class);
    }
}

