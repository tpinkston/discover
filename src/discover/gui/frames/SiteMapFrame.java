/**
 * @author Tony Pinkston
 */
package discover.gui.frames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.common.buffer.HypertextBuffer;
import discover.gui.Utilities;
import discover.vdis.PDU;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

@SuppressWarnings("serial")
public class SiteMapFrame implements MouseListener, TreeSelectionListener {

    private static final Logger logger = LoggerFactory.getLogger(SiteMapFrame.class);

    private static final Icon WORLD_ICON = Utilities.getImageIcon("world.png");
    private static final Icon PERSON_ICON = Utilities.getImageIcon("person.png");
    private static final Icon MACHINE_ICON = Utilities.getImageIcon("machine.png");
    private static final Icon TERMINAL_ICON = Utilities.getImageIcon("terminal.png");

    private static final String SITE_MAP = "Site Tree";

    private static final Dimension TREE_PREFERRED = new Dimension(300, 500);

    private final JFrame frame;
    private final JTree tree;
    private final JPopupMenu popup;
    private final TreeNode root;
    private final JEditorPane description;

    private final ClearAction clear = new ClearAction();
    private final RemoveAction remove = new RemoveAction();

    private TreeNode selection = null;

    /**
     * Constructor.
     */
    public SiteMapFrame(String title) {

        this.frame = new JFrame();
        this.popup = new JPopupMenu();
        this.root = new World();
        this.tree = new JTree(this.root);
        this.description = new JEditorPane();
        this.description.setContentType("text/html");

        this.popup.add(this.clear);
        this.popup.add(this.remove);

        this.tree.setRootVisible(true);
        this.tree.setCellRenderer(new TreeCellRenderer());
        this.tree.setRowHeight(20);
        this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.tree.addMouseListener(this);
        this.tree.addTreeSelectionListener(this);

        this.fill();
        this.setTitle(title);

        this.frame.setMinimumSize(new Dimension(600, 500));
        this.frame.setPreferredSize(new Dimension(800, 600));
        this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.frame.pack();
    }

    public JFrame getFrame() {

        return this.frame;
    }

    public void destroy() {

        this.tree.removeMouseListener(this);
        this.tree.removeTreeSelectionListener(this);
        this.frame.dispose();
    }

    public void setTitle(String title) {

        this.frame.setTitle("Site Map [" + title + "]");
    }

    public void clearAll() {

        DefaultTreeModel model = (DefaultTreeModel)this.tree.getModel();
        List<TreeNode> nodes = new ArrayList<TreeNode>();

        int count = this.root.getChildCount();

        for(int i = 0; i < count; ++i) {

            nodes.add((TreeNode)this.root.getChildAt(i));
        }

        for(TreeNode node : nodes) {

            logger.info("Removing node \"" + node.toString() + "\"");
            model.removeNodeFromParent(node);
        }

        logger.info("Clearing node \"" + this.root.toString() + "\"");

        this.root.clear();
        this.selection = this.root;
        updateSelection(this.root);
    }

    /**
     * Processes tree node selection by the user.
     */
    @Override
    public void valueChanged(TreeSelectionEvent event) {

        Object path[] = event.getPath().getPath();

        if ((path == null) || (path.length == 0)) {

            this.description.setText("");
        }
        else {

            this.selection = (TreeNode)path[path.length - 1];

            logger.info("Selection: " + this.selection.toString());

            this.showNode(this.selection);
        }
    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseExited(MouseEvent event) {

    }

    @Override
    public void mouseClicked(MouseEvent event) {

        this.showPopup(event);
    }

    @Override
    public void mousePressed(MouseEvent event) {

        this.showPopup(event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {

        this.showPopup(event);
    }

    /**
     * Processes captured PDUs.
     */
    public void processPDUs(List<PDU> pdus) {

        final List<PDU> list = new ArrayList<PDU>(pdus);

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                for(PDU pdu : list) {

                    EntityId id = pdu.getId();
                    int type = pdu.getType();

                    if (id != null) {

                        TreeNode node = getEntity(id);

                        if (node == null) {

                            root.publishedPDU(type);
                            updateSelection(root);
                        }
                        else {

                            node.publishedPDU(type);
                            updateSelection(node);
                        }
                    }
                }
            }
        });
    }

    private void updateSelection(TreeNode node) {

        if (this.selection != null) {

            if (this.selection.isRoot() || (this.selection == node)) {

                this.showNode(this.selection);
            }
            else if ((this.selection instanceof Application) ||
                     (this.selection instanceof Site)) {

                if (this.selection.isChildNode(node)) {

                    this.showNode(this.selection);
                }
            }
        }
    }

