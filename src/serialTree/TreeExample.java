package serialTree;

import com.logicbig.example.JTreeUtil;
import com.logicbig.example.TradingProjectTreeRenderer;
import com.logicbig.example.TreeFilterDecorator;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import static com.logicbig.example.TreeExampleMain.createUserObjectMatcher;

/**
 * JTree basic tutorial and example
 * @author wwww.codejava.net
 */
public class TreeExample extends JFrame implements ActionListener, MouseListener, Serializable, KeyListener {
    @Serial
    private static final long serialVersionUID = 1L;
    public Tree tree;
    private JLabel selectedLabel;
    JPopupMenu popMenu;
    JMenuItem addItem;
    JMenuItem addFoldItem;
    JMenuItem delItem;
    JMenuItem editItem;
    JMenuItem runItem;
    JMenuItem runFTItem;
    JMenuItem saveItem;
    JMenuItem cpItem;
    JMenuItem ptItem;
//    JMenuItem ptLinkItem;
    JMenuItem ptContItem;
    JMenuItem sortItem;
    JMenuItem exportItem;
    JMenuItem importItem;
    JMenuItem expColItem;

    JTextField nameField;
    JTextField idField;
    JTextField passField;
    JTextField commentField;

    JDialog dialog;

    private boolean enFlag=false;
    private boolean ctrlEnFlag=false;

    public static boolean isFilterEnabled = true;

    private Tree.TreeNode<LeafTest> tmpBuff;
//    TreeFilterDecorator filterDecorator;

    public TreeExample() throws IOException, ClassNotFoundException {
        this(null);
    }

    public TreeExample(Tree _tree) throws IOException, ClassNotFoundException {
//        String tmpName = "base.bin";
//        LeafTest rootL = new LeafTest("Root");

//        Tree.TreeNode root = new Tree.TreeNode(new LeafTest("Root"));
//        root.addChild(ts);
//        root.addChild(ts2);
//        Tree.TreeNode treeNode = root.addChild(ts2).addChild(ts2);
//        treeNode.addChild(ts2);
//        Consumer<Tree.TreeNode<LeafTest>> childConsumer = x -> System.out.println("Hello " + x.getUserObject().toString() + " !!!");
//        treeNode.forEach(childConsumer, false);
//        Tree tree1 = new Tree<>(new Tree.TreeNode(new LeafTest("Root")));
//        tree1.serialize(Main.tmpName);
//        System.out.println(tree == null);
//        this.tree = (new Tree<>(new LeafTest())).deSerialize(Main.tmpName);
        tree=_tree;
        if (tree == null) {
            tree = new Tree<>(new Tree.TreeNode(new LeafTest("Root")));
        }
        //        ImageIcon imageIcon = new ImageIcon(TreeExample.class.getResource("/leaf.jpg"));
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
//        renderer.setLeafIcon(imageIcon);

        tree.setCellRenderer(renderer);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
        add(new JScrollPane(tree));

        selectedLabel = new JLabel();
        add(selectedLabel, BorderLayout.SOUTH);
        tree.setSelectionPath(new  TreePath(tree.getModel().getRoot()));
        selectedLabel.setText(((Tree.TreeNode<LeafTest>)tree.getModel().getRoot()).getUserObject().toString());
                    tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Tree.TreeNode<LeafTest> selectedNode = (Tree.TreeNode<LeafTest>) tree.getLastSelectedPathComponent();
                if (selectedNode == null) selectedNode = (Tree.TreeNode<LeafTest>) tree.getModel().getRoot();
                selectedLabel.setText(selectedNode.getUserObject().toString());
                enFlag=false;
                ctrlEnFlag=false;
            }
        });

        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.makeItDraggable();
        tree.addMouseListener(this);
        tree.addKeyListener(this);
        tree.setRowHeight(Main.sizeOfFont+5);
        tree.setCellEditor(new DefaultTreeCellEditor(tree, new DefaultTreeCellRenderer()));
        getContentPane().add(tree);
        setSize(200, 200);

