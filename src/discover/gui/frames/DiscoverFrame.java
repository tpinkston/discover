package discover.gui.frames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import discover.Discover;
import discover.common.Version;
import discover.gui.Utilities;
import discover.gui.dialogs.AddTabDialog;
import discover.gui.dialogs.MulticastAddressesDialog;
import discover.gui.dialogs.ReorderTabsDialog;
import discover.gui.tabs.BuilderTab;
import discover.gui.tabs.CFSTab;
import discover.gui.tabs.CaptureTab;
import discover.gui.tabs.ClipboardTab;
import discover.gui.tabs.EntityTab;
import discover.gui.tabs.PDUTab;
import discover.gui.tabs.PlaybackTab;
import discover.gui.tabs.Tab;
import discover.gui.tabs.TabType;
import discover.pcap.PCAP;
import discover.system.Network;
import discover.vdis.PDU;

/**
 * @author Tony Pinkston
 */
@SuppressWarnings("serial")
public class DiscoverFrame implements ActionListener, ChangeListener, MouseListener {

    private static DiscoverFrame instance = null;

    private static final Logger logger = Discover.getLogger();

    private static final String SEPARATOR = "-";

    private final JFrame frame = new JFrame() {

        @Override
        public void dispose() {

            if (ArmyTrackingFrame.getFrame() != null) {

                ArmyTrackingFrame.getFrame().dispose();
            }

            if (ConversionFrame.getFrame() != null) {

                ConversionFrame.getFrame().dispose();
            }

            if (EntityTypesFrame.getFrame() != null) {

                EntityTypesFrame.getFrame().dispose();
            }

            if (ObjectTypesFrame.getFrame() != null) {

                ObjectTypesFrame.getFrame().dispose();
            }

            if (BinaryEditorFrame.getFrame() != null) {

                BinaryEditorFrame.getFrame().dispose();
            }

            if (BulkEditorFrame.getFrame() != null) {

                BulkEditorFrame.getFrame().dispose();
            }

            closeTabs();

            super.dispose();
        }
    };

    private final JMenuBar menus = new JMenuBar();
    private final JToolBar tools = new JToolBar();
    private final JTabbedPane pane = new JTabbedPane();
    private final JPopupMenu popup = new JPopupMenu();
    private final AbstractAction loadAction = new LoadAction();
    private final AbstractAction saveAction = new SaveAction();

    private final AbstractAction stalkerAction = new StalkerAction();
    private final AbstractAction textAction = new TextAction();
    private final AbstractAction deleteAllAction = new DeleteAllAction();
    private final AbstractAction deleteAction = new DeleteAction();
    private final AbstractAction cutAction = new CutAction();
    private final AbstractAction copyAction = new CopyAction();
    private final AbstractAction pasteAction = new PasteAction();
    private final AbstractAction addTabAction = new AddTabAction();
    private final AbstractAction removeTabAction = new RemoveTabAction();
    private final AbstractAction renameTabAction = new RenameTabAction();
    private final AbstractAction reordersTabAction = new ReorderTabsAction();

    private final ArrayList<PDU> clipboard;
    private final TreeMap<String, Tab> tabs;
    private final TreeMap<TabType, Integer> counters;

