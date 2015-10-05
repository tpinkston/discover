/**
 * @author Tony Pinkston
 */
package discover.vdis.marking.army;

public class ArmyBrigade extends AbstractEchelon {

    ArmyBrigade(int value, String name, String description) {

        super(value, name, description);
    }

    public static AbstractEchelon[] getValues() {

        return ArmyTracking.getValues(ArmyBrigade.class);
    }
}
