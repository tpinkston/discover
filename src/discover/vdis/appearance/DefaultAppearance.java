/**
 * @author Tony Pinkston
 */
package discover.vdis.appearance;

public class DefaultAppearance extends AbstractAppearance {

    @Override
    public String getName() {

        return "Default Appearance";
    }

    @Override
    public DefaultAppearance clone() {

        DefaultAppearance appearance = new DefaultAppearance();

        appearance.set(super.get());

        return appearance;
    }
}
