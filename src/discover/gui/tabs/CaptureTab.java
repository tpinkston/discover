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

/**
 * @author Tony Pinkston
 */
@SuppressWarnings("serial")
public class CaptureTab extends PDUTab implements CaptureThreadListener {

    private static final Color ACTIVE_COLOR = Color.BLACK;
    private static final Color INACTIVE_COLOR = Color.RED;
    private static final String CLICK_TO_SUSPEND = "Click to suspend PDU capture on port: ";
    private static final String CLICK_TO_RESUME = "Click to resume PDU capture on port: ";

    private Map<Integer, Port> ports = null;

    private EntityTrackerFrame entityTracker = null;

    private SiteMapFrame siteMap = null;

    public CaptureTab(String name) {

        super(name, TabType.CAPTURE);

        fill();
    }

    @Override
    public void load(Version version, DataInputStream stream)
        throws IOException {

        super.load(version, stream);

        if (version.getValue() > Version.DISCOVER_V2.getValue()) {

            int count = stream.readShort();

            for(int i = 0; i < count; ++i) {

                createPort(new Integer(stream.readInt()), true);
            }
        }
    }

    @Override
    public void save(DataOutputStream stream) throws IOException {

        super.save(stream);

        if (ports == null) {

            stream.writeShort(0);
        }
        else {

            stream.writeShort(ports.size());

            for(Integer port : ports.keySet()) {

                stream.writeInt(port.intValue());
            }
        }
    }

    @Override
    public void close() {

        if (ports != null) {

            for(Port port : ports.values()) {

                if (!port.thread.isStopped()) {

                    port.thread.setStopped(true);
                }
            }
        }

        if (entityTracker != null) {

            entityTracker.destroy();
        }

        if (siteMap != null) {

            siteMap.destroy();
        }
    }

    @Override
    public void setName(String name) {

        super.setName(name);

        if (ports != null) {

            for(Port port : ports.values()) {

                port.thread.setName(getThreadName(port.port));
            }
        }

        if (entityTracker != null) {

            entityTracker.setTitle(name);
        }

        if (siteMap != null) {

            siteMap.setTitle(name);
        }
    }

    @Override
    public void delete() {

        logger.error("Method not supported!");
    }

