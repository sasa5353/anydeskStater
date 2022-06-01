package com.logicbig.example;

import com.logicbig.util.RandomUtil;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public enum TradingProjectDataService {
    instance;
    private final String ROLES[] =
            {"Project Manager", "Tech Lead", "Developer", "Scrum Master", "Business Analyst"};
    private DefaultMutableTreeNode rootNode;

    TradingProjectDataService() {
        rootNode = new DefaultMutableTreeNode("Trading Project Modules");
        addModule("Trading", "Real Time Trading", "Order System");
        addModule("Future/Option", "Option Analyzer", "Market Scanning System");
        addModule("Fixed Income", "Bond Tool", "Price/Yield Calculator",
                "Strategy Evaluator");
    }

    private void addModule(String module, String... projects) {
        DefaultMutableTreeNode moduleNode = new DefaultMutableTreeNode(module);
        rootNode.add(moduleNode);
        for (String project : projects) {
            moduleNode.add(getProjectNode(module, project));
        }
    }

    private MutableTreeNode getProjectNode(String module, String project) {
        DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(new Project(project));
        for (int i = 0; i < ROLES.length; i++) {
            projectNode.add(getEmployeeNodeForRole(module, project, ROLES[i]));
        }
        return projectNode;
    }

    private MutableTreeNode getEmployeeNodeForRole(String module, String project, String role) {
        //just a random employee for testing
        ProjectParticipant projectParticipant = new ProjectParticipant(RandomUtil.getFullName(), role);
        DefaultMutableTreeNode employeeNode = new DefaultMutableTreeNode(projectParticipant);
        return employeeNode;
    }

    public TreeNode getProjectHierarchy() {
        return rootNode;
    }
}