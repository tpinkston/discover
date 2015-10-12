package discover.vdis.appearance;

/**
 * @author Tony Pinkston
 */
public class DefaultAppearance extends AbstractAppearance {

    @Override
    public String getName() {

        return "Default Appearance";
    }

    @Override
    public DefaultAppearance clone() {

        DefaultAppearance appearance = new DefaultAppearance();

        appearance.set(get());

        return appearance;
    }
}