    @Override
    public void deleteAll() {

        if (getPDUCount() > 0) {

            int result = JOptionPane.showConfirmDialog(
                this,
                "Delete all captured PDUs (cannot be undone)?",
                getName(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {

                clearAll();
            }
        }
    }

    @Override
    public void cut(ArrayList<PDU> clipboard) {

        logger.error("Method not supported!");
    }

    @Override
    public void copy(ArrayList<PDU> clipboard) {

        // Copy selected PDUs and add them to the list.
        lock.readLock().lock();
        int size = table.getRowCount();
        int selections[] = table.getSelectedRows();
        lock.readLock().unlock();

        logger.info(
            "Copying " + selections.length + " of " + size + " " +
            getName() + " PDUs...");

        logger.debug("Selected rows: {}", Arrays.toString(selections));

        for(int i = 0; i < selections.length; ++i) {

            int index = sorter.convertRowIndexToModel(selections[i]);

            logger.debug("Copying PDU at index: {}", index);

            clipboard.add(getPDU(index).copy());
        }
    }

    @Override
    public void paste(ArrayList<PDU> clipboard) {

        logger.error("Method not supported!");
    }

    @Override
    public void pdusCaptured(List<PDU> list) {

        if (!list.isEmpty()) {

            final List<PDU> copy = new ArrayList<PDU>(list);

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {

                    logger.debug("Adding {} PDUs", copy.size());

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

        if (entityTracker != null) {

            entityTracker.clearAll();
        }

        if (siteMap != null) {

            siteMap.clearAll();
        }
    }

    private void createPort(Integer number, boolean paused) {

        if (ports == null) {

            ports = new TreeMap<>();
        }

        if (ports.keySet().contains(number)) {

            logger.error("Port already in use: {}", number);
        }
        else try {

            CaptureThread thread = new CaptureThread(
                getThreadName(number),
                this,
                number.intValue());

            Port port = new Port(thread, number.intValue(), paused);

            tools.add(port.button);

            ports.put(number, port);

            if (paused) {

                port.thread.startPaused();
            }
            else {

                port.thread.start();
            }
        }
        catch(Exception exception) {

            logger.error("Caught exception!", exception);

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

        tabbed.add("Content", content);
        tabbed.add("Byte View", hexadecimal);

        Utilities.addComponent(
            status,
            total,
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            0.0, 0.0,
            insets);
        Utilities.addComponent(
            status,
            visible,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.0, 0.0,
            insets);
        Utilities.addComponent(
            status,
            clipboard,
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
        JScrollPane scroller = new JScrollPane(table);

        scroller.setPreferredSize(new Dimension(800, 700));

        panel.add(tools, BorderLayout.NORTH);
        panel.add(scroller, BorderLayout.CENTER);
        panel.add(status, BorderLayout.SOUTH);

        split.setContinuousLayout(true);
        split.setLeftComponent(panel);
        split.setRightComponent(tabbed);

        tools.add(new ScrollAction());
        tools.addSeparator();
        tools.add(new FilterDialogAction());
        tools.addSeparator();
        tools.add(new SiteMapAction());
        tools.add(new EntityTrackerAction());
        tools.addSeparator();
        tools.add(new AddPortAction());
        tools.add(new RemovePortAction());
        tools.addSeparator();

        Utilities.addComponent(
            this,
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

            button = new JButton(Integer.toString(port));
            button.addActionListener(this);

            if (paused) {

                setInactive();
            }
            else {

                setActive();
            }
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (button.getForeground().equals(ACTIVE_COLOR)) {

                // Suspend notifications...
                thread.setPaused(true);
                setInactive();
            }
            else if (button.getForeground().equals(INACTIVE_COLOR)) {

                // Resume notifications...
                thread.setPaused(false);
                setActive();
            }
        }

        void setActive() {

            button.setToolTipText(CLICK_TO_SUSPEND + Integer.toString(port));
            button.setForeground(ACTIVE_COLOR);
        }

        void setInactive() {

            button.setToolTipText(CLICK_TO_RESUME + Integer.toString(port));
            button.setForeground(INACTIVE_COLOR);
        }
    }

    class AddPortAction extends AbstractAction {

        public AddPortAction() {

            super("Add Port");

            putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("port_add.gif"));
            putValue(
                Action.SHORT_DESCRIPTION,
                "Add capture port.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            GetPortDialog dialog = new GetPortDialog(
                "Inbound Port",
                ((ports == null) ? null : ports.keySet()));

            Integer number = dialog.getPort();

            if (number != null) {

                if ((ports == null) || !ports.containsKey(number)) {

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

    class RemovePortAction extends AbstractAction {

        public RemovePortAction() {

            super("Remove Port");

            putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("port_remove.gif"));
            putValue(
                Action.SHORT_DESCRIPTION,
                "Remove capture port.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Set<Integer> choices = ((ports != null) ? ports.keySet() : null);

            if ((choices != null) && !choices.isEmpty()) {

                RemovePortDialog dialog = new RemovePortDialog(choices);

                List<Integer> list = dialog.getPorts();

                for(Integer number : list) {

                    Port port = ports.get(number);

                    if (port == null) {

                        logger.error("No port for {}", number);
                    }
                    else {

                        tools.remove(port.button);
                        tools.revalidate();
                        tools.repaint();

                        port.thread.setStopped(true);

                        ports.remove(number);

                        System.out.println(
                            getName() +
                            ": Removed port: " + number);
                    }
                }
            }
        }
    }

    class EntityTrackerAction extends AbstractAction {

        public EntityTrackerAction() {

            super("Entity Tracker");

            putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("entity_tracker.gif"));
            putValue(
                Action.SHORT_DESCRIPTION,
                "Show entity tracker.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (entityTracker == null) {

                entityTracker = new EntityTrackerFrame(getName());
            }

            entityTracker.setVisible(true);
        }
    }

    class SiteMapAction extends AbstractAction {

        public SiteMapAction() {

            super("Site Map");

            putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("site_map.gif"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Show site map.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (siteMap == null) {

                siteMap = new SiteMapFrame(getName());
            }

            siteMap.setVisible(true);
        }
    }
}
