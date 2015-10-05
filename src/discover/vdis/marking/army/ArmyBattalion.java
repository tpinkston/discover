/**
 * @author Tony Pinkston
 */
package discover.vdis.marking.army;

/**
 * (High Level Unit)
 */
public class ArmyBattalion extends AbstractEchelon {

    private final ArmyDivision division;
    private final ArmyBrigade brigade;
    private final int prv;
    private final int bv;
    private final String bumper;

    ArmyBattalion(
        int value,
        String name,
        String description,
        int prv,
        int bv,
        String bumper,
        ArmyDivision division,
        ArmyBrigade brigade) {

        super(value, name, description);

        this.division = division;
        this.brigade = brigade;
        this.prv = prv;
        this.bv = bv;
        this.bumper = bumper;
    }

    public final ArmyDivision getDivision() {

        return this.division;
    }

    public final ArmyBrigade getBrigade() {

        return this.brigade;
    }

    @Override
    public final int getPRV() {

        return this.prv;
    }

    @Override
    public final int getBV() {

        return this.bv;
    }

    @Override
    public final String getBumper() {

        return this.bumper;
    }

    public static AbstractEchelon[] getValues() {

        return ArmyTracking.getValues(ArmyBattalion.class);
    }
}
