/**
 * @author Tony Pinkston
 */
package discover.gui.tabs;

import java.awt.GridBagLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.Version;

public abstract class Tab {

    protected static final Logger logger = LoggerFactory.getLogger(Tab.class);

    protected final JPanel panel;
    protected final TabType type;

    protected Tab(String name, TabType type) {

        this.type = type;
        this.panel = new JPanel(new GridBagLayout());
        this.panel.setName(name);
    }

    public abstract void load(Version version, DataInputStream stream) throws IOException;

    public abstract void save(DataOutputStream stream) throws IOException;

    public abstract void close();

    public JPanel getPanel() {

        return this.panel;
    }

    public TabType getTabType() {

        return this.type;
    }

    public String getTabName() {

        return this.panel.getName();
    }

    public void setTabName(String name) {

        this.panel.setName(name);
    }

    public final boolean isPDUPanel() {

        return (this.type == TabType.CAPTURE) || (this.type == TabType.PLAYBACK);
    }
}
