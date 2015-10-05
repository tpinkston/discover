/**
 * @author Tony Pinkston
 */
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
import java.util.logging.Level;

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
        
        this.progress.setStringPainted(true);
        
        this.playAction.setEnabled(false);
        this.pauseAction.setEnabled(false);
        this.stopAction.setEnabled(false);
        
        super.popup.addSeparator();
        super.popup.add(this.bulkAction);
        super.popup.add(this.binaryAction);
        
        this.fill();
        this.updateDuration();
    }
    
    @Override
    public void load(Version version, DataInputStream stream) 
        throws IOException {
        
        super.load(version, stream);
        
        if (version.getValue() > Version.DISCOVER_V2.getValue()) {
            
            int value = stream.readInt();
            
            this.port = (value < 1024) ? null : new Integer(value);
            
            this.setPort(this.port, true);
        }
    }
    
    @Override
    public void save(DataOutputStream stream) throws IOException {

        super.save(stream);
        
        if (this.port == null) {
            
            stream.writeInt(0);
        }
        else {
            
            stream.writeInt(this.port.intValue());
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

            this.enablePlayAction();
            this.pauseAction.setEnabled(true);
            this.stopAction.setEnabled(true);
            this.progress.setValue(percent);
        }
        else if (status == PlaybackStatus.SENDING) {

            this.progress.setValue(percent);
        }
        else if (status == PlaybackStatus.PAUSED) {

            if (this.thread.isPaused()) {
                
                this.playAction.setEnabled(true);
                this.pauseAction.setEnabled(false);
            }
            else {
                
                this.playAction.setEnabled(false);
                this.pauseAction.setEnabled(true);
            }
        }
        else if (status == PlaybackStatus.COMPLETE) {

            if (this.thread.getException() != null) {
                
                JOptionPane.showMessageDialog(
                    super.getPanel(),
                    "There was exception thrown during playback: " +
                    this.thread.getException().getClass().getSimpleName(),
                    "Outbound Playback",
                    JOptionPane.ERROR_MESSAGE);
            }
            
            this.thread = null;
            
            this.enablePlayAction();
            this.pauseAction.setEnabled(false);
            this.stopAction.setEnabled(false);
            this.progress.setValue(0);
        }
    }

    public Integer getPort() {
        
        return this.port;
    }
    
    public void setPort(Integer number, boolean force) {
        
        if (number != null) {
            
            if (force || !number.equals(this.port)) {
                
                this.port = number;
                this.portAction.putValue(Action.NAME, number.toString());
                this.enablePlayAction();
            }
        }
    }

    @Override
    public void delete() {
        
        if (this.thread != null) {
            
            this.modifyWarning();
        }
        else {
            
            this.lock.readLock().lock();
            int size = this.table.getRowCount();
            int selections[] = this.table.getSelectedRows();
            this.lock.readLock().unlock();
            
            logger.info(
                "Deleting " + selections.length + " of " + size + " " + 
                super.getTabName() + " PDUs...");
            
            if (logger.isLoggable(Level.FINER)) {
                
                logger.finer(
                    "Selected rows: " + 
                    Arrays.toString(selections));
            }
            
            for(int i = 0; i < selections.length; ++i) {
                
                selections[i] = super.sorter.convertRowIndexToModel(
                    selections[i]);
            }
            
            Arrays.sort(selections);

            if (logger.isLoggable(Level.FINER)) {
                
                logger.finer(
                    "Selected rows (sorted): " + 
                    Arrays.toString(selections));
            }

            for(int i = (selections.length - 1); i >= 0; --i) {
                
                if (logger.isLoggable(Level.FINEST)) {
                    
                    logger.finest("Deleting PDU at index: " + selections[i]);
                }
                
                super.cutPDU(selections[i]);
            }
            
            this.model.fireTableDataChanged();
            this.enablePlayAction();
            this.updateDuration();
            this.show(null);
        }
    }
    
    @Override
    public void deleteAll() {
        
        if (this.thread != null) {
            
            this.modifyWarning();
        }
        else if (super.getPDUCount() > 0) {
            
            int result = JOptionPane.showConfirmDialog(
                getPanel(), 
                "Delete all playback PDUs (cannot be undone)?", 
                super.getTabName(), 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
            
            if (result == JOptionPane.YES_OPTION) {
                
                super.clearAll();
                super.model.fireTableDataChanged();
                this.enablePlayAction();
                this.updateDuration();
            }
        }
    }
    
    @Override
    public void cut(ArrayList<PDU> clipboard) {
        
        if (this.thread != null) {
            
            this.modifyWarning();
        }
        else {
            
            this.lock.readLock().lock();
            int size = this.table.getRowCount();
            int selections[] = this.table.getSelectedRows();
            this.lock.readLock().unlock();
            
            logger.info(
                "Cutting " + selections.length + " of " + size + " " + 
                super.getTabName() + " PDUs...");
            
            if (logger.isLoggable(Level.FINER)) {
                
                logger.finer(
                    "Selected rows: " + 
                    Arrays.toString(selections));
            }
            
            for(int i = 0; i < selections.length; ++i) {
                
                selections[i] = this.sorter.convertRowIndexToModel(
                    selections[i]);
            }
            
            Arrays.sort(selections);

            if (logger.isLoggable(Level.FINER)) {
                
                logger.finer(
                    "Selected rows (sorted): " + 
                    Arrays.toString(selections));
            }

            for(int i = (selections.length - 1); i >= 0; --i) {
                
                logger.finest("Cutting PDU at index: " + selections[i]);
                
                clipboard.add(super.cutPDU(selections[i]));
            }
            
            super.model.fireTableDataChanged();
            this.enablePlayAction();
            this.updateDuration();
        }
    }

    @Override
    public void copy(ArrayList<PDU> clipboard) {
        
        if (this.thread != null) {
            
            this.modifyWarning();
        }
        else {
            
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
    }
    
    @Override
    public void paste(ArrayList<PDU> clipboard) {
        
        if (clipboard.isEmpty()) {
            
            // Do nothing...
        }
        else if (this.thread != null) {
            
            this.modifyWarning();
        }
        else {
            
            logger.info(
                "Pasting " + clipboard.size() + " " + 
                super.getTabName() + " PDUs...");

            this.lock.writeLock().lock();
            
            Iterator<PDU> iterator = clipboard.iterator();

            while(iterator.hasNext()) {
                
                super.list.add(iterator.next());
            }
            
            Collections.sort(super.list, new PDUComparator());
            
            this.lock.writeLock().unlock();

            clipboard.clear();
            
            this.model.fireTableDataChanged();
            this.enablePlayAction();
            this.updateDuration();
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
            
            return super.getCellValue(row, column);
        }
        else if (this.time.booleanValue()) {
            
            return super.getCellValue(row, column);
        }
        else {
            
            PDU pdu = this.getPDU(row);
            
            return Long.toString(pdu.getTime());
        }
    }

    @Override
    protected void setCellValue(int row, int column, Object object) {
        
        if (object != null) {
            
            if (column == Column.TIME.ordinal()) {
                
                try {
                    
                    Long value = Long.parseLong(object.toString());
                    PDU pdu = super.getPDU(row);

                    if ((pdu != null) && (value != null)) {
                        
                        pdu.setTime(value.longValue());

                        this.lock.writeLock().lock();
                        
                        Collections.sort(super.list, new PDUComparator());
                        
                        this.lock.writeLock().unlock();

                        this.model.fireTableDataChanged();
                        this.refresh(pdu);
                    }
                } 
                catch(NumberFormatException exception) {

                    logger.severe("Cannot parse date/time: " + object);
                }
            }
        }
    }

    @Override
    protected void showPopup(MouseEvent event) {
        
        if (event.isPopupTrigger()) {
            
            int selections[] = super.table.getSelectedRows();

            if ((selections != null) && (selections.length > 0)) {

                this.binaryAction.setEnabled(selections.length == 1);
                
                super.popup.show(this.table, event.getX(), event.getY());
            }
        }
    }
    
    private void refresh(PDU pdu) {
        
        if (super.current == pdu) {
            
            super.show(pdu);
        }
    }

    private void updateDuration() {
        
        final int count = super.getPDUCount();
        
        switch(count) {
            
            case 0:
                this.progress.setString("0s");
                break;
            case 1:
                this.progress.setString("< 1s");
                break;
            default:
                long duration =
                    super.getPDU(count - 1).getTime() -
                    super.getPDU(0).getTime();
                
                duration = (duration / 1000);
                
                long seconds = (duration % 60);
                long minutes = ((duration - seconds) / 60);

                this.progress.setString(minutes + "m " + seconds + "s");
        }
    }
    
    private void enablePlayAction() {
        
        if (this.playAction != null) {
            
            if (super.getPDUCount() == 0) {

                this.playAction.setEnabled(false);
            }
            else if (this.getPort() == null) {
                
                this.playAction.setEnabled(false);
            }
            else if (this.thread == null) {
                
                this.playAction.setEnabled(true);
            }
            else {
                
                this.playAction.setEnabled(this.thread.isPaused());
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
        
        insets = new Insets(3, 3, 3, 3);
        
        Utilities.addComponent(
            status, 
            this.progress, 
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
        
        super.tools.add(new FilterDialogAction());
        super.tools.addSeparator();
        super.tools.add(this.playAction);
        super.tools.add(this.pauseAction);
        super.tools.add(this.stopAction);
        super.tools.addSeparator();
        super.tools.add(this.timeAction);
        super.tools.addSeparator();
        super.tools.add(this.portAction);
        
        this.popup.add(this.binaryAction);
        this.popup.add(this.bulkAction);

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
            
            super.putValue(
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
            
            super.putValue(
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
            
            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("playback_play.png"));
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Starts or resumes playback of PDUs.");
        }
        
        @Override
        public void actionPerformed(ActionEvent event) {

            if (port == null) {
                
                logger.severe("Playback port is not set!");
            }
            else if (thread != null) {
                
                if (thread.isPaused()) {
                    
                    thread.setPaused(false);
                }
                else {
                    
                    logger.severe(getTabName() + ": Already started sending!");
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
                
                logger.log(Level.SEVERE, "Caught exception!", exception);
                
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
            
            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("playback_pause.png"));
            super.putValue(
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
            
            super.putValue(
                Action.SMALL_ICON,
                Utilities.getImageIcon("playback_stop.png"));
            super.putValue(
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

            super.putValue(
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

            super.putValue(
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