//        popMenu = new JPopupMenu();
//        addItem = new JMenuItem ("Новый хост");
//        addItem.addActionListener(this);
//        addFoldItem = new JMenuItem ("Новая папка");
//        addFoldItem.addActionListener(this);
//        delItem = new JMenuItem ("Удалить");
//        delItem.addActionListener(this);
//        editItem = new JMenuItem ("Изменить");
//        editItem.addActionListener(this);
//        runItem = new JMenuItem ("Запустить");
//        runItem.addActionListener(this);
//        runFTItem = new JMenuItem ("Запустить File-Transfer");
//        runFTItem.addActionListener(this);
//        saveItem = new JMenuItem("Сохранить данные");
//        saveItem.addActionListener(this);
//
//        popMenu.add(addItem);
//        popMenu.add(addFoldItem);
//        popMenu.add(delItem);
//        popMenu.add(editItem);
//        popMenu.add(runItem);
//        popMenu.add(runFTItem);
//        popMenu.add(saveItem);

        getContentPane().add(new JScrollPane(tree));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("AnyDesk starter");
        this.setSize(Main.mainFrameWidth, Main.mainFrameHeight);

        JTreeUtil.setTreeExpandedState(tree, true);
        TreeFilterDecorator filterDecorator = TreeFilterDecorator.decorate(tree, createUserObjectMatcher());
        tree.setCellRenderer(new TradingProjectTreeRenderer(() -> filterDecorator.getFilterField().getText()));
        this.add(filterDecorator.getFilterField(), BorderLayout.NORTH);
        this.setLocationRelativeTo(null);

        this.setVisible(true);
        this.toFront();
        tree.requestFocus();
    }

    private void recreateMenu(boolean isFolder, boolean isRoot) {
        recreateMenu(isFolder, isRoot, false);
    }

    private void recreateMenu(boolean isFolder, boolean isRoot, boolean fromMultiSel){
        popMenu = new JPopupMenu();
        addItem = new JMenuItem ("Новый хост");
        addItem.addActionListener(this);
        addFoldItem = new JMenuItem ("Новая папка");
        addFoldItem.addActionListener(this);
        delItem = new JMenuItem ("Удалить");
        delItem.addActionListener(this);
        editItem = new JMenuItem ("Изменить");
        editItem.addActionListener(this);
        runItem = new JMenuItem ("Подключиться");
        runItem.addActionListener(this);
        runFTItem = new JMenuItem ("Начать передачу файлов");
        runFTItem.addActionListener(this);
        saveItem = new JMenuItem("Сохранить данные");
        saveItem.addActionListener(this);
        cpItem = new JMenuItem("Копировать");
        cpItem.addActionListener(this);
        ptItem = new JMenuItem("Вставить");
        ptItem.addActionListener(this);
//        ptLinkItem = new JMenuItem("Вставить как ссылку");
//        ptLinkItem.addActionListener(this);
        ptContItem = new JMenuItem("Вставить содержимое");
        ptContItem.addActionListener(this);
        sortItem = new JMenuItem("Сортировать");
        sortItem.addActionListener(this);
        exportItem = new JMenuItem("Экспортировать");
        exportItem.addActionListener(this);
        importItem = new JMenuItem("Импортировать");
        importItem.addActionListener(this);
        expColItem = new JMenuItem("Свернуть/Развернуть");
        expColItem.addActionListener(this);

        if (!fromMultiSel) {
            if (!isFolder) {
                popMenu.add(runItem);
                popMenu.add(runFTItem);
            } else {
                popMenu.add(addItem);
                popMenu.add(addFoldItem);
            }
//        if (!isRoot) {
            popMenu.add(cpItem);
//        }
            if (tmpBuff != null) {
                if (!(tmpBuff.isRoot())) {
                    popMenu.add(ptItem);
                }
                popMenu.add(ptContItem);
//            popMenu.add(ptLinkItem);
            }
            if (!isRoot) {
                popMenu.add(delItem);
                popMenu.add(editItem);
            } else {
                popMenu.add(sortItem);
                popMenu.add(exportItem);
                popMenu.add(importItem);
                popMenu.add(expColItem);
                popMenu.add(saveItem);
            }
        } else {
            popMenu.add(delItem);
        }
    }


    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        TreePath path = tree.getPathForLocation(e.getX(), e.getY()); // Ключом является использование этого метода
        if (path == null) {
            path = new TreePath(tree.getModel().getRoot());
        }
        if (tree.getSelectionPaths() == null) {
            tree.setSelectionPath(path);
        }
        TreePath[] paths = tree.getSelectionPaths();
        if (paths.length<=1 && !e.isControlDown()) {
            tree.setSelectionPath(path);

            Tree.TreeNode<LeafTest> chosen = (Tree.TreeNode<LeafTest>) tree.getLastSelectedPathComponent();
            this.recreateMenu(chosen.getUserObject().isFolder, chosen == (Tree.TreeNode<LeafTest>) tree.getModel().getRoot());
            if (e.getButton() == 1 && e.getClickCount() == 1 && chosen != (Tree.TreeNode<LeafTest>) tree.getModel().getRoot()) {
                if ((chosen.getUserObject().isFolder)) {
                    if (tree.isExpanded(path)) {
                        tree.collapsePath(path);
                    } else {
                        tree.expandPath(path);
                    }
                }
            }
            if (e.getButton() == 1 && e.getClickCount() == 2) {
                if (!(chosen.getUserObject().isFolder)) {
                    chosen.getUserObject().excAnyDesk();
                }
            }
            if (e.getButton() == 3) {
                popMenu.show(tree, e.getX(), e.getY());
            }
            enFlag = false;
            ctrlEnFlag = false;
        } else {
            if (e.getButton() == 3) {
                this.recreateMenu(true,true,true);
                popMenu.show(tree, e.getX(), e.getY());
            }
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void keyPressed(KeyEvent e){
        if (tree.getSelectionPaths() == null) {
            tree.setSelectionPath(new TreePath(tree.getModel().getRoot()));
        }
        TreePath[] paths = tree.getSelectionPaths();
        if (paths.length<=1 && !e.isControlDown()) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
//            Tree.TreeNode<LeafTest> chosen = null;
//            if (chosen == null) {
//                chosen = (Tree.TreeNode<LeafTest>) tree.getModel().getRoot();
//            }
                tree.setSelectionPath(new TreePath(tree.getModel().getRoot()));
                recreateMenu(true, true);

                popMenu.show(tree, 0, 0);
            } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER && !ctrlEnFlag) {
                Tree.TreeNode<LeafTest> chosen = (Tree.TreeNode<LeafTest>) tree.getLastSelectedPathComponent();
                if (chosen == null) {
                    chosen = (Tree.TreeNode<LeafTest>) tree.getModel().getRoot();
                }
                if (!(chosen.getUserObject().isFolder)) {
                    chosen.getUserObject().excAnyDesk(true);
                    ctrlEnFlag = true;
                }
            } else if ((e.getKeyCode() == KeyEvent.VK_ENTER) && !enFlag) {// || (e.getKeyCode()==KeyEvent.VK_RIGHT)){
                Tree.TreeNode<LeafTest> chosen = (Tree.TreeNode<LeafTest>) tree.getLastSelectedPathComponent();
                if (chosen == null) {
                    chosen = (Tree.TreeNode<LeafTest>) tree.getModel().getRoot();
                }
                if (!(chosen.getUserObject().isFolder)) {
                    chosen.getUserObject().excAnyDesk();
                    enFlag = true;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_INSERT) {
                Tree.TreeNode<LeafTest> chosen = (Tree.TreeNode<LeafTest>) tree.getLastSelectedPathComponent();
                if (chosen == null) {
                    chosen = (Tree.TreeNode<LeafTest>) tree.getModel().getRoot();
                }
                recreateMenu(chosen.getUserObject().isFolder, chosen == (Tree.TreeNode<LeafTest>) tree.getModel().getRoot());

                popMenu.show(tree, 0, 0);
            } else if (e.getKeyCode() == KeyEvent.VK_F2) {
                Tree.TreeNode<LeafTest> node = (Tree.TreeNode<LeafTest>) tree.getLastSelectedPathComponent();
                if (node.isRoot()) {
                    return;
                }
                if (node.getUserObject().isFolder) {
                    dialog = new JDialog(this, "Change folder");
                    JLabel nameLabel = new JLabel("Name:");
                    nameField = new JTextField(30);
                    nameField.setText(node.getUserObject().name);
                    nameLabel.setLabelFor(nameField);
                    JButton b = new JButton("Save");
                    b.setActionCommand("editFolder");
                    b.addActionListener(this);
                    JButton bC = new JButton("Cancel");
                    bC.addActionListener(this);
                    JPanel p = new JPanel();
                    p.add(nameLabel);
                    p.add(nameField);
                    p.setLayout(new GridLayout(1, 2, -400, 5));
                    final Set forwardKeys = p.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
                    final Set newForwardKeys = new HashSet(forwardKeys);
                    newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
                    p.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
//                GroupLayout groupLayout = new GroupLayout(p);
//                groupLayout.setAutoCreateGaps(true);
//                p.setLayout(groupLayout);
//                groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                                .addComponent(nameLabel))
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                                .addComponent(nameField)));
//                groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(nameLabel)
//                                .addComponent(nameField)));
                    JPanel pB = new JPanel();
                    pB.add(b);
                    pB.add(bC);
                    dialog.add(p);
                    dialog.add(pB, BorderLayout.AFTER_LAST_LINE);
                    dialog.setSize(Main.dialogFolderFrameWidth, Main.dialogFolderFrameHeight);
                    if (Main.dialogAutoResize) {
                        dialog.pack();
                    }
                    nameField.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && dialog != null) {
                                dialog.dispose();
                            } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                                b.doClick();
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    });
                    dialog.setVisible(true);
                } else {
                    dialog = new JDialog(this, "Change host");
                    JLabel nameLabel = new JLabel("Name:");
                    JLabel idLabel = new JLabel("ID:");
                    JLabel passLabel = new JLabel("Password:");
                    JLabel comLabel = new JLabel("Comment:");
                    nameField = new JTextField(30);
                    nameField.setText(node.getUserObject().name);
                    idField = new JTextField(30);
                    idField.setText(node.getUserObject().xID);
                    passField = new JTextField(30);
                    passField.setText(node.getUserObject().xPSW);
                    commentField = new JTextField(30);
                    commentField.setText(node.getUserObject().comment);
                    nameLabel.setLabelFor(nameField);
                    idLabel.setLabelFor(idField);
                    passLabel.setLabelFor(passField);
                    comLabel.setLabelFor(commentField);
                    JButton b = new JButton("Save");
                    b.setActionCommand("editHost");
                    b.addActionListener(this);
                    JButton bC = new JButton("Cancel");
                    bC.addActionListener(this);
                    JPanel p = new JPanel();
                    p.add(nameLabel);
                    p.add(nameField);
                    p.add(idLabel);
                    p.add(idField);
                    p.add(passLabel);
                    p.add(passField);
                    p.add(comLabel);
                    p.add(commentField);
                    p.setLayout(new GridLayout(4, 2, -400, 5));
                    final Set forwardKeys = p.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
                    final Set newForwardKeys = new HashSet(forwardKeys);
                    newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
                    p.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
//                GroupLayout groupLayout = new GroupLayout(p);
//                groupLayout.setAutoCreateGaps(true);
//                p.setLayout(groupLayout);
//                groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                                .addComponent(nameLabel)
//                                .addComponent(idLabel)
//                                .addComponent(passLabel)
//                                .addComponent(comLabel))
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                                .addComponent(nameField)
//                                .addComponent(idField)
//                                .addComponent(passField)
//                                .addComponent(commentField)));
//                groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(nameLabel)
//                                .addComponent(nameField))
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(idLabel)
//                                .addComponent(idField))
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(passLabel)
//                                .addComponent(passField))
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(comLabel)
//                                .addComponent(commentField)));
                    JPanel pB = new JPanel();
                    pB.add(b);
                    pB.add(bC);
                    dialog.add(p);
                    dialog.add(pB, BorderLayout.AFTER_LAST_LINE);

                    dialog.setSize(Main.dialogHostFrameWidth, Main.dialogHostFrameHeight);
                    if (Main.dialogAutoResize) {
                        dialog.pack();
                    }
                    nameField.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && dialog != null) {
                                dialog.dispose();
                            } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                                b.doClick();
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    });
                    idField.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && dialog != null) {
                                dialog.dispose();
                            } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                                b.doClick();
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    });
                    passField.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && dialog != null) {
                                dialog.dispose();
                            } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                                b.doClick();
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    });
                    commentField.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && dialog != null) {
                                dialog.dispose();
                            } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                                b.doClick();
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    });
                    dialog.setVisible(true);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_F3) {
                if (tree.isExpanded) {
                    for (int i = tree.getRowCount() - 1; i >= 0; i--) {
                        tree.collapseRow(i);
                    }
                } else {
                    for (int i = 0; i < tree.getRowCount(); i++) {
                        tree.expandRow(i);
                    }
                }
                tree.isExpanded = !tree.isExpanded;
            } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_DELETE) {
                Tree.TreeNode<LeafTest> node = (Tree.TreeNode<LeafTest>) tree.getLastSelectedPathComponent();
                if (node.isRoot()) {
                    return;
                }
                ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
            } else if (e.getKeyCode() == KeyEvent.VK_F4) {
                TreeExample.isFilterEnabled = !(TreeExample.isFilterEnabled);
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                model.reload();
                JTreeUtil.setTreeExpandedState(tree, true);
            }
