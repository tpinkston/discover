/**
 * @author Tony Pinkston
 */
package discover.vdis.vprecords;

import discover.vdis.types.EntityType;

public abstract class ExtendedAppearanceVPR extends AbstractVPRecord {

    protected ExtendedAppearanceVPR(int type) {

        super(type);
    }
    
    /**
     * @param type - {@link AbstractEnum} for "VP_RECORD_TYPE"
     * 
     * @return Appropriate appearance object for the given record type.
     */
    public static ExtendedAppearanceVPR get(int type) {

        ExtendedAppearanceVPR record = null;
        
        switch(type) {
        
            case 30: // VP_RECORD_TYPE_LEGACY_EXT_LIFEFORM_APP
                record = new LegacyExtendedLifeformAppearanceVPR();
                break;
            case 31: // VP_RECORD_TYPE_EXT_CULT_FEAT_APP
                record = new ExtendedCulturalFeatureAppearanceVPR();
                break;
            case 20: // VP_RECORD_TYPE_EXT_PLATFORM_APP
                record = new ExtendedPlatformAppearanceVPR();
                break;
            case 32: // VP_RECORD_TYPE_EXT_SUPPLY_APP
                record = new ExtendedSupplyAppearanceVPR();
                break;
        }

        return record;
    }
    
    /**
     * @param type - {@link EntityType}
     * 
     * @return Appropriate appearance object for the given entity type.
     */
    public static ExtendedAppearanceVPR get(EntityType type) {

        ExtendedAppearanceVPR value = null;
        
        switch(type.septuple.kind) {

            case 1: // PLATFORM
                
                switch(type.septuple.domain) {
                
                    case 1: // LAND 
                        value = new ExtendedPlatformAppearanceVPR();
                        break;
                    
                    case 2: // AIR
                        value = new ExtendedPlatformAppearanceVPR();
                        break;
                }
                
                break;
                
            case 3: // LIFEFORM 
                value = new LegacyExtendedLifeformAppearanceVPR();
                break;
                
            case 5: // CULTURAL_FEATURE
                value = new ExtendedCulturalFeatureAppearanceVPR();
                break;
                
            case 6: // SUPPLY
                value = new ExtendedSupplyAppearanceVPR();
                break;
        }
        
        if (value != null) {
            
            value.setDomain(type.septuple.domain);
        }
        
        return value;
    }
}
