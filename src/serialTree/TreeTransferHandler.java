package serialTree;

import javax.swing.*;
import serialTree.Tree.TreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.*;

public class TreeTransferHandler extends TransferHandler {
    DataFlavor nodesFlavor;
    DataFlavor[] flavors = new DataFlavor[1];
    Tree.TreeNode[] nodesToRemove;

    public TreeTransferHandler() {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                    ";class=\"" +
                    Tree.TreeNode[].class.getName() +
                    "\"";
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }

    public boolean canImport(TransferHandler.TransferSupport support) {
        if(!support.isDrop()) {
            return false;
        }
        support.setShowDropLocation(true);
        if(!support.isDataFlavorSupported(nodesFlavor)) {
            return false;
        }
        // Do not allow a drop on the drag source selections.
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        JTree tree = (JTree)support.getComponent();
        int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        for(int i = 0; i < selRows.length; i++) {
            if(selRows[i] == dropRow) {
                return false;
            }
            Tree.TreeNode treeNode =
                    (Tree.TreeNode)tree.getPathForRow(selRows[i]).getLastPathComponent();
            for (javax.swing.tree.TreeNode offspring: Collections.list(treeNode.depthFirstEnumeration())) {
                if (tree.getRowForPath(new TreePath(((Tree.TreeNode)offspring).getPath())) == dropRow) {
                    return false;
                }
            }
        }
        return true;
    }

    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree) c;
        TreePath[] paths = tree.getSelectionPaths();
        if (paths == null) {
            return null;
        }
        // Make up a node array of copies for transfer and
        // another for/of the nodes that will be removed in
        // exportDone after a successful drop.
        List<Tree.TreeNode> copies =
                new ArrayList<Tree.TreeNode>();
        List<Tree.TreeNode> toRemove =
                new ArrayList<Tree.TreeNode>();
        Tree.TreeNode firstNode =
                (Tree.TreeNode) paths[0].getLastPathComponent();
        HashSet<TreeNode> doneItems = new LinkedHashSet<>(paths.length);
        Tree.TreeNode copy = copy(firstNode, doneItems, tree);
        copies.add(copy);
        toRemove.add(firstNode);
        for (int i = 1; i < paths.length; i++) {
            Tree.TreeNode next =
                    (Tree.TreeNode) paths[i].getLastPathComponent();
            if (doneItems.contains(next)) {
                continue;
            }
            // Do not allow higher level nodes to be added to list.
            if (next.getLevel() < firstNode.getLevel()) {
                break;
            } else if (next.getLevel() > firstNode.getLevel()) {  // child node
                copy.add(copy(next, doneItems, tree));
                // node already contains child
            } else {                                        // sibling
                copies.add(copy(next, doneItems, tree));
                toRemove.add(next);
            }
            doneItems.add(next);
        }
        Tree.TreeNode[] nodes =
                copies.toArray(new Tree.TreeNode[copies.size()]);
        nodesToRemove =
                toRemove.toArray(new Tree.TreeNode[toRemove.size()]);
        return new NodesTransferable(nodes);
    }

    private Tree.TreeNode copy(Tree.TreeNode node, HashSet<TreeNode> doneItems, JTree tree) {
        Tree.TreeNode copy = new Tree.TreeNode<>(node.getUserObject());
        if (!(copy.isLeaf())) {
            copy.nodeId = Main.treeExample.tree.counter;
            Main.treeExample.tree.counter++;
        }
        doneItems.add(node);
        for (int i=0; i<node.getChildCount(); i++) {
            copy.add(copy((Tree.TreeNode) node.getChildAt(i), doneItems, tree));
        }
        int row = tree.getRowForPath(new TreePath(copy.getPath()));
        tree.expandRow(row);
        return copy;
    }

    protected void exportDone(JComponent source, Transferable data, int action) {
        if((action & MOVE) == MOVE) {
            JTree tree = (JTree)source;
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            // Remove nodes saved in nodesToRemove in createTransferable.
            for(int i = 0; i < nodesToRemove.length; i++) {
                model.removeNodeFromParent(nodesToRemove[i]);
            }
        }
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        if(!canImport(support)) {
            return false;
        }
        // Extract transfer data.
        Tree.TreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (Tree.TreeNode[])t.getTransferData(nodesFlavor);
        } catch(UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch(java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
        // Get drop location info.
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        Tree.TreeNode parent =
                (Tree.TreeNode)dest.getLastPathComponent();
        JTree tree = (JTree)support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // Configure for drop mode.
        int index = childIndex;    // DropMode.INSERT
        if(childIndex == -1) {     // DropMode.ON
            index = parent.getChildCount();
        }
        // Add data to model.
        for(int i = 0; i < nodes.length; i++) {
            model.insertNodeInto(nodes[i], parent, index++);
        }
        return true;
    }

    public String toString() {
        return getClass().getName();
    }

    public class NodesTransferable implements Transferable {
        Tree.TreeNode[] nodes;

        public NodesTransferable(Tree.TreeNode[] nodes) {
            this.nodes = nodes;
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {
            if(!isDataFlavorSupported(flavor))
                throw new UnsupportedFlavorException(flavor);
            return nodes;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}