    private void insertNodeInto(TreeNode child, TreeNode parent, int index) {

        DefaultTreeModel model = (DefaultTreeModel)this.tree.getModel();

        logger.debug("Inserting node '{}' into '{}'", child, parent);

        model.insertNodeInto(child, parent, index);
    }

    private void removeNodeFromParent(TreeNode node) {

        DefaultTreeModel model = (DefaultTreeModel)this.tree.getModel();

        model.removeNodeFromParent(node);
    }

    private void showNode(TreeNode node) {

        HypertextBuffer buffer = new HypertextBuffer();

        buffer.addText("<html><body>");
        buffer.addBuffer(node);
        buffer.addText("</body></html>");

        this.description.setText(buffer.toString());
    }

    private void showPopup(MouseEvent event) {

        if (event.isPopupTrigger()) {

            final int row = this.tree.getRowForLocation(
                event.getX(),
                event.getY());

            final int rows[] = this.tree.getSelectionRows();

            if ((rows != null) && (rows.length == 1) && (rows[0] == row)) {

                TreePath path = this.tree.getPathForLocation(
                    event.getX(),
                    event.getY());

                TreeNode node = (TreeNode)path.getLastPathComponent();

                this.clear.setNode(node);
                this.remove.setNode(node);
                this.popup.show(this.tree, event.getX(), event.getY());
            }
        }
    }

    /**
     * Retrieves the Entity object from the tree.
     *
     * @param id - {@link EntityId}
     *
     * @return {@link Entity}
     */
    private Entity getEntity(EntityId id) {

        TreeNode site = this.root.getChildNode(id.getSite());

        if (site == null) {

            logger.error("Site not found for {}", id);
        }
        else {

            TreeNode application = site.getChildNode(id.getApplication());

            if (application == null) {

                logger.error("Application not found for {}", id);
            }
            else {

                TreeNode entity = application.getChildNode(id.getEntity());

                if (entity == null) {

                    logger.error("Entity not found for {}", id);
                }
                else {

                    return (Entity)entity;
                }
            }
        }

        return null;
    }

    /**
     * Fills frame with GUI components.
     */
    private void fill() {

        JScrollPane scroller = new JScrollPane(this.tree);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

        scroller.setPreferredSize(TREE_PREFERRED);

        split.setContinuousLayout(true);
        split.setLeftComponent(scroller);
        split.setRightComponent(this.description);

        this.frame.add(split, BorderLayout.CENTER);
    }

    class ClearAction extends AbstractAction {

        TreeNode node = null;

        public ClearAction() {

            super("Clear");

            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Clears info for selected node and all of its sub-nodes.");
        }

        public void setNode(TreeNode node) {

            this.node = node;
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            int choice = JOptionPane.showConfirmDialog(
                SiteMapFrame.this.frame,
                "Clear all PDU totals for " + this.node.toString() + "?",
                "Site Map",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {

                this.node.clear();
                updateSelection(this.node);
            }
        }
    }

    class RemoveAction extends AbstractAction {

        TreeNode node = null;

        public RemoveAction() {

            super("Remove");

            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Removes selected node and all of its sub-nodes.");
        }

        public void setNode(TreeNode node) {

            // Cannot delete the root node!
            this.node = ((node instanceof World) ? null : node);
            super.setEnabled(this.node != null);
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            int choice = JOptionPane.showConfirmDialog(
                SiteMapFrame.this.frame,
                "Remove " + this.node.toString() + " and sub-nodes?",
                this.node.getClass().getSimpleName(),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {

                removeNodeFromParent(this.node);
            }
        }
    }

    final class World extends TreeNode {

        public World() { super(-1); }

        @Override
        public final void toBuffer(AbstractBuffer buffer) {

            buffer.addTitle("ALL");

            super.toBuffer(buffer);
        }
    }

    /**
     * Represents a DIS site.
     */
    final class Site extends TreeNode {

        public Site(int number) { super(number); }

        @Override
        public final void toBuffer(AbstractBuffer buffer) {

            buffer.addTitle("SITE (" + super.getNumber() + ", #, #)");

            super.toBuffer(buffer);
        }
    }

    /**
     * Represents a DIS application under a specific site.
     */
    final class Application extends TreeNode {

        public Application(int number) { super(number); }

        @Override
        public final void toBuffer(AbstractBuffer buffer) {

            buffer.addTitle(
                "APPLICATION (" + this.getParentNode().getNumber() +
                ", " + super.getNumber() +", #)");

            super.toBuffer(buffer);
        }
    }

    /**
     * Represents a DIS entity under a specific application.
     */
    final class Entity extends TreeNode {

        public Entity(int number) { super(number); }

