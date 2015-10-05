/**
 * @author Tony Pinkston
 */
package discover.vdis.appearance;

import discover.vdis.bits.Abstract32Bits;

public class SpecificObjectAppearance extends Abstract32Bits {

    @Override
    public SpecificObjectAppearance clone() {
        
        SpecificObjectAppearance appearance = new SpecificObjectAppearance();
        
        appearance.set(super.get());
        
        return appearance;
    }
}
