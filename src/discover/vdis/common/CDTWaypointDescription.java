/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.util.ArrayList;
import java.util.List;

import discover.vdis.bits.Abstract32Bits;
import discover.vdis.bits.Bits;

public class CDTWaypointDescription extends Abstract32Bits {

    private static final List<Bits> values;

    static {

        values = new ArrayList<Bits>();

        values.add(Bits.getOnOff("Right Turn Signal", 0));
        values.add(Bits.getOnOff("Left Turn Signal", 1));
        values.add(Bits.getOnOff("Brake Lights", 2));
        values.add(Bits.getOnOff("Headlights", 3));
        values.add(Bits.getOnOff("Emergency Lights", 4));
        values.add(Bits.getYesNo("Reverse", 7));
    }

    @Override
    public List<Bits> getValues() {

        return values;
    }

    @Override
    public CDTWaypointDescription clone() {

        CDTWaypointDescription description = new CDTWaypointDescription();

        description.set(super.get());

        return description;
    }
}