//        else {
//            System.out.println(e.getKeyCode());
//        }
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    public void actionPerformed(ActionEvent e) {
        Tree.TreeNode<LeafTest> node = (Tree.TreeNode<LeafTest>) tree.getLastSelectedPathComponent();
        if (e.getSource() == runItem && !node.getUserObject().isFolder && node.isLeaf()) {
            node.getUserObject().excAnyDesk();
        } else if (e.getSource() == runFTItem && !node.getUserObject().isFolder && node.isLeaf()) {
            node.getUserObject().excAnyDesk(true);
        } else if (e.getSource() == addFoldItem && node.getUserObject().isFolder && (dialog==null || !dialog.isShowing())){
            dialog = new JDialog(this, "Add folder");
            JLabel nameLabel = new JLabel("Name:");
            nameField = new JTextField(30);
            nameLabel.setLabelFor(nameField);
            JButton b = new JButton("Save");
            b.setActionCommand("addFolder");
            b.addActionListener(this);
            JButton bC = new JButton("Cancel");
            bC.addActionListener(this);
            JPanel p = new JPanel();
            p.add(nameLabel);
            p.add(nameField);
            p.setLayout(new GridLayout(1,2,-400,5));
            final Set forwardKeys = p.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
            final Set newForwardKeys = new HashSet(forwardKeys);
            newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
            p.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
//            GroupLayout groupLayout = new GroupLayout(p);
//            groupLayout.setAutoCreateGaps(true);
//            p.setLayout(groupLayout);
//            groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
//                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                            .addComponent(nameLabel))
//                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                            .addComponent(nameField)));
//            groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
//                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                            .addComponent(nameLabel)
//                            .addComponent(nameField)));
            JPanel pB = new JPanel();
            pB.add(b);
            pB.add(bC);
            dialog.add(p);
            dialog.add(pB,BorderLayout.AFTER_LAST_LINE);
            dialog.setSize(Main.dialogFolderFrameWidth, Main.dialogFolderFrameHeight);
            if(Main.dialogAutoResize) {
                dialog.pack();
            }
            nameField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_ESCAPE && dialog!=null) {
                            dialog.dispose();
                        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                            b.doClick();
                        }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
//            dialog.addKeyListener(this);
//            KeyboardFocusManager
//                    .getCurrentKeyboardFocusManager()
//                    .addKeyEventDispatcher(new KeyEventDispatcher() {
//                        public boolean dispatchKeyEvent(KeyEvent e) {
//                            boolean keyHandled = false;
//                            if (e.getID() == KeyEvent.KEY_PRESSED) {
//                                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
////                                    ok();
//                                    keyHandled = true;
//                                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//                                    dialog.dispose();
//                                    keyHandled = true;
//                                }
//                            }
//                            return keyHandled;
//                        }
//                    });
            dialog.setVisible(true);
        } else if (e.getActionCommand().equals("addFolder")){
            Tree.TreeNode<LeafTest> tmp = new Tree.TreeNode<>(new LeafTest(nameField.getText().trim()));
            ((DefaultTreeModel) tree.getModel()).insertNodeInto(tmp, node, node
                    .getChildCount(false));
            tmp.nodeId= tree.counter;
            tree.counter++;
            tree.scrollPathToVisible(new TreePath(tmp.getPath()));
//            node.children.add(tmp);
//            tree.expandPath(tree.getSelectionPath());
            dialog.dispose();
//            try {
//                TreeExample.serialize(this, Main.baseName);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
        } else if (e.getSource() == addItem && node.getUserObject().isFolder && (dialog==null || !dialog.isShowing())){
            dialog = new JDialog(this, "Add host");
            JLabel nameLabel = new JLabel("Name:");
            JLabel idLabel = new JLabel("ID:");
            JLabel passLabel = new JLabel("Password:");
            JLabel comLabel = new JLabel("Comment:");
            nameField = new JTextField(30);
            idField = new JTextField(30);
            passField = new JTextField(30);
            commentField = new JTextField(30);
            nameLabel.setLabelFor(nameField);
            idLabel.setLabelFor(idField);
            passLabel.setLabelFor(passField);
            comLabel.setLabelFor(commentField);
            JButton b = new JButton("Save");
            b.setActionCommand("addHost");
            b.addActionListener(this);
            JButton bC = new JButton("Cancel");
            bC.addActionListener(this);
            JPanel p = new JPanel();
            p.add(nameLabel);
            p.add(nameField);
            p.add(idLabel);
            p.add(idField);
            p.add(passLabel);
            p.add(passField);
            p.add(comLabel);
            p.add(commentField);
            p.setLayout(new GridLayout(4,2,-400,5));
            final Set forwardKeys = p.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
            final Set newForwardKeys = new HashSet(forwardKeys);
            newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
            p.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
//            GroupLayout groupLayout = new GroupLayout(p);
//            groupLayout.setAutoCreateGaps(true);
//            p.setLayout(groupLayout);
//            groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
//                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                            .addComponent(nameLabel)
//                            .addComponent(idLabel)
//                            .addComponent(passLabel)
//                            .addComponent(comLabel))
//                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                            .addComponent(nameField)
//                            .addComponent(idField)
//                            .addComponent(passField)
//                            .addComponent(commentField)));
//            groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
//                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                            .addComponent(nameLabel)
//                            .addComponent(nameField))
//                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                            .addComponent(idLabel)
//                            .addComponent(idField))
//                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                            .addComponent(passLabel)
//                            .addComponent(passField))
//                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                            .addComponent(comLabel)
//                            .addComponent(commentField)));
            JPanel pB = new JPanel();
            pB.add(b);
            pB.add(bC);
            dialog.add(p);
            dialog.add(pB,BorderLayout.AFTER_LAST_LINE);

            dialog.setSize(Main.dialogHostFrameWidth, Main.dialogHostFrameHeight);
            if(Main.dialogAutoResize) {
                dialog.pack();
            }
            nameField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_ESCAPE && dialog!=null) {
                            dialog.dispose();
                        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                            b.doClick();
                        }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
            idField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_ESCAPE && dialog!=null) {
                            dialog.dispose();
                        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                            b.doClick();
                        }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
            passField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_ESCAPE && dialog!=null) {
                            dialog.dispose();
                        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                            b.doClick();
                        }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
            commentField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_ESCAPE && dialog!=null) {
                            dialog.dispose();
                        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                            b.doClick();
                        }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
            dialog.setVisible(true);
        } else if (e.getActionCommand().equals("addHost")){
            Tree.TreeNode<LeafTest> tmp = new Tree.TreeNode<>(new LeafTest(nameField.getText().trim(),idField.getText().trim(),passField.getText().trim(),commentField.getText().trim()));
            ((DefaultTreeModel) tree.getModel()).insertNodeInto(tmp, node, node
                    .getChildCount(false));
//            tmp.nodeId=tree.counter;
//            tree.counter++;
            tree.scrollPathToVisible(new TreePath(tmp.getPath()));
//            node.children.add(tmp);
//            tree.expandPath(tree.getSelectionPath());
            dialog.dispose();
//            try {
//                TreeExample.serialize(this, Main.baseName);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
        } else if (e.getSource() == saveItem) {
            try {
                serialize(Main.baseName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
//            try {
//                deserialize(Main.baseName);
//            } catch (IOException | ClassNotFoundException ioException) {
//                ioException.printStackTrace();
//            }
        } else if (e.getSource() == delItem) {
            if (tree.getSelectionPaths() == null) {
                tree.setSelectionPath(new TreePath(tree.getModel().getRoot()));
            }
            TreePath[] paths = tree.getSelectionPaths();
            for (TreePath path : paths) {
                Tree.TreeNode next =
                        (Tree.TreeNode) path.getLastPathComponent();

                if (next.isRoot()) {
                    continue;
                }
                ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(next);
            }
//            try {
//                TreeExample.serialize(this, Main.baseName);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
        } else if (e.getSource() == editItem  && (dialog==null || !dialog.isShowing())) {
//            tree.startEditingAtPath(tree.getSelectionPath());
            if (node.getUserObject().isFolder){
                dialog = new JDialog(this, "Change folder");
                JLabel nameLabel = new JLabel("Name:");
                nameField = new JTextField(30);
                nameField.setText(node.getUserObject().name);
                nameLabel.setLabelFor(nameField);
                JButton b = new JButton("Save");
                b.setActionCommand("editFolder");
                b.addActionListener(this);
                JButton bC = new JButton("Cancel");
                bC.addActionListener(this);
                JPanel p = new JPanel();
                p.add(nameLabel);
                p.add(nameField);
                p.setLayout(new GridLayout(1,2,-400,5));
                final Set forwardKeys = p.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
                final Set newForwardKeys = new HashSet(forwardKeys);
                newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
                p.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
//                GroupLayout groupLayout = new GroupLayout(p);
//                groupLayout.setAutoCreateGaps(true);
//                p.setLayout(groupLayout);
//                groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                                .addComponent(nameLabel))
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                                .addComponent(nameField)));
//                groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(nameLabel)
//                                .addComponent(nameField)));
                JPanel pB = new JPanel();
                pB.add(b);
                pB.add(bC);
                dialog.add(p);
                dialog.add(pB,BorderLayout.AFTER_LAST_LINE);
                dialog.setSize(Main.dialogFolderFrameWidth, Main.dialogFolderFrameHeight);
                if(Main.dialogAutoResize) {
                    dialog.pack();
                }
                nameField.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode()==KeyEvent.VK_ESCAPE && dialog!=null) {
                            dialog.dispose();
                        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                            b.doClick();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });
                dialog.setVisible(true);
            } else {
                dialog = new JDialog(this, "Change host");
                JLabel nameLabel = new JLabel("Name:");
                JLabel idLabel = new JLabel("ID:");
                JLabel passLabel = new JLabel("Password:");
                JLabel comLabel = new JLabel("Comment:");
                nameField = new JTextField(30);
                nameField.setText(node.getUserObject().name);
                idField = new JTextField(30);
                idField.setText(node.getUserObject().xID);
                passField = new JTextField(30);
                passField.setText(node.getUserObject().xPSW);
                commentField = new JTextField(30);
                commentField.setText(node.getUserObject().comment);
                nameLabel.setLabelFor(nameField);
                idLabel.setLabelFor(idField);
                passLabel.setLabelFor(passField);
                comLabel.setLabelFor(commentField);
                JButton b = new JButton("Save");
                b.setActionCommand("editHost");
                b.addActionListener(this);
                JButton bC = new JButton("Cancel");
                bC.addActionListener(this);
                JPanel p = new JPanel();
                p.add(nameLabel);
                p.add(nameField);
                p.add(idLabel);
                p.add(idField);
                p.add(passLabel);
                p.add(passField);
                p.add(comLabel);
                p.add(commentField);
                p.setLayout(new GridLayout(4,2,-400,5));
                final Set forwardKeys = p.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
                final Set newForwardKeys = new HashSet(forwardKeys);
                newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
                p.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
//                GroupLayout groupLayout = new GroupLayout(p);
//                groupLayout.setAutoCreateGaps(true);
//                p.setLayout(groupLayout);
//                groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                                .addComponent(nameLabel)
//                                .addComponent(idLabel)
//                                .addComponent(passLabel)
//                                .addComponent(comLabel))
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                                .addComponent(nameField)
//                                .addComponent(idField)
//                                .addComponent(passField)
//                                .addComponent(commentField)));
//                groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(nameLabel)
//                                .addComponent(nameField))
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(idLabel)
//                                .addComponent(idField))
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(passLabel)
//                                .addComponent(passField))
//                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                                .addComponent(comLabel)
//                                .addComponent(commentField)));
                JPanel pB = new JPanel();
                pB.add(b);
                pB.add(bC);
                dialog.add(p);
                dialog.add(pB,BorderLayout.AFTER_LAST_LINE);

                dialog.setSize(Main.dialogHostFrameWidth, Main.dialogHostFrameHeight);
                if(Main.dialogAutoResize) {
                    dialog.pack();
                }
                nameField.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode()==KeyEvent.VK_ESCAPE && dialog!=null) {
                            dialog.dispose();
                        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                            b.doClick();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });
                idField.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode()==KeyEvent.VK_ESCAPE && dialog!=null) {
                            dialog.dispose();
                        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                            b.doClick();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });
                passField.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode()==KeyEvent.VK_ESCAPE && dialog!=null) {
                            dialog.dispose();
                        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                            b.doClick();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });
                commentField.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode()==KeyEvent.VK_ESCAPE && dialog!=null) {
                            dialog.dispose();
                        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                            b.doClick();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });
                dialog.setVisible(true);
            }
        } else if (e.getActionCommand().equals("editFolder")){
//            node.setUserObject(new LeafTest(nameField.getText().trim()));
            node.getUserObject().name=nameField.getText().trim();
//            tree.startEditingAtPath(tree.getSelectionPath());
//            tree.stopEditing();
//            try {
//                TreeExample.serialize(this, Main.baseName);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
            dialog.dispose();
            ((DefaultTreeModel) tree.getModel()).reload(node);
            tree.scrollPathToVisible(new TreePath(node.getPath()));

//            tree.repaint();
//            tree.setVisible(true);
//            tree.expandPath(tree.getSelectionPath());
        } else if (e.getActionCommand().equals("editHost")) {
//            node.setUserObject(new LeafTest(nameField.getText().trim(),idField.getText().trim(),passField.getText().trim(),commentField.getText().trim()));
            node.getUserObject().name=nameField.getText().trim();
            node.getUserObject().xID=idField.getText().trim();
            node.getUserObject().xPSW=passField.getText().trim();
            node.getUserObject().comment=commentField.getText().trim();
//            tree.startEditingAtPath(tree.getSelectionPath());
//            tree.stopEditing();
//            try {
//                TreeExample.serialize(this, Main.baseName);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
            dialog.dispose();
            ((DefaultTreeModel) tree.getModel()).reload(node);
            tree.scrollPathToVisible(new TreePath(node.getPath()));
        } else if (e.getActionCommand().equals("Cancel")) {
            dialog.dispose();
        } else if (e.getSource() == cpItem){
            tmpBuff = node;
        } else if (e.getSource() == ptItem){
            if(node.isLeaf()) {
                Tree.TreeNode.cpNode(tree, (Tree.TreeNode<LeafTest>) node.getParent(), tmpBuff);
            } else {
                Tree.TreeNode.cpNode(tree, node, tmpBuff);
            }
            tmpBuff=null;
            //            try {
            //                TreeExample.serialize(this, Main.baseName);
            //            } catch (IOException ex) {
            //                ex.printStackTrace();
            //            }
//        } else if (e.getSource() == ptLinkItem){
//            Tree.TreeNode<LeafTest> tmp = (Tree.TreeNode<LeafTest>) tmpBuff.clone();
//            ((DefaultTreeModel) tree.getModel()).insertNodeInto(tmp, node, node
//                    .getChildCount());
//            if (tmpBuff.getChildCount() > 0){
//                for(int i = 0;i<tmpBuff.getChildCount();i++){
//                    Tree.TreeNode<LeafTest> tmp2 = new Tree.TreeNode<>(((Tree.TreeNode<LeafTest>) tmpBuff.getChildAt(i)).getUserObject());
//                    ((DefaultTreeModel) tree.getModel()).insertNodeInto(tmp2, tmp, tmp
//                            .getChildCount());;
//                }
//            }
//            tree.scrollPathToVisible(new TreePath(tmp.getPath()));
//            //            tree.expandPath(tree.getSelectionPath());
//            tmpBuff=null;
//            //            try {
//            //                TreeExample.serialize(this, Main.baseName);
//            //            } catch (IOException ex) {
//            //                ex.printStackTrace();
//            //            }
        } else if (e.getSource()==ptContItem) {
            int n = tmpBuff.getChildCount(false);
            if(node.isLeaf()) {
                for (int i = 0; i < n; i++) {
                    Tree.TreeNode.cpNode(tree, (Tree.TreeNode<LeafTest>) node.getParent(),
                            (Tree.TreeNode<LeafTest>) tmpBuff.getChildAt(i, false));
                }
            } else {
                for (int i = 0; i < n; i++) {
                    Tree.TreeNode.cpNode(tree, node, (Tree.TreeNode<LeafTest>) tmpBuff.getChildAt(i, false));
                }
            }
            tmpBuff=null;
        } else if (e.getSource()==sortItem){
            ((DefaultTreeModel) tree.getModel()).reload(sort((DefaultMutableTreeNode) tree.getModel().getRoot()));
        } else if (e.getSource() == exportItem){
//            filterDecorator.unFilterTree();
            FileWriter csvWriter = null;
            try {
                csvWriter = new FileWriter(Main.pathToCsv, Charset.forName("windows-1251"));
                csvWriter.append("nodeId");
                csvWriter.append(";");
                csvWriter.append("parentId");
                csvWriter.append(";");
                csvWriter.append("isFolder");
                csvWriter.append(";");
                csvWriter.append("Name");
                csvWriter.append(";");
                csvWriter.append("Id");
                csvWriter.append(";");
                csvWriter.append("Password");
                csvWriter.append(";");
                csvWriter.append("Comment");
                csvWriter.append("\n");

                Tree.TreeNode<LeafTest> currentNode= (Tree.TreeNode<LeafTest>) tree.getModel().getRoot();
                csvWriter.append(String.valueOf(currentNode.nodeId));
                csvWriter.append(";");
                csvWriter.append(null);
                csvWriter.append(";");
                csvWriter.append(String.valueOf(currentNode.getUserObject().isFolder));
                csvWriter.append(";");
                csvWriter.append(String.valueOf(currentNode.getUserObject().name));
                csvWriter.append(";");
                if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().xID));
                csvWriter.append(";");
                if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().xPSW));
                csvWriter.append(";");
                if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().comment));
                csvWriter.append("\n");

                for (int i = 0; i < currentNode.getChildCount(false); i++){
                    expWithChildren(csvWriter,(Tree.TreeNode<LeafTest>) currentNode.getChildAt(i, false));
                }

