package discover.vdis.marking.army;

/**
 * (High Level Unit)
 *
 * @author Tony Pinkston
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

        return division;
    }

    public final ArmyBrigade getBrigade() {

        return brigade;
    }

    @Override
    public final int getPRV() {

        return prv;
    }

    @Override
    public final int getBV() {

        return bv;
    }

    @Override
    public final String getBumper() {

        return bumper;
    }

    public static AbstractEchelon[] getValues() {

        return ArmyTracking.getValues(ArmyBattalion.class);
    }
}
