package discover.gui.tabs;

import java.awt.GridBagLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.Version;

/**
 * @author Tony Pinkston
 */
public abstract class Tab {

    protected static final Logger logger = LoggerFactory.getLogger(Tab.class);

    // TODO: Make private
    protected final JPanel panel;
    protected final TabType type;

    protected Tab(String name, TabType tabType) {

        type = tabType;
        panel = new JPanel(new GridBagLayout());
        panel.setName(name);
    }

    public abstract void load(Version version, DataInputStream stream) throws IOException;

    public abstract void save(DataOutputStream stream) throws IOException;

    public abstract void close();

    public JPanel getPanel() {

        return panel;
    }

    public TabType getTabType() {

        return type;
    }

    public String getTabName() {

        return panel.getName();
    }

    public void setTabName(String name) {

        panel.setName(name);
    }

    public final boolean isPDUPanel() {

        return (type == TabType.CAPTURE) || (type == TabType.PLAYBACK);
    }
}
