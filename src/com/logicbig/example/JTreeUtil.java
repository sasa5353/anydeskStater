package com.logicbig.example;

import serialTree.Tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JTreeUtil {
    public static void setTreeExpandedState(JTree tree, boolean expanded) {
        Tree.TreeNode node = (Tree.TreeNode) tree.getModel().getRoot();
        setNodeExpandedState(tree, node, expanded);
    }

    public static void setNodeExpandedState(JTree tree, Tree.TreeNode node, boolean expanded) {
        for (TreeNode treeNode : children(node)) {
            setNodeExpandedState(tree, (Tree.TreeNode) treeNode, expanded);
        }
        if (!expanded && node.isRoot()) {
            return;
        }
        TreePath path = new TreePath(node.getPath());
        if (expanded) {
            tree.expandPath(path);
        } else {
            tree.collapsePath(path);
        }
    }

    public static Tree.TreeNode copyNode(Tree.TreeNode oldNode) {
        Tree.TreeNode newNode = new Tree.TreeNode(oldNode.getUserObject());
        for (TreeNode oldChildNode : JTreeUtil.children(oldNode)) {
            Tree.TreeNode newChildNode = new Tree.TreeNode(((Tree.TreeNode)oldChildNode).getUserObject());
            newNode.add(newChildNode);
            if (!oldChildNode.isLeaf()) {
                copyChildrenTo((Tree.TreeNode) oldChildNode, newChildNode);
            }
        }
        return newNode;
    }

    public static void copyChildrenTo(Tree.TreeNode from, Tree.TreeNode to) {
        for (TreeNode oldChildNode : JTreeUtil.children(from)) {
            Tree.TreeNode newChildNode = new Tree.TreeNode(((Tree.TreeNode)oldChildNode).getUserObject());
            to.add(newChildNode);
            if (!oldChildNode.isLeaf()) {
                copyChildrenTo((Tree.TreeNode) oldChildNode, newChildNode);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static List<TreeNode> children(Tree.TreeNode node) {
        return Collections.list(node.children());
    }
}