//                for(int row=0;row<tree.getRowCount();row++){
//                    currentNode= (Tree.TreeNode<LeafTest>)
//                            tree.getPathForRow(row).getLastPathComponent();
//                    csvWriter.append(String.valueOf(currentNode.nodeId));
//                    csvWriter.append(";");
//                    csvWriter.append(String.valueOf(((Tree.TreeNode) currentNode.getParent()).nodeId));
//                    csvWriter.append(";");
//                    csvWriter.append(String.valueOf(currentNode.getUserObject().isFolder));
//                    csvWriter.append(";");
//                    csvWriter.append(String.valueOf(currentNode.getUserObject().name));
//                    csvWriter.append(";");
//                    if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().xID));
//                    csvWriter.append(";");
//                    if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().xPSW));
//                    csvWriter.append(";");
//                    if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().comment));
//                    csvWriter.append("\n");
//                }

                csvWriter.flush();
                csvWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == importItem){
            File csvFile = new File(Main.pathToCsv);
            if (csvFile.isFile()) {
                // create BufferedReader and read data from csv
                BufferedReader csvReader = null;
                try {
                    csvReader = new BufferedReader(new FileReader(Main.pathToCsv, Charset.forName("windows-1251")));
                    String row;
                    csvReader.readLine();
                    if ((row = csvReader.readLine()) !=null){
                        getContentPane().removeAll();
                        tree = new Tree(new Tree.TreeNode(new LeafTest(row.split(";",-1)[3])));
                        //        ImageIcon imageIcon = new ImageIcon(TreeExample.class.getResource("/leaf.jpg"));
                        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
//        renderer.setLeafIcon(imageIcon);

                        tree.setCellRenderer(renderer);
                        tree.setShowsRootHandles(true);
                        tree.setRootVisible(false);
                        add(new JScrollPane(tree));

                        selectedLabel = new JLabel();
                        add(selectedLabel, BorderLayout.SOUTH);
                        tree.setSelectionPath(new  TreePath(tree.getModel().getRoot()));
                        selectedLabel.setText(((Tree.TreeNode<LeafTest>)tree.getModel().getRoot()).getUserObject().toString());
                    tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
                            @Override
                            public void valueChanged(TreeSelectionEvent e) {
                                Tree.TreeNode<LeafTest> selectedNode = (Tree.TreeNode<LeafTest>) tree.getLastSelectedPathComponent();
                                if (selectedNode == null) selectedNode = (Tree.TreeNode<LeafTest>) tree.getModel().getRoot();
                                selectedLabel.setText(selectedNode.getUserObject().toString());
                                enFlag=false;
                                ctrlEnFlag=false;
                            }
                        });

                        tree.setEditable(false);
                        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                        tree.makeItDraggable();
                        tree.addMouseListener(this);
                        tree.addKeyListener(this);
                        tree.setRowHeight(Main.sizeOfFont+5);
                        tree.setCellEditor(new DefaultTreeCellEditor(tree, new DefaultTreeCellRenderer()));
                        getContentPane().add(tree);
                        setSize(200, 200);
                        getContentPane().add(new JScrollPane(tree));

                        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        this.setTitle("AnyDesk starter");
                        this.setSize(Main.mainFrameWidth, Main.mainFrameHeight);

                        JTreeUtil.setTreeExpandedState(tree, true);
                        TreeFilterDecorator filterDecorator  = TreeFilterDecorator.decorate(tree, createUserObjectMatcher());
                        tree.setCellRenderer(new TradingProjectTreeRenderer(() -> filterDecorator.getFilterField().getText()));
                        this.add(filterDecorator.getFilterField(), BorderLayout.NORTH);
                        this.setLocationRelativeTo(null);

                        this.setVisible(true);
                        this.toFront();
                        tree.setVisible(true);
                        tree.requestFocus();
                    }
                    while ((row = csvReader.readLine()) != null) {
                        String[] data = row.split(";",-1);
                        // do something with the data
                        LeafTest tmpObj;
                        Tree.TreeNode<LeafTest> tmpNode;
                        if (data[2].equals("true")) {
                            tmpObj = new LeafTest(data[3]);
                            tmpNode = new Tree.TreeNode(tmpObj);
                            tmpNode.nodeId=Long.parseLong(data[0]);
                            ((DefaultTreeModel) tree.getModel()).insertNodeInto(tmpNode, getNodeByID(Long.parseLong(data[1])),
                                    getNodeByID(Long.parseLong(data[1])).getChildCount(false));
                            tree.counter=Math.max(tree.counter,tmpNode.nodeId);
                            tree.scrollPathToVisible(new TreePath(tmpNode.getPath()));
                        } else {
                            tmpObj = new LeafTest(data[3],data[4],data[5],data[6]);
                            tmpNode = new Tree.TreeNode(tmpObj);
//                            tmpNode.nodeId=Long.parseLong(data[0]);
                            ((DefaultTreeModel) tree.getModel()).insertNodeInto(tmpNode, getNodeByID(Long.parseLong(data[1])),
                                    getNodeByID(Long.parseLong(data[1])).getChildCount(false));
//                            tree.counter=Math.max(tree.counter,tmpNode.nodeId);
                            tree.scrollPathToVisible(new TreePath(tmpNode.getPath()));
                        }
                    }
                    tree.counter++;
                    csvReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == expColItem) {
            if (tree.isExpanded) {
                for(int i = tree.getRowCount() - 1; i >= 0; i--){
                    tree.collapseRow(i);
                }
            } else {
                for (int i = 0; i < tree.getRowCount(); i++) {
                    tree.expandRow(i);
                }
            }
            tree.isExpanded=!tree.isExpanded;
        }

