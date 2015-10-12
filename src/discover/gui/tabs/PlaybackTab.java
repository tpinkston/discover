package discover.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import discover.common.Version;
import discover.gui.Utilities;
import discover.gui.dialogs.GetPortDialog;
import discover.gui.frames.BinaryEditorFrame;
import discover.gui.frames.BulkEditorFrame;
import discover.gui.frames.DiscoverFrame;
import discover.system.Network;
import discover.system.PlaybackStatus;
import discover.system.PlaybackThread;
import discover.system.PlaybackThreadListener;
import discover.vdis.PDU;

/**
 * @author Tony Pinkston
 */
public class PlaybackTab extends PDUTab implements PlaybackThreadListener {

    private final JProgressBar progress = new JProgressBar(0, 100);
    private final JPopupMenu popup = new JPopupMenu();

    private final AbstractAction bulkAction = new BulkEditAction();
    private final AbstractAction binaryAction = new BinaryEditAction();
    private final AbstractAction playAction = new PlayAction();
    private final AbstractAction pauseAction = new PauseAction();
    private final AbstractAction stopAction = new StopAction();
    private final AbstractAction timeAction = new TimeAction();
    private final AbstractAction portAction = new PortAction();

    private PlaybackThread thread = null;
    private Integer port = null;
    private Boolean time = Boolean.TRUE;

    public PlaybackTab(String name) {

        super(name, TabType.PLAYBACK);

        progress.setStringPainted(true);

        playAction.setEnabled(false);
        pauseAction.setEnabled(false);
        stopAction.setEnabled(false);

        super.popup.addSeparator();
        super.popup.add(bulkAction);
        super.popup.add(binaryAction);

        fill();
        updateDuration();
    }

    @Override
    public void load(Version version, DataInputStream stream)
        throws IOException {

        super.load(version, stream);

        if (version.getValue() > Version.DISCOVER_V2.getValue()) {

            int value = stream.readInt();

            port = (value < 1024) ? null : new Integer(value);

            setPort(port, true);
        }
    }

    @Override
    public void save(DataOutputStream stream) throws IOException {

        super.save(stream);

        if (port == null) {

            stream.writeInt(0);
        }
        else {

            stream.writeInt(port.intValue());
        }
    }

    @Override
    public void close() {

    }

    /**
     * @param status - {@link PlaybackStatus}
     * @param percent - Percent complete
     */
    @Override
    public void sendStatus(PlaybackStatus status, int percent) {

        logger.info("Status: " + status);

        if (status == PlaybackStatus.STARTED) {

            enablePlayAction();
            pauseAction.setEnabled(true);
            stopAction.setEnabled(true);
            progress.setValue(percent);
        }
        else if (status == PlaybackStatus.SENDING) {

            progress.setValue(percent);
        }
        else if (status == PlaybackStatus.PAUSED) {

            if (thread.isPaused()) {

                playAction.setEnabled(true);
                pauseAction.setEnabled(false);
            }
            else {

                playAction.setEnabled(false);
                pauseAction.setEnabled(true);
            }
        }
        else if (status == PlaybackStatus.COMPLETE) {

            if (thread.getException() != null) {

                JOptionPane.showMessageDialog(
                    getPanel(),
                    "There was exception thrown during playback: " +
                    thread.getException().getClass().getSimpleName(),
                    "Outbound Playback",
                    JOptionPane.ERROR_MESSAGE);
            }

            thread = null;

            enablePlayAction();
            pauseAction.setEnabled(false);
            stopAction.setEnabled(false);
            progress.setValue(0);
        }
    }

    public Integer getPort() {

        return port;
    }

    public void setPort(Integer number, boolean force) {

        if (number != null) {

            if (force || !number.equals(port)) {

                port = number;
                portAction.putValue(Action.NAME, number.toString());
                enablePlayAction();
            }
        }
    }

