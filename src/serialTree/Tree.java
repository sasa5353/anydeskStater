package serialTree;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.tree.*;

public class Tree<T> extends JTree implements Serializable {
//    @Serial
    private static final long serialVersionUID = 1L;
    public long counter;
    public boolean isExpanded;

//    public Tree(T rootUserObj){
//        this(new TreeNode<>(rootUserObj));
//    }

    public Tree(TreeNode<T> root) {
        super(root);
        this.counter=1;
        root.nodeId=0;
        this.isExpanded=true;

//        this.setPreferredSize(new Dimension(150, 0));
//      this.setRootVisible(false);
//        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//        this.setExpandsSelectedPaths(true);
    }
    public void makeItDraggable(){
        this.setRootVisible(true);
        this.setDragEnabled(true);
        this.setDropMode(DropMode.ON_OR_INSERT);
        this.setTransferHandler(new TreeTransferHandler());
        this.getSelectionModel().setSelectionMode(
                TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
    }

//    public void serialize(String outPutFileName) throws IOException {
//        FileOutputStream fos = new FileOutputStream(outPutFileName);
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(this);
//        oos.flush();
//        oos.close();
//    }
//
//    public Tree<T> deSerialize(String name) throws IOException, ClassNotFoundException {
//        FileInputStream fis = new FileInputStream(name);
//        ObjectInputStream oin = new ObjectInputStream(fis);
//        return (Tree<T>) oin.readObject();
//    }

    @SuppressWarnings("unchecked")
//    public TreeNode<T> getRoot() {
//        return (TreeNode<T>) getModel().getRoot();
//    }

//    public void fillChildren(Consumer<TreeNode<T>> childFiller){
//        getRoot().fillChildren(childFiller);
//    }

//    /** Loop for all nodes (deep=true) */
//    public void forEachNodes(Consumer<TreeNode<T>> childConsumer){
//        TreeNode<T> root = getRoot();
//        childConsumer.accept(root);
//        root.forEach(childConsumer, true);
//    }

//    public T getRootUserObject(){
//        return getRoot().getUserObject();
//    }

//    public TreeNode<T> getSelectedNode(){
//        @SuppressWarnings("unchecked")
//        TreeNode<T> node = (TreeNode<T>) getSelectionModel().getSelectionPath().getLastPathComponent();
//        return node;
//    }

//    public T getSelectedUserObject(){
//        TreeNode<T> node = getSelectedNode();
//        if(node != null /*&& node.isLeaf()*/) {
//            return node.getUserObject();
//        }
//        return null;
//    }

//    public void setSelectedNode(TreeNode<T> node){
//        setSelectionPath(new TreePath(node.getPath()));
//    }

//    public void onChange(Consumer<T> consumer){
//        super.addTreeSelectionListener(evt->{
//            TreePath selectedPath = evt.getNewLeadSelectionPath();
//            if(selectedPath==null) return;
//            @SuppressWarnings("unchecked")
//            TreeNode<T> node = (TreeNode<T>) selectedPath.getLastPathComponent();
//            if(node != null && node.isLeaf()) {
//                T userObject = node.getUserObject();
//                consumer.accept(userObject);
//            }
//        });
//    }

//    public void onMouseRightClickOnNode(BiConsumer<TreeNode<T>, MouseEvent> consumer){
//        Tree<T> tree = this;
//        CommonHelper.onMouseRightClick(tree, evt->{
//            TreePath path = getPathForLocation(evt.getX(), evt.getY());
//            if(path==null) return;
//            tree.setSelectionPath(path);
//            TreeNode<T> node = tree.getSelectedNode();
//
//            consumer.accept(node, evt);
//        });
//    }

//    public void showPopupOnRightClick(BiConsumer<JPopupMenu, TreeNode<T>> popupMenuItemFiller){
//        this.onMouseRightClickOnNode((node,evt)->{
//            JPopupMenu popup = new JPopupMenu();
//            popupMenuItemFiller.accept(popup,node);
//            popup.show(evt.getComponent(), evt.getX(), evt.getY());
//        });
//    }

//    public void startEditingAtPath(TreePath path) {
//
//    }
//
//    public boolean stopEditing() {
//
//        return false;
//    }

    public static class TreeNode<T> extends DefaultMutableTreeNode {
        @Serial
        private static final long serialVersionUID = 1L;
        boolean isVisible;
        public long nodeId;
//        public List<TreeNode<T>> children;

        public TreeNode(T userObj){
            this(userObj, true);
        }

        public TreeNode(T userObj, boolean isVisible) {
            super(userObj);
            this.isVisible = isVisible;
        }

        /**
         * Returns the child at the specified index in this node's child array.
         *
         * @param index an index into this node's child array
         * @return the TreeNode in this node's child array at  the specified index
         * @throws ArrayIndexOutOfBoundsException if <code>index</code>
         *                                        is out of bounds
         */
        @Override
        public javax.swing.tree.TreeNode getChildAt(int index) {
            return getChildAt(index, isFilterEnabled());
        }

        /**
         * Returns the number of children of this node.
         *
         * @return an int giving the number of children of this node
         */
        @Override
        public int getChildCount() {
            return getChildCount(isFilterEnabled());
        }

        public javax.swing.tree.TreeNode getChildAt(int index, boolean filterIsActive) {
            if (!filterIsActive) {
                return super.getChildAt(index);
            }
            if (children == null) {
                throw new ArrayIndexOutOfBoundsException("node has no children");
            }

            int realIndex = -1;
            int visibleIndex = -1;
            Enumeration e = children.elements();
            while (e.hasMoreElements()) {
                TreeNode node = (TreeNode) e.nextElement();
                if (node.isVisible()) {
                    visibleIndex++;
                }
                realIndex++;
                if (visibleIndex == index) {
                    return children.elementAt(realIndex);
                }
            }

            throw new ArrayIndexOutOfBoundsException("index unmatched");
            //return (TreeNode)children.elementAt(index);
        }

        public int getChildCount(boolean filterIsActive) {
            if (!filterIsActive) {
                return super.getChildCount();
            }
            if (children == null) {
                return 0;
            }

            int count = 0;
            Enumeration e = children.elements();
            while (e.hasMoreElements()) {
                TreeNode node = (TreeNode) e.nextElement();
                if (node.isVisible()) {
                    count++;
                }
            }

            return count;
        }

        private boolean isFilterEnabled(){
            return TreeExample.isFilterEnabled;
        }

//        /** @param userObj
//         * @return child node
//         */
//        public TreeNode<T> addChild(T userObj) {
//            TreeNode<T> child = new TreeNode<>(userObj);
//            add(child);
////            children.add(child);
//            return child;
//        }

        public static void cpNode (Tree<Tree.TreeNode<LeafTest>> tree, Tree.TreeNode<LeafTest> parent,
                                               Tree.TreeNode<LeafTest> child) {
            LeafTest tmpObj = child.getUserObject();
            Tree.TreeNode<LeafTest> tmp;
            if (tmpObj.isFolder){
                tmp = new Tree.TreeNode<>(new LeafTest(tmpObj.name));
                tmp.getUserObject().isFolder=true;
            } else {
                tmp = new Tree.TreeNode<>(new LeafTest(tmpObj.name, tmpObj.xID, tmpObj.xPSW, tmpObj.comment));
                tmp.getUserObject().isFolder=false;
            }
            if (child.getChildCount(false) > 0){
                for(int i = 0; i < child.getChildCount(false); i++){
                    cpNode(tree, tmp,((Tree.TreeNode<LeafTest>) child.getChildAt(i,false)));
//                    LeafTest tmpObj2 = ((Tree.TreeNode<LeafTest>) child.getChildAt(i)).getUserObject();
//                    Tree.TreeNode<LeafTest> tmp2;
//                    if (tmpObj2.isFolder){
//                        tmp2 = new Tree.TreeNode<>(new LeafTest(tmpObj2.name));
//                        tmp2.getUserObject().isFolder=true;
//                    } else {
//                        tmp2 = new Tree.TreeNode<>(new LeafTest(tmpObj2.name, tmpObj2.xID, tmpObj2.xPSW, tmpObj2.comment));
//                        tmp2.getUserObject().isFolder=false;
//                    }
//                    ((DefaultTreeModel) tree.getModel()).insertNodeInto(tmp2, tmp, tmp
//                            .getChildCount());
                }
            }
            ((DefaultTreeModel) tree.getModel()).insertNodeInto(tmp, parent, parent
                    .getChildCount(false));
            if (!(tmp.isLeaf())) {
                tmp.nodeId = tree.counter;
                tree.counter++;
            }
            tree.scrollPathToVisible(new TreePath(tmp.getPath()));
//            return child;
        }

        @Override
        public T getUserObject(){
            @SuppressWarnings("unchecked")
            T userObject = (T) super.getUserObject();
            return userObject;
        }

//        @SuppressWarnings("unchecked")
//        public List<TreeNode<T>> getChildren(){
//            return children;//.clone();
//        }

//        public void fillChildren(Consumer<TreeNode<T>> childFiller){
//            if(getChildCount() != 0)
//                super.removeAllChildren();
//            childFiller.accept(this);
//            forEach(child->child.fillChildren(childFiller), false);
//        }

//        /**
//         * @param childConsumer
//         * @param deep <code>true</code>=access all, <code>false</code>=first level children
//         */
//        public void forEach(Consumer<TreeNode<T>> childConsumer, boolean deep){
//            getChildren().forEach(child->{
//                childConsumer.accept(child);
//                if(deep)
//                    child.forEach(childConsumer, deep);
//            });
//        }

        public boolean isLeaf() {
            return !(((LeafTest) getUserObject()).isFolder);
        }

        public void setVisible(boolean visible) {
            this.isVisible = visible;
        }

        public boolean isVisible() {
            return isVisible;
        }
    }

//    public class CommonHelper {
//
//        /**
//         * @param comp component where listener should add
//         * @param onRightClickConsumer
//         */
//        public static void onMouseRightClick(Component comp, Consumer<MouseEvent> onRightClickConsumer){
//            comp.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    if(SwingUtilities.isRightMouseButton(e) && e.getClickCount()==1)
//                        onRightClickConsumer.accept(e); //call on right click
//                }
//            });
//        }
//    }

//    static void serialize(Tree user, String path) throws IOException {
//        try (FileOutputStream outputStream = new FileOutputStream(path); ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
//            //создаем 2 потока для сериализации объекта и сохранения его в файл
//
//            // сохраняем объект в файл
//            objectOutputStream.writeObject(user);
//        }
//        // Закроем потоки в блоке finally
//    }
//    static Tree deserialize(String path) throws IOException, ClassNotFoundException {
//
//        try (FileInputStream fileInputStream = new FileInputStream(path); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
//
//            //создаем 2 потока для десериализации объекта из файла
//
//            //загружаем объект из файла
//            return (Tree) objectInputStream.readObject();
//        }
//    }

}