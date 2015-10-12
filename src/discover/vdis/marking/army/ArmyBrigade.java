package discover.vdis.marking.army;

/**
 * @author Tony Pinkston
 */
public class ArmyBrigade extends AbstractEchelon {

    ArmyBrigade(int value, String name, String description) {

        super(value, name, description);
    }

    public static AbstractEchelon[] getValues() {

        return ArmyTracking.getValues(ArmyBrigade.class);
    }
}
