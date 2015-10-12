package discover.vdis.common;

import discover.vdis.bits.Abstract32Bits;

/**
 * @author Tony Pinkston
 */
public class ExtendedLightsDefault extends Abstract32Bits {

    @Override
    public ExtendedLightsDefault clone() {

        ExtendedLightsDefault lights = new ExtendedLightsDefault();

        lights.set(get());

        return lights;
    }
}
