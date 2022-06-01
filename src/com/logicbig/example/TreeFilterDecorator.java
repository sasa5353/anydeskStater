package com.logicbig.example;

import serialTree.Tree;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.function.BiPredicate;

public class TreeFilterDecorator {
    private final JTree tree;
    private Tree.TreeNode originalRootNode;
    private BiPredicate<Object, String> userObjectMatcher;
    private JTextField filterField;

    public TreeFilterDecorator(JTree tree, BiPredicate<Object, String> userObjectMatcher) {
        this.tree = tree;
        this.originalRootNode = (Tree.TreeNode) tree.getModel().getRoot();
        this.userObjectMatcher = userObjectMatcher;
    }

    public static TreeFilterDecorator decorate(JTree tree, BiPredicate<Object, String> userObjectMatcher) {
        TreeFilterDecorator tfd = new TreeFilterDecorator(tree, userObjectMatcher);
        tfd.init();
        return tfd;
    }

    public JTextField getFilterField() {
        return filterField;
    }

    private void init() {
        initFilterField();
    }

    private void initFilterField() {
        filterField = new JTextField(15);
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTree();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTree();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTree();
            }
        });
    }

    private void filterTree() {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        String text = filterField.getText().trim().toLowerCase();
//        if (text.equals("") && tree.getModel().getRoot() != originalRootNode) {
        model.setRoot(originalRootNode);
        if (text.equals("")){
            setVisible(originalRootNode,true);
        } else {
//            Tree.TreeNode newRootNode = matchAndBuildNode(text, originalRootNode);
//            model.setRoot(newRootNode);
            setVisible(originalRootNode,false);
            setVisible(text, originalRootNode);
        }
        model.reload();
        JTreeUtil.setTreeExpandedState(tree, true);
    }

//    public void unFilterTree(){
//        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
//        model.setRoot(originalRootNode);
//        JTreeUtil.setTreeExpandedState(tree, true);
//    }

    private Tree.TreeNode setVisible(final String text, Tree.TreeNode oldNode) {
        if (!oldNode.isRoot() && userObjectMatcher.test(oldNode.getUserObject(), text)) {
//            oldNode.setVisible(true);
            setVisible(oldNode,true);
            return oldNode;
        }
        Tree.TreeNode newMatchedNode = oldNode.isRoot() ?oldNode : null;
        for (TreeNode childOldNode : JTreeUtil.children(oldNode)) {
            Tree.TreeNode newMatchedChildNode = setVisible(text, (Tree.TreeNode) childOldNode);
            if (newMatchedChildNode != null) {
                if (newMatchedNode == null) {
                    newMatchedNode = oldNode;
                    newMatchedNode.setVisible(true);
                }
                newMatchedChildNode.setVisible(true);
            }
        }
        return newMatchedNode;
    }

    private void setVisible(Tree.TreeNode oldNode, boolean isVisible) {
        if (!oldNode.isRoot()) {
            oldNode.setVisible(isVisible);
        } else {
            oldNode.setVisible(true);
        }
        for (TreeNode childOldNode : JTreeUtil.children(oldNode)) {
            Tree.TreeNode newMatchedChildNode = (Tree.TreeNode) childOldNode;
            newMatchedChildNode.setVisible(isVisible);
            setVisible((Tree.TreeNode) childOldNode, isVisible);
        }
//        return newMatchedNode;
    }

    private Tree.TreeNode matchAndBuildNode(final String text, Tree.TreeNode oldNode) {
        if (!oldNode.isRoot() && userObjectMatcher.test(oldNode.getUserObject(), text)) {
            return JTreeUtil.copyNode(oldNode);
        }
        Tree.TreeNode newMatchedNode = oldNode.isRoot() ? new Tree.TreeNode(oldNode
                .getUserObject()) : null;
        for (TreeNode childOldNode : JTreeUtil.children(oldNode)) {
            Tree.TreeNode newMatchedChildNode = matchAndBuildNode(text, (Tree.TreeNode) childOldNode);
            if (newMatchedChildNode != null) {
                if (newMatchedNode == null) {
                    newMatchedNode = new Tree.TreeNode(oldNode.getUserObject());
                }
                newMatchedNode.add(newMatchedChildNode);
            }
        }
        return newMatchedNode;
    }
}