    @Override
    public void delete() {

        if (thread != null) {

            modifyWarning();
        }
        else {

            lock.readLock().lock();
            int size = table.getRowCount();
            int selections[] = table.getSelectedRows();
            lock.readLock().unlock();

            logger.info(
                "Deleting " + selections.length + " of " + size + " " +
                getTabName() + " PDUs...");

            if (logger.isDebugEnabled()) {

                logger.debug("Selected rows: {}", Arrays.toString(selections));
            }

            for(int i = 0; i < selections.length; ++i) {

                selections[i] = sorter.convertRowIndexToModel(
                    selections[i]);
            }

            Arrays.sort(selections);

            if (logger.isDebugEnabled()) {

                logger.debug("Selected rows (sorted): {}", Arrays.toString(selections));
            }

            for(int i = (selections.length - 1); i >= 0; --i) {

                if (logger.isDebugEnabled()) {

                    logger.debug("Deleting PDU at index: {}", selections[i]);
                }

                cutPDU(selections[i]);
            }

            model.fireTableDataChanged();
            enablePlayAction();
            updateDuration();
            this.show(null);
        }
    }

    @Override
    public void deleteAll() {

        if (thread != null) {

            modifyWarning();
        }
        else if (getPDUCount() > 0) {

            int result = JOptionPane.showConfirmDialog(
                getPanel(),
                "Delete all playback PDUs (cannot be undone)?",
                getTabName(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {

                clearAll();
                model.fireTableDataChanged();
                enablePlayAction();
                updateDuration();
            }
        }
    }

    @Override
    public void cut(ArrayList<PDU> clipboard) {

        if (thread != null) {

            modifyWarning();
        }
        else {

            lock.readLock().lock();
            int size = table.getRowCount();
            int selections[] = table.getSelectedRows();
            lock.readLock().unlock();

            logger.info(
                "Cutting " + selections.length + " of " + size + " " +
                getTabName() + " PDUs...");

            if (logger.isDebugEnabled()) {

                logger.debug("Selected rows: {}", Arrays.toString(selections));
            }

            for(int i = 0; i < selections.length; ++i) {

                selections[i] = sorter.convertRowIndexToModel(
                    selections[i]);
            }

            Arrays.sort(selections);

            if (logger.isDebugEnabled()) {

                logger.debug("Selected rows (sorted): {}", Arrays.toString(selections));
            }

            for(int i = (selections.length - 1); i >= 0; --i) {

                logger.debug("Cutting PDU at index: {}", selections[i]);

                clipboard.add(cutPDU(selections[i]));
            }

            model.fireTableDataChanged();
            enablePlayAction();
            updateDuration();
        }
    }

    @Override
    public void copy(ArrayList<PDU> clipboard) {

        if (thread != null) {

            modifyWarning();
        }
        else {

            // Copy selected PDUs and add them to the list.
            lock.readLock().lock();
            int size = table.getRowCount();
            int selections[] = table.getSelectedRows();
            lock.readLock().unlock();

            logger.info(
                "Copying " + selections.length + " of " + size + " " +
                getTabName() + " PDUs...");

            logger.debug("Selected rows: {}", Arrays.toString(selections));

            for(int i = 0; i < selections.length; ++i) {

                int index = sorter.convertRowIndexToModel(selections[i]);

                logger.debug("Copying PDU at index: {}", index);

                clipboard.add(getPDU(index).copy());
            }
        }
    }

    @Override
    public void paste(ArrayList<PDU> clipboard) {

        if (clipboard.isEmpty()) {

            // Do nothing...
        }
        else if (thread != null) {

            modifyWarning();
        }
        else {

            logger.info(
                "Pasting " + clipboard.size() + " " +
                getTabName() + " PDUs...");

            lock.writeLock().lock();

            Iterator<PDU> iterator = clipboard.iterator();

            while(iterator.hasNext()) {

                list.add(iterator.next());
            }

            Collections.sort(list, new PDUComparator());

            lock.writeLock().unlock();

            clipboard.clear();

            model.fireTableDataChanged();
            enablePlayAction();
            updateDuration();
        }
    }

    public void modified(Object object) {

        if (object instanceof PDU) {

            this.refresh((PDU)object);
        }
        else if (Collection.class.isInstance(object)) {

            Collection<?> collection = (Collection<?>)object;

            for(Object item : collection) {

                if (item instanceof PDU) {

                    this.refresh((PDU)item);
                }
            }
        }
    }

    @Override
    protected boolean isEditable(int row, int column) {

        return (column == Column.TIME.ordinal());
    }

    @Override
    protected Object getCellValue(int row, int column) {

        if (column != Column.TIME.ordinal()) {

            return getCellValue(row, column);
        }
        else if (time.booleanValue()) {

            return getCellValue(row, column);
        }
        else {

            PDU pdu = getPDU(row);

            return Long.toString(pdu.getTime());
        }
    }

    @Override
    protected void setCellValue(int row, int column, Object object) {

        if (object != null) {

            if (column == Column.TIME.ordinal()) {

                try {

                    Long value = Long.parseLong(object.toString());
                    PDU pdu = getPDU(row);

                    if ((pdu != null) && (value != null)) {

                        pdu.setTime(value.longValue());

                        lock.writeLock().lock();

                        Collections.sort(list, new PDUComparator());

                        lock.writeLock().unlock();

                        model.fireTableDataChanged();
                        this.refresh(pdu);
                    }
                }
                catch(NumberFormatException exception) {

                    logger.error("Cannot parse date/time: {}", object);
                }
            }
        }
    }

    @Override
    protected void showPopup(MouseEvent event) {

        if (event.isPopupTrigger()) {

            int selections[] = table.getSelectedRows();

            if ((selections != null) && (selections.length > 0)) {

                binaryAction.setEnabled(selections.length == 1);

                super.popup.show(table, event.getX(), event.getY());
            }
        }
    }

    private void refresh(PDU pdu) {

        if (current == pdu) {

            show(pdu);
        }
    }

    private void updateDuration() {

        final int count = getPDUCount();

        switch(count) {

            case 0:
                progress.setString("0s");
                break;
            case 1:
                progress.setString("< 1s");
                break;
            default:
                long duration =
                    getPDU(count - 1).getTime() -
                    getPDU(0).getTime();

                duration = (duration / 1000);

                long seconds = (duration % 60);
                long minutes = ((duration - seconds) / 60);

                progress.setString(minutes + "m " + seconds + "s");
        }
    }

    private void enablePlayAction() {

        if (playAction != null) {

            if (getPDUCount() == 0) {

                playAction.setEnabled(false);
            }
            else if (getPort() == null) {

                playAction.setEnabled(false);
            }
            else if (thread == null) {

                playAction.setEnabled(true);
            }
            else {

                playAction.setEnabled(thread.isPaused());
            }
        }
    }

    private void modifyWarning() {

        JOptionPane.showMessageDialog(
            DiscoverFrame.getFrame(),
            "Cannot modify PDUs while playback is in progress.",
            "Delete All",
            JOptionPane.WARNING_MESSAGE);
    }

    private void fill() {

        JPanel status = new JPanel(new GridBagLayout());
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JTabbedPane tabbed = new JTabbedPane(JTabbedPane.BOTTOM);
        Insets insets = new Insets(3, 3, 3, 20);

        tabbed.add("Content", content.getPanel());
        tabbed.add("Byte View", hexadecimal.getPanel());

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

        insets = new Insets(3, 3, 3, 3);

        Utilities.addComponent(
            status,
            progress,
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

        tools.add(new FilterDialogAction());
        tools.addSeparator();
        tools.add(playAction);
        tools.add(pauseAction);
        tools.add(stopAction);
        tools.addSeparator();
        tools.add(timeAction);
        tools.addSeparator();
        tools.add(portAction);

        popup.add(binaryAction);
        popup.add(bulkAction);

        Utilities.addComponent(
            getPanel(),
            split,
            Utilities.BOTH,
            0, 0,
            1, 1,
            1.0, 1.0,
            null);

        split.setDividerLocation(0.9);
    }

    class PDUComparator implements Comparator<PDU> {

        @Override
        public int compare(PDU pdu1, PDU pdu2) {

            if (pdu1.getTime() < pdu2.getTime()) {

                return -1;
            }
            else if (pdu1.getTime() > pdu2.getTime()) {

                return 1;
            }
            else {

                return 0;
            }
        }
    }

    @SuppressWarnings("serial")
    class BulkEditAction extends AbstractAction {

        public BulkEditAction() {

            super("Open in Bulk Editor");

            putValue(
                Action.SHORT_DESCRIPTION,
                "Performs bulk edit of selected PDUs.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (thread != null) {

                modifyWarning();
            }
            else {

                BulkEditorFrame.create(PlaybackTab.this, getSelectedPDUs());
            }
        }
    }

    @SuppressWarnings("serial")
    class BinaryEditAction extends AbstractAction {

        public BinaryEditAction() {

            super("Open in Binary Editor");

            putValue(
                Action.SHORT_DESCRIPTION,
                "Edit single PDU (bit-wise).");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (thread != null) {

                modifyWarning();
            }
            else {

                BinaryEditorFrame.create(PlaybackTab.this, getSelectedPDU());
            }
        }
    }

    @SuppressWarnings("serial")
    class PlayAction extends AbstractAction {

        public PlayAction() {

            super("Play");

            putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("playback_play.png"));
            putValue(
                Action.SHORT_DESCRIPTION,
                "Starts or resumes playback of PDUs.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (port == null) {

                logger.error("Playback port is not set!");
            }
            else if (thread != null) {

                if (thread.isPaused()) {

                    thread.setPaused(false);
                }
                else {

                    logger.error(getTabName() + ": Already started sending!");
                }
            }
            else try {

                thread = new PlaybackThread(
                    getThreadName(getPort()),
                    Network.getPlaybackAddress(),
                    PlaybackTab.this,
                    getListCopy(),
                    getPort().intValue());

                thread.start();
            }
            catch(Exception exception) {

                logger.error("Caught exception!", exception);

                JOptionPane.showMessageDialog(
                    DiscoverFrame.getFrame(),
                    ("Caught " + exception.getClass().getSimpleName()),
                    "Start Playback",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("serial")
    class PauseAction extends AbstractAction {

        public PauseAction() {

            super("Pause");

            putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("playback_pause.png"));
            putValue(
                Action.SHORT_DESCRIPTION,
                "Pauses playback of PDU.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (thread != null) {

                thread.setPaused(true);
            }
        }
    }

    @SuppressWarnings("serial")
    class StopAction extends AbstractAction {

        public StopAction() {

            super("Stop Outgoing PDUs");

            putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("playback_stop.png"));
            putValue(
                Action.SHORT_DESCRIPTION,
                "Stops playback of PDUs.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (thread != null) {

                thread.setStopped(true);
            }
        }
    }

    @SuppressWarnings("serial")
    class TimeAction extends AbstractAction {

        public TimeAction() {

            super("Time");

            putValue(
                Action.SHORT_DESCRIPTION,
                "Click to toggle time column display (long or parsed).");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (time.booleanValue()) {

                time = Boolean.FALSE;
            }
            else {

                time = Boolean.TRUE;
            }

            model.fireTableDataChanged();
        }
    }

    @SuppressWarnings("serial")
    class PortAction extends AbstractAction {

        public PortAction() {

            super("Set Port");

            putValue(
                Action.SHORT_DESCRIPTION,
                "Sets the network port for PDU playback.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            if (thread != null) {

                JOptionPane.showMessageDialog(
                    DiscoverFrame.getFrame(),
                    "Cannot set port number while playback is in progress.",
                    "Playback Port",
                    JOptionPane.WARNING_MESSAGE);
            }
            else {

                TreeSet<Integer> ports = new TreeSet<Integer>();

                if (getPort() != null) {

                    ports.add(getPort());
                }

                GetPortDialog dialog = new GetPortDialog(
                    "Set Playback Port",
                    ports);

                setPort(dialog.getPort(), false);
            }
        }
    }
}
