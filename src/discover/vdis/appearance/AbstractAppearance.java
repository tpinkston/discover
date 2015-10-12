package discover.vdis.appearance;

import discover.vdis.bits.Abstract32Bits;
import discover.vdis.types.EntityType;

/**
 * @author Tony Pinkston
 */
public abstract class AbstractAppearance extends Abstract32Bits {

    public abstract String getName();

    @Override
    public abstract AbstractAppearance clone();

    /**
     * @param type - {@link EntityType}
     *
     * @return Appropriate appearance object for the given entity type.
     */
    public static AbstractAppearance get(EntityType type) {

        AbstractAppearance value = null;

        switch(type.septuple.kind) {


            case 1: // PLATFORM

                switch(type.septuple.domain) {

                    case 1: // LAND
                        value = new PlatformLandAppearance();
                        break;

                    case 2: // AIR
                        value = new PlatformAirAppearance();
                        break;
                }

                break;

            case 3: // LIFEFORM

                value = new LifeformAppearance();
                break;
        }

        if (value == null) {

            value = new DefaultAppearance();
        }

        return value;
    }
}
