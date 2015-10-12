package discover.vdis.marking.army;

/**
 * @author Tony Pinkston
 */
public class ArmySection extends AbstractEchelon {

    private final String bumper;

    ArmySection(
        int value,
        String name,
        String description,
        String bumper) {

        super(value, name, description);

        this.bumper = bumper;
    }

    @Override
    public final String getBumper() {

        return bumper;
    }

    public static AbstractEchelon[] getValues() {

        return ArmyTracking.getValues(ArmySection.class);
    }
}
