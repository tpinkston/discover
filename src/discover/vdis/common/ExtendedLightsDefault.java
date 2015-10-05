/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import discover.vdis.bits.Abstract32Bits;

public class ExtendedLightsDefault extends Abstract32Bits {

    @Override
    public ExtendedLightsDefault clone() {

        ExtendedLightsDefault lights = new ExtendedLightsDefault();

        lights.set(super.get());

        return lights;
    }
}
