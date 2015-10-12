package discover.vdis.marking.army;

import java.util.List;

/**
 * @author Tony Pinkston
 */
public class ArmyDivision extends AbstractEchelon {

    ArmyDivision(int value, String name, String description) {

        super(value, name, description);
    }

    @Override
    public String getBumper() {

        return null;
    }

    public ArmyBattalion[] getBattalions() {

        List<ArmyBattalion> values = ArmyTracking.getBattalions(this);

        if (values == null) {

            return null;
        }
        else {

            return values.toArray(new ArmyBattalion[values.size()]);
        }
   }

    public static AbstractEchelon[] getValues() {

        return ArmyTracking.getValues(ArmyDivision.class);
    }
}
