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
@SuppressWarnings("serial")
public abstract class Tab extends JPanel {

    protected static final Logger logger = LoggerFactory.getLogger(Tab.class);

    private final TabType type;

    protected Tab(String name, TabType tabType) {

        super(new GridBagLayout());

        type = tabType;

        setName(name);
    }

    public abstract void load(Version version, DataInputStream stream) throws IOException;

    public abstract void save(DataOutputStream stream) throws IOException;

    public abstract void close();

    public final TabType getType() {

        return type;
    }

    public final boolean isPDUPanel() {

        return (this instanceof PDUTab);
    }
}