//            JDialog d = new JDialog(this, "Save dialog");
//            JLabel l = new JLabel("press to save");
////            JLabel nameLabel = new JLabel("Name:");
////            nameField = new JTextField(30);
////            nameLabel.setLabelFor(nameField);
//            JButton button = new JButton("Save");
//            button.addActionListener(this);
////            button.setSize(10, 10);
//            JPanel p = new JPanel();
//            p.add(button);
//            d.add(p);
//
//            d.setSize(400, 400);
//            d.setVisible(true);
        //} else if (e.getActionCommand().equals("Save")){
//            try {
//                TreeExample.serialize(this, Main.tmpName);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }

//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//        if (e.getSource() == addItem) {
//            ((DefaultTreeModel) tree.getModel()).insertNodeInto(new DefaultMutableTreeNode("Test"), node, node
//                    .getChildCount());
//            tree.expandPath(tree.getSelectionPath());
//        } else if (e.getSource() == delItem) {
//            if (node.isRoot()) {
//                return;
//            }
//            ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
//        } else if (e.getSource() == editItem) {
//            tree.startEditingAtPath(tree.getSelectionPath());
//        }
    }
    public DefaultMutableTreeNode sort(DefaultMutableTreeNode node) {

        //sort alphabetically
        for(int i = 0; i < node.getChildCount(); i++) {

            for(int j = i + 1; j < node.getChildCount(); j++) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
                String nt = child.getUserObject().toString();
                DefaultMutableTreeNode prevNode = (DefaultMutableTreeNode) node.getChildAt(j);
                String np = prevNode.getUserObject().toString();

//                System.out.println(nt + " " + np);
                if(nt.compareToIgnoreCase(np) > 0) {
                    node.insert(child, j);
                    node.insert(prevNode, i);
                }
            }
        }

        for(int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);

            if(child.getChildCount() > 0) {
                sort(child);
            }
        }

        //put folders first - normal on Windows and some flavors of Linux but not on Mac OS X.
        for(int i = 0; i < node.getChildCount(); i++) {
            for(int j = i + 1; j < node.getChildCount(); j++) {
                Tree.TreeNode<LeafTest> child = (Tree.TreeNode<LeafTest>) node.getChildAt(i);
                Tree.TreeNode<LeafTest> prevNode = (Tree.TreeNode<LeafTest>) node.getChildAt(j);

                if(!prevNode.isLeaf() && child.isLeaf()) {
                    node.insert(child, j);
                    node.insert(prevNode, i);
                }
            }
        }

        return node;

    }

    public void  expWithChildren(FileWriter csvWriter, Tree.TreeNode<LeafTest> currentNode){
        try {
//        Tree.TreeNode<LeafTest> currentNode= (Tree.TreeNode<LeafTest>) tree.getModel().getRoot();
            if (!(currentNode.isLeaf())) csvWriter.append(String.valueOf(currentNode.nodeId));
            csvWriter.append(";");
            csvWriter.append(String.valueOf(((Tree.TreeNode) currentNode.getParent()).nodeId));
            csvWriter.append(";");
            csvWriter.append(String.valueOf(currentNode.getUserObject().isFolder));
            csvWriter.append(";");
            csvWriter.append(String.valueOf(currentNode.getUserObject().name));
            csvWriter.append(";");
            if (currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().xID));
            csvWriter.append(";");
            if (currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().xPSW));
            csvWriter.append(";");
            if (currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().comment));
            csvWriter.append("\n");

            for (int i = 0; i < currentNode.getChildCount(false); i++) {
                expWithChildren(csvWriter,(Tree.TreeNode<LeafTest>) currentNode.getChildAt(i, false));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public Tree.TreeNode getNodeByID(long idToFind) {
//        return getNodeByID(idToFind, 0, Position.Bias.Forward);
//    }
//
//    public Tree.TreeNode getNodeByID(long idToFind, int startingRow,
//                                     Position.Bias bias) {
//
//        int max = tree.getRowCount();
//        if (idToFind < 0) {
//            throw new IllegalArgumentException();
//        }
//        if(idToFind==0){
//            return (Tree.TreeNode) tree.getModel().getRoot();
//        }
//        if (startingRow < 0 || startingRow >= max) {
//            throw new IllegalArgumentException();
//        }
//
//        // start search from the next/previous element from the
//        // selected element
//        int increment = (bias == Position.Bias.Forward) ? 1 : -1;
//        int row = startingRow;
//        do {
//            TreePath path = tree.getPathForRow(row);
//            long currentNodeId = ((Tree.TreeNode) path.getLastPathComponent()).nodeId;
//
//            if (currentNodeId == idToFind) {
//                return (Tree.TreeNode) path.getLastPathComponent();
//            }
//            row = (row + increment + max) % max;
//        } while (row != startingRow);
//        return null;
//    }

    public Tree.TreeNode getNodeByID(long idToFind) {
        Tree.TreeNode _root = (Tree.TreeNode) tree.getModel().getRoot();
        if (idToFind == 0)
            return _root;
        return getNodeByID(idToFind, _root);
    }

    public Tree.TreeNode getNodeByID(long idToFind, Tree.TreeNode node) {
        for (int i = 0; i < node.getChildCount(false); i++){
            Tree.TreeNode currentNode = (Tree.TreeNode) node.getChildAt(i, false);
            Tree.TreeNode ans;
            if (currentNode.nodeId == idToFind)
                return currentNode;
            if (currentNode.getChildCount(false)>0){
                ans = getNodeByID(idToFind, currentNode);
                if (ans != null)
                    return ans;
            }
        }
        return null;
    }

//    public void serialize(String outPutFileName) throws IOException {
//        FileOutputStream fos = new FileOutputStream(outPutFileName);
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(this);
//        oos.flush();
//        oos.close();
//    }

    public void deserialize(String name) throws IOException, ClassNotFoundException {
        File csvFile = new File(name);
        if (csvFile.isFile()) {
            // create BufferedReader and read data from csv
            BufferedReader csvReader = null;
            try {
                csvReader = new BufferedReader(new FileReader(name, Charset.forName("windows-1251")));
                String row;
                csvReader.readLine();
                if ((row = csvReader.readLine()) != null) {
                    getContentPane().removeAll();
                    tree = new Tree(new Tree.TreeNode(new LeafTest(row.split(";", -1)[3])));
                    //        ImageIcon imageIcon = new ImageIcon(TreeExample.class.getResource("/leaf.jpg"));
                    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
//        renderer.setLeafIcon(imageIcon);

                    tree.setCellRenderer(renderer);
                    tree.setShowsRootHandles(true);
                    tree.setRootVisible(false);
                    add(new JScrollPane(tree));

                    selectedLabel = new JLabel();
                    add(selectedLabel, BorderLayout.SOUTH);
                    tree.setSelectionPath(new TreePath(tree.getModel().getRoot()));
                    selectedLabel.setText(((Tree.TreeNode<LeafTest>)tree.getModel().getRoot()).getUserObject().toString());
                    tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
                        @Override
                        public void valueChanged(TreeSelectionEvent e) {
                            Tree.TreeNode<LeafTest> selectedNode = (Tree.TreeNode<LeafTest>) tree.getLastSelectedPathComponent();
                            if (selectedNode == null)
                                selectedNode = (Tree.TreeNode<LeafTest>) tree.getModel().getRoot();
                            selectedLabel.setText(selectedNode.getUserObject().toString());
                            enFlag = false;
                            ctrlEnFlag = false;
                        }
                    });

                    tree.setEditable(false);
                    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                    tree.makeItDraggable();
                    tree.addMouseListener(this);
                    tree.addKeyListener(this);
                    tree.setRowHeight(Main.sizeOfFont + 5);
                    tree.setCellEditor(new DefaultTreeCellEditor(tree, new DefaultTreeCellRenderer()));
                    getContentPane().add(tree);
                    setSize(200, 200);
                    getContentPane().add(new JScrollPane(tree));

                    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    this.setTitle("AnyDesk starter");
                    this.setSize(Main.mainFrameWidth, Main.mainFrameHeight);

                    JTreeUtil.setTreeExpandedState(tree, true);
                    TreeFilterDecorator filterDecorator = TreeFilterDecorator.decorate(tree, createUserObjectMatcher());
                    tree.setCellRenderer(new TradingProjectTreeRenderer(() -> filterDecorator.getFilterField().getText()));
                    this.add(filterDecorator.getFilterField(), BorderLayout.NORTH);
                    this.setLocationRelativeTo(null);

                    this.setVisible(true);
                    this.toFront();
                    tree.setVisible(true);
                    tree.requestFocus();
                }
                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(";", -1);
                    // do something with the data
                    LeafTest tmpObj;
                    Tree.TreeNode<LeafTest> tmpNode;
                    if (data[2].equals("true")) {
                        tmpObj = new LeafTest(data[3]);
                        tmpNode = new Tree.TreeNode(tmpObj);
                        tmpNode.nodeId = Long.parseLong(data[0]);
                        ((DefaultTreeModel) tree.getModel()).insertNodeInto(tmpNode, getNodeByID(Long.parseLong(data[1])),
                                getNodeByID(Long.parseLong(data[1])).getChildCount(false));
                        tree.counter = Math.max(tree.counter, tmpNode.nodeId);
                        tree.scrollPathToVisible(new TreePath(tmpNode.getPath()));
                    } else {
                        tmpObj = new LeafTest(data[3], data[4], data[5], data[6]);
                        tmpNode = new Tree.TreeNode(tmpObj);
//                        tmpNode.nodeId = Long.parseLong(data[0]);
                        ((DefaultTreeModel) tree.getModel()).insertNodeInto(tmpNode, getNodeByID(Long.parseLong(data[1])),
                                getNodeByID(Long.parseLong(data[1])).getChildCount(false));
//                        tree.counter = Math.max(tree.counter, tmpNode.nodeId);
                        tree.scrollPathToVisible(new TreePath(tmpNode.getPath()));
                    }
                }
                tree.counter++;
                csvReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    void serialize(String path) throws IOException {
//        filterDecorator.unFilterTree();
        FileWriter csvWriter = null;
        try {
            csvWriter = new FileWriter(path, Charset.forName("windows-1251"));
            csvWriter.append("nodeId");
            csvWriter.append(";");
            csvWriter.append("parentId");
            csvWriter.append(";");
            csvWriter.append("isFolder");
            csvWriter.append(";");
            csvWriter.append("Name");
            csvWriter.append(";");
            csvWriter.append("Id");
            csvWriter.append(";");
            csvWriter.append("Password");
            csvWriter.append(";");
            csvWriter.append("Comment");
            csvWriter.append("\n");

            Tree.TreeNode<LeafTest> currentNode= (Tree.TreeNode<LeafTest>) tree.getModel().getRoot();
            csvWriter.append(String.valueOf(currentNode.nodeId));
            csvWriter.append(";");
            csvWriter.append(null);
            csvWriter.append(";");
            csvWriter.append(String.valueOf(currentNode.getUserObject().isFolder));
            csvWriter.append(";");
            csvWriter.append(String.valueOf(currentNode.getUserObject().name));
            csvWriter.append(";");
            if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().xID));
            csvWriter.append(";");
            if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().xPSW));
            csvWriter.append(";");
            if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().comment));
            csvWriter.append("\n");

            for (int i = 0; i < currentNode.getChildCount(false); i++){
                expWithChildren(csvWriter,(Tree.TreeNode<LeafTest>) currentNode.getChildAt(i, false));
            }

