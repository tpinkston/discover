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
import discover.vdis.Enumerations;
import discover.vdis.PDU;
import discover.vdis.common.EntityId;
import discover.vdis.enums.PDU_TYPE;

/**
 * @author Tony Pinkston
 */
@SuppressWarnings("serial")
public class SiteMapFrame
        extends JFrame
        implements MouseListener, TreeSelectionListener {

    private static final Logger logger = LoggerFactory.getLogger(SiteMapFrame.class);

    private static final Icon WORLD_ICON = Utilities.getImageIcon("world.png");
    private static final Icon PERSON_ICON = Utilities.getImageIcon("person.png");
    private static final Icon MACHINE_ICON = Utilities.getImageIcon("machine.png");
    private static final Icon TERMINAL_ICON = Utilities.getImageIcon("terminal.png");

    private static final String SITE_MAP = "Site Tree";

    private static final Dimension TREE_PREFERRED = new Dimension(300, 500);

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

        popup = new JPopupMenu();
        root = new World();
        tree = new JTree(root);
        description = new JEditorPane();
        description.setContentType("text/html");

        popup.add(clear);
        popup.add(remove);

        tree.setRootVisible(true);
        tree.setCellRenderer(new TreeCellRenderer());
        tree.setRowHeight(20);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addMouseListener(this);
        tree.addTreeSelectionListener(this);

        fill();
        setTitle(title);

        setMinimumSize(new Dimension(600, 500));
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
    }

    public void destroy() {

        tree.removeMouseListener(this);
        tree.removeTreeSelectionListener(this);
        dispose();
    }

    @Override
    public void setTitle(String title) {

        super.setTitle("Site Map [" + title + "]");
    }

    public void clearAll() {

        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        List<TreeNode> nodes = new ArrayList<TreeNode>();

        int count = root.getChildCount();

        for(int i = 0; i < count; ++i) {

            nodes.add((TreeNode)root.getChildAt(i));
        }

        for(TreeNode node : nodes) {

            logger.info("Removing node \"" + node.toString() + "\"");
            model.removeNodeFromParent(node);
        }

        logger.info("Clearing node \"" + root.toString() + "\"");

        root.clear();
        selection = root;
        updateSelection(root);
    }

    /**
     * Processes tree node selection by the user.
     */
    @Override
    public void valueChanged(TreeSelectionEvent event) {

        Object path[] = event.getPath().getPath();

        if ((path == null) || (path.length == 0)) {

            description.setText("");
        }
        else {

            selection = (TreeNode)path[path.length - 1];

            logger.info("Selection: " + selection.toString());

            showNode(selection);
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

        showPopup(event);
    }

    @Override
    public void mousePressed(MouseEvent event) {

        showPopup(event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {

        showPopup(event);
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

        if (selection != null) {

            if (selection.isRoot() || (selection == node)) {

                showNode(selection);
            }
            else if ((selection instanceof Application) ||
                     (selection instanceof Site)) {

                if (selection.isChildNode(node)) {

                    showNode(selection);
                }
            }
        }
    }

    private void insertNodeInto(TreeNode child, TreeNode parent, int index) {

        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();

        logger.debug("Inserting node '{}' into '{}'", child, parent);

        model.insertNodeInto(child, parent, index);
    }

    private void removeNodeFromParent(TreeNode node) {

        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();

        model.removeNodeFromParent(node);
    }

    private void showNode(TreeNode node) {

        HypertextBuffer buffer = new HypertextBuffer();

        buffer.addText("<html><body>");
        buffer.addBuffer(node);
        buffer.addText("</body></html>");

        description.setText(buffer.toString());
    }

    private void showPopup(MouseEvent event) {

        if (event.isPopupTrigger()) {

            final int row = tree.getRowForLocation(
                event.getX(),
                event.getY());

            final int rows[] = tree.getSelectionRows();

            if ((rows != null) && (rows.length == 1) && (rows[0] == row)) {

                TreePath path = tree.getPathForLocation(
                    event.getX(),
                    event.getY());

                TreeNode node = (TreeNode)path.getLastPathComponent();

                clear.setNode(node);
                remove.setNode(node);
                popup.show(tree, event.getX(), event.getY());
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

        TreeNode site = root.getChildNode(id.getSite());

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

        JScrollPane scroller = new JScrollPane(tree);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

        scroller.setPreferredSize(TREE_PREFERRED);

        split.setContinuousLayout(true);
        split.setLeftComponent(scroller);
        split.setRightComponent(description);

        add(split, BorderLayout.CENTER);
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
                SiteMapFrame.this,
                "Clear all PDU totals for " + node.toString() + "?",
                "Site Map",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {

                node.clear();
                updateSelection(node);
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
                SiteMapFrame.this,
                "Remove " + node.toString() + " and sub-nodes?",
                node.getClass().getSimpleName(),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {

                removeNodeFromParent(node);
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
                "APPLICATION (" + getParentNode().getNumber() +
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
                "ENTITY (" + getParentNode().getParentNode().getNumber() +
                ", " + getParentNode().getNumber() +
                ", " + super.getNumber() +")");

            super.toBuffer(buffer);
        }
    }

    /**
     * Tree node representing either the root, site, application or entity node.
     */
    abstract class TreeNode extends DefaultMutableTreeNode implements Bufferable {

        // TODO: Use PDU_TYPE as key...
        private final Map<Integer, Integer> published;
        private final int number;

        protected TreeNode(int number) {

            published = new TreeMap<Integer, Integer>();
            this.number = number;

            logger.info("Created node \"" + toString() + "\"");
        }

        public final int getNumber() { return number; }

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

                if (isRoot()) {

                    node = new Site(number);
                }
                else if (isSite()) {

                    node = new Application(number);
                }
                else if (isApplication()) {

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

            Integer count = published.get(type);

            if (isEntity()) {

                logger.debug("Adding PDU ({}) for node: {}", type, toString());
            }

            if (count == null) {

                published.put(type, new Integer(1));
            }
            else {

                published.put(type, new Integer(count.intValue() + 1));
            }

            TreeNode parent = getParentNode();

            if (parent != null) {

                parent.publishedPDU(type);
            }
        }

        public final void clear() {

            logger.info("Clearing node \"" + toString() + "\"");

            published.clear();

            for(int i = 0; i < super.getChildCount(); ++i) {

                TreeNode child = (TreeNode)super.getChildAt(i);

                child.clear();
            }
        }

        @Override
        public final String toString() {

            if (isRoot()) {

                return SITE_MAP;
            }
            else {

                return (this.getClass().getSimpleName() + ": " + number);
            }
        }

        @Override
        public void toBuffer(AbstractBuffer buffer) {

            int total = 0;

            buffer.addBreak();
            buffer.addTitle("Published PDUs:");

            for(Integer type : published.keySet()) {

                int count = published.get(type).intValue();

                total += count;

                buffer.addAttribute(
                    Enumerations.getDescription(type, PDU_TYPE.class),
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
