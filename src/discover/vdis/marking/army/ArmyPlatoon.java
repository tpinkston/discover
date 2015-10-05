/**
 * @author Tony Pinkston
 */
package discover.vdis.marking.army;

public class ArmyPlatoon extends AbstractEchelon {

    private final int pv;
    private final String bumper;

    ArmyPlatoon(
        int value,
        String name,
        String description,
        int pv,
        String bumper) {

        super(value, name, description);

        this.pv = pv;
        this.bumper = bumper;
    }

    @Override
    public final int getPV() {

        return this.pv;
    }

    @Override
    public final String getBumper() {

        return this.bumper;
    }

    public static AbstractEchelon[] getValues() {

        return ArmyTracking.getValues(ArmyPlatoon.class);
    }
}
