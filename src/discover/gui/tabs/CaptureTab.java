/**
 * @author Tony Pinkston
 */
package discover.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import discover.common.Version;
import discover.gui.Utilities;
import discover.gui.dialogs.GetPortDialog;
import discover.gui.dialogs.RemovePortDialog;
import discover.gui.frames.DiscoverFrame;
import discover.gui.frames.EntityTrackerFrame;
import discover.gui.frames.SiteMapFrame;
import discover.system.CaptureThread;
import discover.system.CaptureThreadListener;
import discover.vdis.PDU;

public class CaptureTab extends PDUTab implements CaptureThreadListener {

    private static final Color ACTIVE_COLOR = Color.BLACK;
    private static final Color INACTIVE_COLOR = Color.RED;
    private static final String CLICK_TO_SUSPEND = "Click to suspend PDU capture on port: ";
    private static final String CLICK_TO_RESUME = "Click to resume PDU capture on port: ";

    private final Map<Integer, Port> ports = new TreeMap<Integer, Port>();

    private EntityTrackerFrame entityTracker = null;

    private SiteMapFrame siteMap = null;

    public CaptureTab(String name) {

        super(name, TabType.CAPTURE);

        this.fill();
    }

    @Override
    public void load(Version version, DataInputStream stream)
        throws IOException {

        super.load(version, stream);

        if (version.getValue() > Version.DISCOVER_V2.getValue()) {

            int count = stream.readShort();

            for(int i = 0; i < count; ++i) {

                this.createPort(new Integer(stream.readInt()), true);
            }
        }
    }

    @Override
    public void save(DataOutputStream stream) throws IOException {

        super.save(stream);

        stream.writeShort(this.ports.size());

        for(Integer port : this.ports.keySet()) {

            stream.writeInt(port.intValue());
        }
    }

    @Override
    public void close() {

        for(Port port : this.ports.values()) {

            if (!port.thread.isStopped()) {

                port.thread.setStopped(true);
            }
        }

        if (this.entityTracker != null) {

            this.entityTracker.destroy();
        }

        if (this.siteMap != null) {

            this.siteMap.destroy();
        }
    }

    @Override
    public void setTabName(String name) {

        super.setTabName(name);

        for(Port port : this.ports.values()) {

            port.thread.setName(this.getThreadName(port.port));
        }

        if (this.entityTracker != null) {

            this.entityTracker.setTitle(name);
        }

        if (this.siteMap != null) {

            this.siteMap.setTitle(name);
        }
    }

    @Override
    public void delete() {

        logger.severe("Method not supported!");
    }

    @Override
    public void deleteAll() {

        if (super.getPDUCount() > 0) {

            int result = JOptionPane.showConfirmDialog(
                getPanel(),
                "Delete all captured PDUs (cannot be undone)?",
                getPanel().getName(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {

                this.clearAll();
            }
        }
    }

    @Override
    public void cut(ArrayList<PDU> clipboard) {

        logger.severe("Method not supported!");
    }

    @Override
    public void copy(ArrayList<PDU> clipboard) {

        // Copy selected PDUs and add them to the list.
        this.lock.readLock().lock();
        int size = this.table.getRowCount();
        int selections[] = this.table.getSelectedRows();
        this.lock.readLock().unlock();

        logger.info(
            "Copying " + selections.length + " of " + size + " " +
            super.getTabName() + " PDUs...");

        logger.finer("Selected rows: " + Arrays.toString(selections));

        for(int i = 0; i < selections.length; ++i) {

            int index = this.sorter.convertRowIndexToModel(selections[i]);

            logger.finest("Copying PDU at index: " + index);

            clipboard.add(this.getPDU(index).copy());
        }
    }

    @Override
    public void paste(ArrayList<PDU> clipboard) {

        logger.severe("Method not supported!");
    }

    @Override
    public void pdusCaptured(List<PDU> list) {

        if (!list.isEmpty()) {

            final List<PDU> copy = new ArrayList<PDU>(list);

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {

                    logger.finer("Adding " + copy.size() + " PDUs");

                    lock.writeLock().lock();

                    CaptureTab.super.list.addAll(copy);

                    int count = CaptureTab.super.list.size();

                    lock.writeLock().unlock();

                    final int start = (count - copy.size());
                    final int end = (count - 1);

                    model.fireTableRowsInserted(start, end);
                    updateTotalStatus();
                    updateVisibleStatus();

                    if (scrollLocked) {

                        table.scrollRectToVisible(
                            table.getCellRect(end, 0, true));
                    }

                    if (entityTracker != null) {

                        entityTracker.processPDUs(copy);
                    }

                    if (siteMap != null) {

                        siteMap.processPDUs(copy);
                    }
                }
            });
        }
    }

    @Override
    protected void clearAll() {

        super.clearAll();

        if (this.entityTracker != null) {

            this.entityTracker.clearAll();
        }

        if (this.siteMap != null) {

            this.siteMap.clearAll();
        }
    }

