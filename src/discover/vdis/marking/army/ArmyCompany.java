/**
 * @author Tony Pinkston
 */
package discover.vdis.marking.army;

public class ArmyCompany extends AbstractEchelon {

    private final int cv;
    private final String bumper;
    
    ArmyCompany(
        int value, 
        String name, 
        String description, 
        int cv, 
        String bumper) {
    
        super(value, name, description);
        
        this.cv = cv;
        this.bumper = bumper;
    }
    
    @Override
    public final int getCV() {
        
        return this.cv;
    }
    
    @Override
    public String getBumper() {
        
        return this.bumper;
    }

    public static AbstractEchelon[] getValues() {
        
        return ArmyTracking.getValues(ArmyCompany.class);
    }
}