    public DiscoverFrame() {

        this.clipboard = new ArrayList<PDU>();
        this.tabs = new TreeMap<String, Tab>();
        this.counters = new TreeMap<TabType, Integer>();

        this.createMenus();
        this.createTools();

        this.saveAction.setEnabled(false);
        this.removeTabAction.setEnabled(false);
        this.renameTabAction.setEnabled(false);
        this.reordersTabAction.setEnabled(false);
        this.deleteAllAction.setEnabled(false);
        this.deleteAction.setEnabled(false);
        this.cutAction.setEnabled(false);
        this.copyAction.setEnabled(false);
        this.pasteAction.setEnabled(false);

        this.pane.addChangeListener(this);
        this.pane.addMouseListener(this);

        this.popup.add(this.addTabAction);
        this.popup.add(this.removeTabAction);
        this.popup.addSeparator();
        this.popup.add(this.stalkerAction);
        this.popup.add(this.textAction);

        this.frame.setJMenuBar(this.menus);
        this.frame.add(this.tools, BorderLayout.NORTH);
        this.frame.add(this.pane, BorderLayout.CENTER);
        this.frame.setTitle("Discover (" + Network.getHostname() + ")");
        this.frame.setMinimumSize(new Dimension(700, 550));
        this.frame.setPreferredSize(new Dimension(1200, 700));
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    public AbstractAction getDeleteAllAction() {

        return this.deleteAllAction;
    }

    public AbstractAction getDeleteAction() {

        return this.deleteAction;
    }

    public AbstractAction getCutAction() {

        return this.cutAction;
    }

    public AbstractAction getCopyAction() {

        return this.copyAction;
    }

    public AbstractAction getPasteAction() {

        return this.pasteAction;
    }

    /**
     * @param event - {@link ActionEvent}
     */
    @Override
    public void actionPerformed(ActionEvent event) {

    }

    /**
     * @param event - {@link ChangeEvent}
     */
    @Override
    public void stateChanged(ChangeEvent event) {

        Tab tab = this.getSelectedTab();
        int tabs = this.pane.getComponentCount();

        this.saveAction.setEnabled(tabs > 0);
        this.removeTabAction.setEnabled(tabs > 0);
        this.renameTabAction.setEnabled(tabs > 0);
        this.reordersTabAction.setEnabled(tabs > 1);

        if (tabs == 0) {

            this.saveAction.setEnabled(false);
            this.removeTabAction.setEnabled(false);
            this.deleteAllAction.setEnabled(false);
            this.deleteAction.setEnabled(false);
            this.cutAction.setEnabled(false);
            this.copyAction.setEnabled(false);
            this.pasteAction.setEnabled(false);
        }
        else {

            this.saveAction.setEnabled(true);
            this.removeTabAction.setEnabled(true);

            if (tab instanceof CaptureTab) {

                this.deleteAllAction.setEnabled(true);
                this.deleteAction.setEnabled(false);
                this.cutAction.setEnabled(false);
                this.copyAction.setEnabled(true);
                this.pasteAction.setEnabled(false);
            }
            else if (tab instanceof PlaybackTab) {

                this.deleteAllAction.setEnabled(true);
                this.deleteAction.setEnabled(true);
                this.cutAction.setEnabled(true);
                this.copyAction.setEnabled(true);
                this.pasteAction.setEnabled(true);
            }
            else if (tab instanceof BuilderTab) {

                this.deleteAllAction.setEnabled(false);
                this.deleteAction.setEnabled(false);
                this.cutAction.setEnabled(false);
                this.copyAction.setEnabled(true);
                this.pasteAction.setEnabled(true);
            }
        }
    }

    /**
     * @param event - {@link MouseEvent}
     */
    @Override
    public void mousePressed(MouseEvent event) {

        this.showPopup(event);
    }

    /**
     * @param event - {@link MouseEvent}
     */
    @Override
    public void mouseReleased(MouseEvent event) {

        this.showPopup(event);
    }

    /**
     * @param event - {@link MouseEvent}
     */
    @Override
    public void mouseClicked(MouseEvent event) {

        this.showPopup(event);
    }

    /**
     * @param event - {@link MouseEvent}
     */
    @Override
    public void mouseEntered(MouseEvent event) {

    }

    /**
     * @param event - {@link MouseEvent}
     */
    @Override
    public void mouseExited(MouseEvent event) {

    }

    /**
     * @return {@link DiscoverFrame}
     */
    public static DiscoverFrame getInstance() {

        if (instance == null) {

            instance = new DiscoverFrame();

            // Put here instead of constructor to prevent infinite loop
            // (CaptureTab constructor calls getInstance()).
            instance.addTab(new CaptureTab("Capture-0"));
        }

        return instance;
    }

    /**
     * @return {@link JFrame}
     */
    public static JFrame getFrame() {

        if (instance != null) {

            return instance.frame;
        }
        else {

            return null;
        }
    }

    public File getSavedDataPath() {

        String property = System.getProperty("user.dir");
        File defaultPath = new File(property);
        File savedPath = new File(property + "/saved");

        if (savedPath.exists() &&
            savedPath.isDirectory() &&
            savedPath.canRead() &&
            savedPath.canWrite()) {

            defaultPath = savedPath;
        }

        return defaultPath;
    }

    public String[] getCurrentTabNames() {

        final String names[] = new String[this.pane.getTabCount()];

        for(int i = 0; i < names.length; ++i) {

            names[i] = this.pane.getComponent(i).getName();
        }

        return names;
    }

    public int getCurrentTabCount(TabType type) {

        int count = 0;

        for(Tab panel : this.tabs.values()) {

            if ((type == null) || (panel.getTabType() == type)) {

                ++count;
            }
        }

        return count;
    }

    public String getNextTabName(TabType type) {

        String label = type.getLabel();

        this.increment(type);

        return (label + SEPARATOR + this.getTabCount(type));
    }

    public Tab getSelectedTab() {

        Component component = this.pane.getSelectedComponent();

        if (component == null) {

            return null;
        }
        else {

            return this.getTab(component.getName());
        }
    }

    public void createEntityTab(PDU pdu) {

        EntityTab tab = new EntityTab(pdu);

        this.addTab(tab);
    }

    private Tab getTab(String name) {

        return this.tabs.get(name);
    }

    private void addTab(Tab tab) {

        String name = tab.getTabName();

        if (this.getTab(name) != null) {

            tab.setTabName(this.getSubsequentName(name));

            name = tab.getTabName();
        }

        if (tab instanceof PDUTab) {

            ((PDUTab)tab).updateClipboardStatus(this.clipboard);
        }

        this.tabs.put(name, tab);
        this.pane.add(name, tab.getPanel());
        this.pane.setSelectedComponent(tab.getPanel());

        System.out.println(
            "Added " + tab.getTabType().getLabel() +
            " tab: " + tab.getTabName());

        this.checkName(name);
    }

    private int getTabCount(TabType type) {

        if (!this.counters.containsKey(type)) {

            this.counters.put(type, new Integer(0));
        }

        return this.counters.get(type).intValue();
    }

    private void closeTabs() {

        for(Tab tab : this.tabs.values()) {

            tab.close();
        }
    }

    private void removeAllTabs() {

        while(!this.tabs.isEmpty()) {

            String name = this.tabs.firstKey();

            this.tabs.get(name).close();
            this.tabs.remove(name);
        }

        this.pane.removeAll();
    }

    private void fireClipboardUpdate() {

        for(Tab tab : this.tabs.values()) {

            if (tab instanceof PDUTab) {

                ((PDUTab)tab).updateClipboardStatus(this.clipboard);
            }
        }
    }

    private void showPopup(MouseEvent event) {

        ((StalkerAction)this.stalkerAction).setTab(this.getSelectedTab());
        ((TextAction)this.textAction).setTab(this.getSelectedTab());

        if (event.isPopupTrigger()) {

            this.popup.show(this.pane, event.getX(), event.getY());
        }
    }

    private void increment(TabType type) {

        int count = this.getTabCount(type);

        this.counters.put(type, new Integer(count + 1));

        if (logger.isLoggable(Level.FINER)) {

            logger.finer(
                "Incremented " + type +
                " counter to " + this.getTabCount(type));
        }
    }

    private void reset(TabType type, int count) {

        this.counters.put(type, new Integer(count));

        if (logger.isLoggable(Level.FINER)) {

            logger.finer("Reset " + type + " counter to " + count);
        }
    }

    private String getSubsequentName(String previous) {

        String prefix = this.getPrefix(previous);
        String subsequent = null;

        if (prefix == null) {

            subsequent = this.getSubsequentName(previous, 1);
        }
        else {

            Integer number = this.getNumber(prefix, previous);

            if (number == null) {

                subsequent = this.getSubsequentName(prefix, 1);
            }
            else {

                subsequent = this.getSubsequentName(prefix, (number + 1));
            }
        }

        return subsequent;
    }

    private String getSubsequentName(String prefix, int start) {

        String subsequent = null;
        int count = start;

        while(subsequent == null) {

            subsequent = (prefix + SEPARATOR + count);

            if (this.tabs.containsKey(subsequent)) {

                subsequent = null;
                ++count;
            }
        }

        return subsequent;
    }

    private String getPrefix(String name) {

        int first = name.indexOf(SEPARATOR);

        if (first > 0) {

            if (name.indexOf(SEPARATOR, (first + 1)) == -1) {

                String prefix = name.substring(0, first);

                logger.finest("Returning " + prefix + " for " + name);

                return prefix;
            }
        }

        logger.finest("Returning null for " + name);

        return null;
    }

    private Integer getNumber(String prefix, String name) {

        String number = null;

        if (name.contains(prefix + SEPARATOR)) {

            if (name.length() > prefix.length()) {

                number = name.substring(prefix.length() + 1);
            }
        }

        try {

            Integer integer = Integer.parseInt(number);

            logger.finest("Returning " + integer + " for " + name);

            return integer;
        }
        catch(NumberFormatException exception) {

            return null;
        }
    }

    private void checkName(String name) {

        for(TabType type : TabType.values()) {

            String label = type.getLabel();
            Integer number = this.getNumber(label, name);

            if (number != null) {

                if (number > this.getTabCount(type)) {

                    this.reset(type, number.intValue());
                }
            }
        }
    }

    private Tab createTab(String name, TabType type) {

        switch(type) {

            case CAPTURE:
                return new CaptureTab(name);
            case PLAYBACK:
                return new PlaybackTab(name);
            case CFS:
                return new CFSTab(name);
            case ENTITY:
                return new EntityTab(name);
            case BUILDER:
                return new BuilderTab(name);
            default:
                logger.severe("Unsupported tab type: " + type);
                return null;
        }
    }

    private PDUTab createPDUTab(TabType type) {

        String name = this.getNextTabName(type);

        if (type == TabType.CAPTURE) {

            return new CaptureTab(name);
        }
        else if (type == TabType.PLAYBACK) {

            return new PlaybackTab(name);
        }
        else {

            logger.severe("Invalid tab type: " + type);
            return null;
        }
    }

    private void createMenus() {

        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu tabs = new JMenu("Tabs");
        JMenu network = new JMenu("Network");
        JMenu tools = new JMenu("Tools");
        JMenu options = new JMenu("Options");
        JCheckBoxMenuItem item = null;

        file.add(this.loadAction);
        file.add(this.saveAction);
        file.addSeparator();
        file.add(new ExitAction());

        edit.add(this.deleteAllAction);
        edit.add(this.deleteAction);
        edit.addSeparator();
        edit.add(this.cutAction);
        edit.add(this.copyAction);
        edit.add(this.pasteAction);

        tabs.add(this.addTabAction);
        tabs.add(this.removeTabAction);
        tabs.add(this.renameTabAction);
        tabs.add(this.reordersTabAction);

        network.add(new NetworkInfoAction());
        network.add(new MulticastAddressesAction());

        tools.add(new EntityTypesAction());
        tools.add(new ObjectTypesAction());
        tools.add(new NumberConversionAction());
        tools.add(new ArmyTrackingAction());

        item = new JCheckBoxMenuItem(new ToggleToolBarAction());
        item.setSelected(true);
        options.add(item);

        this.menus.add(file);
        this.menus.add(edit);
        this.menus.add(tabs);
        this.menus.add(network);
        this.menus.add(tools);
        this.menus.add(options);
    }

    private void createTools() {

        this.tools.setFloatable(false);
        this.tools.add(this.saveAction);
        this.tools.add(this.loadAction);
        this.tools.addSeparator();
        this.tools.add(this.addTabAction);
        this.tools.add(this.removeTabAction);
        this.tools.addSeparator();
        this.tools.add(this.deleteAllAction);
        this.tools.add(this.deleteAction);
        this.tools.addSeparator();
        this.tools.add(this.cutAction);
        this.tools.add(this.copyAction);
        this.tools.add(this.pasteAction);
    }

    private TabType getTabType(DataInputStream stream) throws IOException {

        int value = stream.readInt();

        logger.finer("Tab type as integer: " + value);

        for(TabType type : TabType.values()) {

            if (type.ordinal() == value) {

                return type;
            }
        }

        throw new IOException("Invalid tab type value: " + value);
    }

    class SaveAction extends AbstractAction {

        private static final String SAVE = "Save";

        private JFileChooser chooser = null;

        public SaveAction() {

            super(SAVE);

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("file_save.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Saves all data to file.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (tabs.isEmpty()) {

                JOptionPane.showMessageDialog(
                    frame,
                    "No data to save!",
                    SAVE,
                    JOptionPane.ERROR_MESSAGE);
            }
            else {

                File file = this.getFile();

                if (file != null) {

                    this.save(file);
                }
            }
        }

        public File getFile() {

            File file = null;

            if (this.chooser == null) {

                this.chooser = new JFileChooser(getSavedDataPath());
            }

            int choice = this.chooser.showDialog(getFrame(), SAVE);

            if (choice == JFileChooser.APPROVE_OPTION) {

                file = this.chooser.getSelectedFile();

                if ((file != null) && file.exists()) {

                    if (!file.isFile()) {

                        JOptionPane.showMessageDialog(
                            frame,
                            "Not a file: " + file.getName(),
                            "Save Error",
                            JOptionPane.ERROR_MESSAGE);

                        file = null;
                    }
                    else if (!file.canWrite()) {

                        JOptionPane.showMessageDialog(
                            frame,
                            "File cannnot be over-written: " + file.getName(),
                            "Save Error",
                            JOptionPane.ERROR_MESSAGE);

                        file = null;
                    }
                    else {

                        choice = JOptionPane.showConfirmDialog(
                            frame,
                            "Overwrite existing file: " + file.getName() + "?",
                            SAVE,
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                        if (choice != JOptionPane.YES_OPTION) {

                            file = null;
                        }
                    }
                }
            }

            return file;
        }

        private void save(File file) {

            DataOutputStream stream = null;

            try {

                stream = new DataOutputStream(new FileOutputStream(file));

                long start = System.currentTimeMillis();

                stream.writeLong(Version.getLatest().getValue());
                stream.writeInt(tabs.size());

                logger.config("Tabs to save: " + tabs.size());

                for(int i = 0; i < pane.getTabCount(); ++i) {

                    String title = pane.getTitleAt(i);

                    if (title == null) {

                        logger.severe("Null tab title at index " + i);
                    }
                    else {

                        Tab tab = getTab(title);

                        if (tab == null) {

                            logger.severe("Null tab for " + title);
                        }
                        else {

                            logger.config("Saving tab: " + title);

                            tab.save(stream);
                        }
                    }
                }

                logger.info("Bytes saved: " + stream.size());

                stream.close();

                long time = (System.currentTimeMillis() - start);

                JOptionPane.showMessageDialog(
                    getFrame(),
                    "Saved successfully in " + time + " milliseconds.",
                    SAVE,
                    JOptionPane.INFORMATION_MESSAGE);
            }
            catch(Exception exception) {

                logger.log(Level.SEVERE, "Caught exception!", exception);
            }
        }
    }

    class StalkerAction extends AbstractAction {

        private static final String SAVE = "Save as PDU Stalker File";

        private JFileChooser chooser = null;
        private PDUTab tab = null;

        public StalkerAction() {

            super(SAVE);

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("file_save.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Saves PDUs to file loadable by PDU Stalker.");
        }

        public void setTab(Tab tab) {

            if (tab instanceof PDUTab) {

                this.tab = (PDUTab)tab;
                super.setEnabled(this.tab.getPDUCount() > 0);
            }
            else {

                this.tab = null;
                super.setEnabled(false);
            }
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if ((this.tab == null) || (this.tab.getPDUCount() == 0)) {

                JOptionPane.showMessageDialog(
                    frame,
                    "No data to save!",
                    SAVE,
                    JOptionPane.ERROR_MESSAGE);
            }
            else {

                if (this.chooser == null) {

                    this.chooser = new JFileChooser(getSavedDataPath());
                }

                File file = Utilities.getSaveFile(SAVE, this.chooser);

                if (file != null) {

                    this.save(file, this.tab.getListCopy());
                }
            }
        }

        private void save(File file, List<PDU> list) {

            DataOutputStream stream = null;
            int size = list.size();

            try {

                stream = new DataOutputStream(new FileOutputStream(file));

                long start = System.currentTimeMillis();

                stream.writeLong(Version.PDU_STALKER.getValue());
                stream.writeInt(list.size());

                for(int i = 0; i < size; ++i) {

                    list.get(i).save(stream);
                }

                logger.info("Bytes saved: " + stream.size());

                stream.close();

                long time = (System.currentTimeMillis() - start);

                JOptionPane.showMessageDialog(
                    getFrame(),
                    "Saved successfully in " + time + " milliseconds.",
                    SAVE,
                    JOptionPane.INFORMATION_MESSAGE);
            }
            catch(Exception exception) {

                logger.log(Level.SEVERE, "Caught exception!", exception);
            }
        }
    }

    class TextAction extends AbstractAction {

        private static final String SAVE = "Save as Text File";

        private JFileChooser chooser = null;
        private PDUTab tab = null;

        public TextAction() {

            super(SAVE);

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("file_save.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Saves content of PDUs to text file.");
        }

        public void setTab(Tab tab) {

            if (tab instanceof PDUTab) {

                this.tab = (PDUTab)tab;
                super.setEnabled(this.tab.getPDUCount() > 0);
            }
            else {

                this.tab = null;
                super.setEnabled(false);
            }
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if ((this.tab == null) || (this.tab.getPDUCount() == 0)) {

                JOptionPane.showMessageDialog(
                    frame,
                    "No data to save!",
                    SAVE,
                    JOptionPane.ERROR_MESSAGE);
            }
            else {

                if (this.chooser == null) {

                    this.chooser = new JFileChooser(getSavedDataPath());
                }

                File file = Utilities.getSaveFile(SAVE, this.chooser);

                if (file != null) {

                    this.tab.writeTextTo(file, null);
                }
            }
        }
    }

    class LoadAction extends AbstractAction {

        private static final String LOAD = "Load";

        private JFileChooser chooser = null;

        public LoadAction() {

            super(LOAD);

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("file_load.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Loads saved data from file.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            int choice = JOptionPane.NO_OPTION;
            int count = getCurrentTabCount(null);

            if (count > 0) {

                choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Close existing tabs before loading?",
                    LOAD,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            }

            if (choice != JOptionPane.CANCEL_OPTION) {

                if (choice == JOptionPane.YES_OPTION) {

                    removeAllTabs();
                }

                File file = this.getFile();

                if (file != null) {

                    this.load(file);
                }
            }
        }

        private File getFile() {

            File file = null;

            if (this.chooser == null) {

                this.chooser = new JFileChooser(getSavedDataPath());
            }

            int choice = this.chooser.showDialog(getFrame(), LOAD);

            if (choice == JFileChooser.APPROVE_OPTION) {

                file = this.chooser.getSelectedFile();

                if ((file != null) && file.exists()) {

                    if (!file.isFile()) {

                        JOptionPane.showMessageDialog(
                            frame,
                            "Not a file: " + file.getName(),
                            "Load Error",
                            JOptionPane.ERROR_MESSAGE);

                        file = null;
                    }
                    else if (!file.canRead()) {

                        JOptionPane.showMessageDialog(
                            frame,
                            "File is not readable: " + file.getName(),
                            "Save Error",
                            JOptionPane.ERROR_MESSAGE);

                        file = null;
                    }
                }
            }

            return file;
        }

        private void load(File file) {

            NumberFormat formatter = NumberFormat.getInstance();

            logger.config("Loading PDUs, file: " + file.getAbsolutePath());

            try {

                DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

                long start = System.currentTimeMillis();
                int bytes = stream.available();

                logger.fine("Bytes to load: " + bytes);

                stream.mark(stream.available());

                Version version = Version.getVersion(stream.readLong());

                if (version == null) {

                    stream.close();
                    throw new IOException("Invalid data (bad version)!");
                }
                else {

                    StringBuilder builder = new StringBuilder();

                    builder.append("Loading file: ");
                    builder.append(file.getAbsolutePath());
                    builder.append("\n  size: ");
                    builder.append(formatter.format(bytes));
                    builder.append(" bytes\n  data version: ");
                    builder.append(version.toString());

                    System.out.println(builder.toString());

                    logger.config(builder.toString());

                    if (version == Version.PDU_STALKER) {

                        this.loadPDUStalker(stream);
                    }
                    else if (version == Version.PCAP_SWAP_V2_4) {

                        // Multiple tabs unsupported.

                        // Go back to the beginning of the stream.
                        stream.reset();

                        loadPcap(file.getName(), stream, true);

                    }
                    else if (version == Version.PCAP_NOSWAP_V2_4) {

                        loadPcap(file.getName(), stream, false);
                    }
                    else if (version == Version.DISCOVER_V1) {

                        // Multiple tabs unsupported by version 1.
                        this.loadPrevious(stream);
                    }
                    else {

                        // Differences in saved data from V2+ are handled by
                        // classes that extend the abstract TabData class.
                        this.loadCurrent(version, stream);
                    }

                    if (stream.available() > 0) {

                        logger.warning(
                            stream.available() +
                            " bytes left in stream after loading...");
                    }

                    stream.close();

                    long time = (System.currentTimeMillis() - start);

                    JOptionPane.showMessageDialog(
                        getFrame(),
                        "Loaded successfully in " + time + " milliseconds.",
                        LOAD,
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
            catch(Exception exception) {

                logger.log(Level.SEVERE, "Caught exception!", exception);
            }
        }

        private void loadPDUStalker(

            DataInputStream stream) throws IOException {

            PDUTab tab = createPDUTab(TabType.PLAYBACK);
            int count = stream.readInt();

            logger.info(
                "Loading " + count +
                " PDU Stalker PDUs to " + tab.getTabName());

            for(int i = 0; i < count; ++i) {

                PDU pdu = PDU.create(stream);

                if (pdu != null) {

                    tab.addPDU(pdu);
                }
            }

            addTab(tab);
        }

        private void loadPcap(String name, DataInputStream stream, boolean swap) throws IOException {

            List<PDU> pdus = PCAP.getPDUs(stream, swap);

            if (pdus.isEmpty()) {

                JOptionPane.showMessageDialog(
                    getFrame(),
                    "No PDUs could be loaded from PCAP file!",
                    "Load PCAP",
                    JOptionPane.ERROR_MESSAGE);
            }
            else {

                PDUTab tab = (PDUTab)createTab(name, TabType.CAPTURE);

                for(PDU pdu : pdus) {

                    tab.addPDU(pdu);
                }

                tab.refresh();
                addTab(tab);
            }
        }

        private void loadPrevious(
            DataInputStream stream) throws IOException {

            PDUTab capture = createPDUTab(TabType.CAPTURE);
            PDUTab playback = createPDUTab(TabType.PLAYBACK);

            capture.load(Version.DISCOVER_V1, stream);
            playback.load(Version.DISCOVER_V1, stream);

            if (capture.getPDUCount() > 0) {

                addTab(capture);
            }

            if (playback.getPDUCount() > 0) {

                addTab(playback);
            }
        }

        private void loadCurrent(
            Version version,
            DataInputStream stream) throws IOException {

            // Number of tabs to be loaded
            final int tabs = stream.readInt();

            logger.config("Tabs to load: " + tabs);

            for(int i = 0; i < tabs; ++i) {

                logger.config(
                    "Iteration " + i + ": " + stream.available() +
                    " bytes remaining");

                TabType type = getTabType(stream);
                Tab tab = createTab(stream.readUTF(), type);

                tab.load(version, stream);

                addTab(tab);
            }
        }
    }

    class ExitAction extends AbstractAction {

        public ExitAction() {

            super("Exit");

            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Terminates application.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            getFrame().dispose();
        }
    }

    class DeleteAllAction extends AbstractAction {

        public DeleteAllAction() {

            super("Delete All");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("edit_delete_all.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Clear all PDUs (capture and playback tabs only).");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Tab tab = getSelectedTab();

            if (tab instanceof PDUTab) {

                ((PDUTab)tab).deleteAll();
            }
        }
    }

    class DeleteAction extends AbstractAction {

        public DeleteAction() {

            super("Delete");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("edit_delete.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Delete selected PDUs (playback tab only).");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Tab tab = getSelectedTab();

            if (tab instanceof PDUTab) {

                ((PDUTab)tab).delete();
            }
        }
    }

    class CutAction extends AbstractAction {

        public CutAction() {

            super("Cut");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("edit_cut.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Cut selected PDUs (certain tab only).");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Tab tab = getSelectedTab();

            if (tab instanceof ClipboardTab) {

                ((ClipboardTab)tab).cut(clipboard);

                fireClipboardUpdate();
            }
        }
    }

    class CopyAction extends AbstractAction {

        public CopyAction() {

            super("Copy");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("edit_copy.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Copy selected PDUs (certain tabs only).");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Tab tab = getSelectedTab();

            if (tab instanceof ClipboardTab) {

                ((ClipboardTab)tab).copy(clipboard);

                fireClipboardUpdate();
            }
        }
    }

    class PasteAction extends AbstractAction {

        public PasteAction() {

            super("Paste");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("edit_paste.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Paste selected PDUs (certain tab only).");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Tab tab = getSelectedTab();

            if (tab instanceof ClipboardTab) {

                ((ClipboardTab)tab).paste(clipboard);

                fireClipboardUpdate();
            }
            else {

            }
        }
    }

    class AddTabAction extends AbstractAction {

        public AddTabAction() {

            super("Add Tab");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("tab_add.png"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Adds application tab.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            AddTabDialog dialog = new AddTabDialog();
            TabType type = dialog.getTabType();

            if (type != null) {

                String name = dialog.getTabName();
                Tab tab = null;

                if (name.isEmpty()) {

                    name = getNextTabName(type);
                }
                else if (getTab(name) != null) {

                    JOptionPane.showMessageDialog(
                        getFrame(),
                        ("Tab name is already in use: " + name),
                        "Add Tab",
                        JOptionPane.ERROR_MESSAGE);

                    type = null;
                }

                if (type != null) {

                    switch(type) {

                        case CAPTURE:
                            tab = new CaptureTab(name);
                            break;
                        case PLAYBACK:
                            tab = new PlaybackTab(name);
                            break;
                        case CFS:
                            tab = new CFSTab(name);
                            break;
                        case ENTITY:
                            tab = new EntityTab(name);
                            break;
                        case BUILDER:
                            tab = new BuilderTab(name);
                            break;
                    }

                    if (tab != null) {

                        addTab(tab);
                    }
                }
            }
        }
    }

    class RemoveTabAction extends AbstractAction {

        public RemoveTabAction() {

            super("Remove Tab");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("tab_remove.png"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Removes currently selected application tab.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Tab tab = getSelectedTab();

            if (tab != null) {

                String name = tab.getTabName();

                int result = JOptionPane.showConfirmDialog(
                    DiscoverFrame.getFrame(),
                    "Remove '" + name + "' (this cannot be undone)?",
                    "Remove Tab",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {

                    logger.fine("Removing tab: " + name);

                    tab.close();
                    tabs.remove(name);
                    pane.remove(tab.getPanel());
                }
            }
        }
    }

    class RenameTabAction extends AbstractAction {

        public RenameTabAction() {

            super("Rename Tab");

            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Rename currently selected application tab.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Tab tab = getSelectedTab();

            if (tab != null) {

                String name = null;
                boolean cancel = false;

                // Loop until valid name is entered or user cancels
                while((name == null) && !cancel) {

                    name = JOptionPane.showInputDialog(
                        DiscoverFrame.getFrame(),
                        "Enter new unique name:",
                        tab.getTabName());

                    if (name == null) {

                        cancel = true;
                    }
                    else if (getTab(name) != null) {

                        JOptionPane.showMessageDialog(
                            DiscoverFrame.getFrame(),
                            "The name \"" + name + "\" is in use.\n" +
                            "Enter new unique name or cancel operation.",
                            "Tab Name",
                            JOptionPane.ERROR_MESSAGE);

                        name = null;
                    }
                    else if (name.isEmpty()) {

                        name = null;
                    }
                }

                if (name != null) {

                    String original = tab.getTabName();
                    checkName(name);

                    int count = pane.getComponentCount();

                    tab.setTabName(name);

                    for(int i = 0; i < count; ++i) {

                        if (pane.getComponentAt(i) == tab.getPanel()) {

                            pane.removeTabAt(i);

                            pane.insertTab(
                                name,
                                null, // icon
                                tab.getPanel(),
                                null, // tool tip
                                i);

                            pane.setSelectedIndex(i);
                        }
                    }

                    tabs.remove(original);
                    tabs.put(name, tab);
                }
            }
        }
    }

    class ReorderTabsAction extends AbstractAction {

        public ReorderTabsAction() {

            super("Reorder Tabs");

            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Modify tab order.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            final int count = getCurrentTabCount(null);

            if (count > 1) {

                final String names[] = getCurrentTabNames();

                String selection = names[pane.getSelectedIndex()];
                ReorderTabsDialog dialog = new ReorderTabsDialog(names);

                if (dialog.reorderTabs()) {

                    int index = 0;

                    pane.removeAll();

                    for(int i = 0; i < count; ++i) {

                        pane.add(
                            names[i],
                            tabs.get(names[i]).getPanel());

                        if (names[i] == selection) {

                            index = i;
                        }
                    }

                    pane.setSelectedIndex(index);
                }
            }
        }
    }

    class NetworkInfoAction extends AbstractAction {

        public NetworkInfoAction() {

            super("Network Information");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("info.png"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Shows network configurations.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Map<String, String> info = Network.getNetworkInfo(true);

            if (info.isEmpty()) {

                JOptionPane.showMessageDialog(
                    frame,
                    "No network information available.",
                    "Network Information",
                    JOptionPane.ERROR_MESSAGE);
            }
            else {

                JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);

                for(String key : info.keySet()) {

                    JEditorPane pane = new JEditorPane(
                        "text/html",
                        info.get(key));

                    tabs.addTab(key, new JScrollPane(pane));
                }

                tabs.setPreferredSize(new Dimension(500, 250));

                JOptionPane.showMessageDialog(
                    frame,
                    tabs,
                    "Network Information",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    class MulticastAddressesAction extends AbstractAction {

        public MulticastAddressesAction() {

            super(MulticastAddressesDialog.TITLE);

            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Add or delete multicast IP addresses used to capture PDUs.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            new MulticastAddressesDialog();
        }
    }

    class EntityTypesAction extends AbstractAction {

        public EntityTypesAction() {

            super("Entity Type Dictionary");

            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Brings up the Entity Type dictionary.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            EntityTypesFrame.setVisible();
        }
    }

    class ObjectTypesAction extends AbstractAction {

        public ObjectTypesAction() {

            super("Object Type Dictionary");

            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Brings up the Object Type dictionary.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            ObjectTypesFrame.setVisible();
        }
    }

    class NumberConversionAction extends AbstractAction {

        public NumberConversionAction() {

            super("Number Conversion");

            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Tool for conversion to hexdecimal, binary, etc.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            ConversionFrame.setVisible();
        }
    }

    class ArmyTrackingAction extends AbstractAction {

        public ArmyTrackingAction() {

            super("Army Tracking");

            super.putValue(
                Action.SHORT_DESCRIPTION,
                "View Army Tracking ");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            ArmyTrackingFrame.setVisible();
        }
    }

    class ToggleToolBarAction extends AbstractAction {

        public ToggleToolBarAction() {

            super("View Toolbar");

            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Show or hide toolbar.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            JCheckBoxMenuItem item = (JCheckBoxMenuItem)event.getSource();

            tools.setVisible(item.isSelected());
        }
    }
}