        @Override
        public final void toBuffer(AbstractBuffer buffer) {

            buffer.addTitle(
                "ENTITY (" + this.getParentNode().getParentNode().getNumber() +
                ", " + this.getParentNode().getNumber() +
                ", " + super.getNumber() +")");

            super.toBuffer(buffer);
        }
    }

    /**
     * Tree node representing either the root, site, application or entity node.
     */
    abstract class TreeNode extends DefaultMutableTreeNode implements Bufferable {

        private final Map<Integer, Integer> published;
        private final int number;

        protected TreeNode(int number) {

            this.published = new TreeMap<Integer, Integer>();
            this.number = number;

            logger.info("Created node \"" + this.toString() + "\"");
        }

        public final int getNumber() { return this.number; }

        @Override
        public final boolean isRoot() {

            return (this instanceof World);
        }

        public final boolean isSite() {

            return (this instanceof Site);
        }

        public final boolean isApplication() {

            return (this instanceof Application);
        }

        public final boolean isEntity() {

            return (this instanceof Entity);
        }

        public final boolean isChildNode(TreeNode node) {

            for(int i = 0, count = super.getChildCount(); i < count; ++i) {

                TreeNode child = (TreeNode)super.getChildAt(i);

                if (node == child) {

                    return true;
                }
                else if (child.isChildNode(node)) {

                    return true;
                }
            }

            return false;
        }

        public final TreeNode getParentNode() {

            return (TreeNode)super.getParent();
        }

        public final TreeNode getChildNode(int number) {

            final int count = super.getChildCount();
            TreeNode node = null;

            for(int i = 0; i < count; ++i) {

                TreeNode child = (TreeNode)super.getChildAt(i);

                if (number == child.getNumber()) {

                    node = child;
                }
            }

            if (node == null) {

                // Node not yet created, create a new one.
                boolean inserted = false;

                if (this.isRoot()) {

                    node = new Site(number);
                }
                else if (this.isSite()) {

                    node = new Application(number);
                }
                else if (this.isApplication()) {

                    node = new Entity(number);
                }
                else {

                    logger.error("Cannot create child node!");
                }

                if (node != null) {

                    if (count == 0) {

                        insertNodeInto(node, this, 0);
                    }
                    else for(int i = 0; (i < count) && !inserted; ++i) {

                        TreeNode child = (TreeNode)super.getChildAt(i);

                        if (number < child.getNumber()) {

                          insertNodeInto(node, this, i);
                          inserted = true;
                        }
                    }

                    if (!inserted) {

                        insertNodeInto(node, this, count);
                        inserted = true;
                    }
                }

                if (!inserted) {

                    logger.error("Failed to insert node for number {}", number);
                }
            }

            return node;
        }

        public final void publishedPDU(int type) {

            Integer count = this.published.get(type);

            if (this.isEntity()) {

                logger.debug("Adding PDU ({}) for node: {}", type, toString());
            }

            if (count == null) {

                this.published.put(type, new Integer(1));
            }
            else {

                this.published.put(type, new Integer(count.intValue() + 1));
            }

            TreeNode parent = this.getParentNode();

            if (parent != null) {

                parent.publishedPDU(type);
            }
        }

        public final void clear() {

            logger.info("Clearing node \"" + this.toString() + "\"");

            this.published.clear();

            for(int i = 0; i < super.getChildCount(); ++i) {

                TreeNode child = (TreeNode)super.getChildAt(i);

                child.clear();
            }
        }

        @Override
        public final String toString() {

            if (this.isRoot()) {

                return SITE_MAP;
            }
            else {

                return (this.getClass().getSimpleName() + ": " + this.number);
            }
        }

        @Override
        public void toBuffer(AbstractBuffer buffer) {

            int total = 0;

            buffer.addBreak();
            buffer.addTitle("Published PDUs:");

            for(Integer type : this.published.keySet()) {

                int count = this.published.get(type).intValue();

                total += count;

                buffer.addAttribute(
                    VDIS.getDescription(VDIS.PDU_TYPE, type),
                    count);
            }

            buffer.addBreak();
            buffer.addAttribute("TOTAL", total);
        }
    }

    /**
     * Tree node renderer.
     */
    class TreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(
            JTree tree,
            Object object,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean focus) {

            JLabel label = new JLabel(object.toString());

            if (object instanceof World) {

                label.setIcon(WORLD_ICON);
            }
            else if (object instanceof Site) {

                label.setIcon(MACHINE_ICON);
            }
            else if (object instanceof Application) {

                label.setIcon(TERMINAL_ICON);
            }
            else if (object instanceof Entity) {

                label.setIcon(PERSON_ICON);
            }

            return label;
        }
    }
}
