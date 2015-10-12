package discover.vdis.appearance;

import discover.vdis.bits.Abstract32Bits;

/**
 * @author Tony Pinkston
 */
public class SpecificObjectAppearance extends Abstract32Bits {

    @Override
    public SpecificObjectAppearance clone() {

        SpecificObjectAppearance appearance = new SpecificObjectAppearance();

        appearance.set(get());

        return appearance;
    }
}