    private void createPort(Integer number, boolean paused) {

        if (this.ports.keySet().contains(number)) {

            logger.severe("Port already in use: " + number);
        }
        else try {

            CaptureThread thread = new CaptureThread(
                super.getThreadName(number),
                this,
                number.intValue());

            Port port = new Port(thread, number.intValue(), paused);

            super.tools.add(port.button);

            this.ports.put(number, port);

            if (paused) {

                port.thread.startPaused();
            }
            else {

                port.thread.start();
            }
        }
        catch(Exception exception) {

            logger.log(Level.SEVERE, "Caught exception!", exception);

            JOptionPane.showMessageDialog(
                DiscoverFrame.getFrame(),
                ("Caught " + exception.getClass().getSimpleName()),
                "Capture Port",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fill() {

        JPanel status = new JPanel(new GridBagLayout());
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JTabbedPane tabbed = new JTabbedPane(JTabbedPane.BOTTOM);
        Insets insets = new Insets(3, 3, 3, 20);

        tabbed.add("Content", super.content.getPanel());
        tabbed.add("Byte View", super.hexadecimal.getPanel());

        Utilities.addComponent(
            status,
            super.total,
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            insets);
        Utilities.addComponent(
            status,
            super.visible,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.0, 0.0,
            insets);
        Utilities.addComponent(
            status,
            super.clipboard,
            Utilities.HORIZONTAL,
            2, 0,
            1, 1,
            0.0, 0.0,
            insets);
        Utilities.addComponent(
            status,
            new JLabel("    "),
            Utilities.HORIZONTAL,
            3, 0,
            1, 1,
            1.0, 0.0,
            insets);

        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scroller = new JScrollPane(super.table);

        scroller.setPreferredSize(new Dimension(800, 700));

        panel.add(super.tools, BorderLayout.NORTH);
        panel.add(scroller, BorderLayout.CENTER);
        panel.add(status, BorderLayout.SOUTH);

        split.setContinuousLayout(true);
        split.setLeftComponent(panel);
        split.setRightComponent(tabbed);

        super.tools.add(new ScrollAction());
        super.tools.addSeparator();
        super.tools.add(new FilterDialogAction());
        super.tools.addSeparator();
        super.tools.add(new SiteMapAction());
        super.tools.add(new EntityTrackerAction());
        super.tools.addSeparator();
        super.tools.add(new AddPortAction());
        super.tools.add(new RemovePortAction());
        super.tools.addSeparator();

        Utilities.addComponent(
            super.panel,
            split,
            Utilities.BOTH,
            0, 0,
            1, 1,
            1.0, 1.0,
            null);

        split.setDividerLocation(0.9);
    }

    private static class Port implements ActionListener {

        private final int port;
        private final JButton button;
        private final CaptureThread thread;

        Port(CaptureThread thread, int port, boolean paused) {

            this.port = port;
            this.thread = thread;

            this.button = new JButton(Integer.toString(port));
            this.button.addActionListener(this);

            if (paused) {

                this.setInactive();
            }
            else {

                this.setActive();
            }
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (this.button.getForeground().equals(ACTIVE_COLOR)) {

                // Suspend notifications...
                this.thread.setPaused(true);
                this.setInactive();
            }
            else if (this.button.getForeground().equals(INACTIVE_COLOR)) {

                // Resume notifications...
                this.thread.setPaused(false);
                this.setActive();
            }
        }

        void setActive() {

            this.button.setToolTipText(CLICK_TO_SUSPEND + Integer.toString(port));
            this.button.setForeground(ACTIVE_COLOR);
        }

        void setInactive() {

            this.button.setToolTipText(CLICK_TO_RESUME + Integer.toString(port));
            this.button.setForeground(INACTIVE_COLOR);
        }
    }

    @SuppressWarnings("serial")
    class AddPortAction extends AbstractAction {

        public AddPortAction() {

            super("Add Port");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("port_add.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Add capture port.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            GetPortDialog dialog = new GetPortDialog(
                "Inbound Port",
                ports.keySet());

            Integer number = dialog.getPort();

            if (number != null) {

                if (!ports.containsKey(number)) {

                    createPort(number, false);
                }
                else {

                    JOptionPane.showMessageDialog(
                        DiscoverFrame.getFrame(),
                        "Port is already is active on this tab: " + number,
                        "Capture Port",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    @SuppressWarnings("serial")
    class RemovePortAction extends AbstractAction {

        public RemovePortAction() {

            super("Remove Port");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("port_remove.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Remove capture port.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Set<Integer> choices = ports.keySet();

            if (!choices.isEmpty()) {

                RemovePortDialog dialog = new RemovePortDialog(choices);

                List<Integer> list = dialog.getPorts();

                for(Integer number : list) {

                    Port port = ports.get(number);

                    if (port == null) {

                        logger.severe("No port for " + number);
                    }
                    else {

                        tools.remove(port.button);
                        tools.revalidate();
                        tools.repaint();

                        port.thread.setStopped(true);

                        ports.remove(number);

                        System.out.println(
                            getTabName() +
                            ": Removed port: " + number);
                    }
                }
            }
        }
    }

    @SuppressWarnings("serial")
    class EntityTrackerAction extends AbstractAction {

        public EntityTrackerAction() {

            super("Entity Tracker");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("entity_tracker.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Show entity tracker.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (entityTracker == null) {

                entityTracker = new EntityTrackerFrame(getTabName());
            }

            entityTracker.getFrame().setVisible(true);
        }
    }

    @SuppressWarnings("serial")
    class SiteMapAction extends AbstractAction {

        public SiteMapAction() {

            super("Site Map");

            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("site_map.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Show site map.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {


            if (siteMap == null) {

                siteMap = new SiteMapFrame(getTabName());
            }

            siteMap.getFrame().setVisible(true);
        }
    }
}
