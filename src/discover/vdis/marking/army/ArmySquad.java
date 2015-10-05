/**
 * @author Tony Pinkston
 */
package discover.vdis.marking.army;

public class ArmySquad extends AbstractEchelon {

    private final String bumper;
    
    ArmySquad(
        int value, 
        String name, 
        String description, 
        String bumper) {
    
        super(value, name, description);
        
        this.bumper = bumper;
    }
    
    @Override
    public final String getBumper() {
        
        return this.bumper;
    }
    
    public static AbstractEchelon[] getValues() {
        
        return ArmyTracking.getValues(ArmySquad.class);
    }
}