//                for(int row=0;row<tree.getRowCount();row++){
//                    currentNode= (Tree.TreeNode<LeafTest>)
//                            tree.getPathForRow(row).getLastPathComponent();
//                    csvWriter.append(String.valueOf(currentNode.nodeId));
//                    csvWriter.append(";");
//                    csvWriter.append(String.valueOf(((Tree.TreeNode) currentNode.getParent()).nodeId));
//                    csvWriter.append(";");
//                    csvWriter.append(String.valueOf(currentNode.getUserObject().isFolder));
//                    csvWriter.append(";");
//                    csvWriter.append(String.valueOf(currentNode.getUserObject().name));
//                    csvWriter.append(";");
//                    if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().xID));
//                    csvWriter.append(";");
//                    if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().xPSW));
//                    csvWriter.append(";");
//                    if(currentNode.isLeaf()) csvWriter.append(String.valueOf(currentNode.getUserObject().comment));
//                    csvWriter.append("\n");
//                }

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
//        try (FileOutputStream outputStream = new FileOutputStream(path); ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
//            //создаем 2 потока для сериализации объекта и сохранения его в файл
//
//            // сохраняем объект в файл
//            objectOutputStream.writeObject(user);
//        }
//        // Закроем потоки в блоке finally
    }
//    static TreeExample deserialize(String path) throws IOException, ClassNotFoundException {
//        try (FileInputStream fileInputStream = new FileInputStream(path); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
//
//            //загружаем объект из файла
//            TreeExample treeExample = (TreeExample) objectInputStream.readObject();
//            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
//
//            treeExample.tree.setCellRenderer(renderer);
//            treeExample.tree.setShowsRootHandles(true);
//            treeExample.tree.setRootVisible(false);
//            treeExample.setVisible(true);
////            treeExample.initialize();
//            return treeExample;
//        }
//    }
}
