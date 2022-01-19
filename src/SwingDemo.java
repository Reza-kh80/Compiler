import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class SwingDemo {

    public static void showTree(DefaultMutableTreeNode node) {
        JFrame frame = new JFrame("Tree");
        JTree tree = new JTree(node);
        JScrollPane scrollPane = new JScrollPane(tree);
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        frame.getContentPane().setLayout(new BorderLayout());


        frame.setContentPane(scrollPane);
        frame.setSize(1500,700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void showDemo(){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("Project");
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("App");
        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Website");
        DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("WebApp");
        node.add(node1);
        node.add(node2);
        node.add(node3);
        DefaultMutableTreeNode one = new DefaultMutableTreeNode("Learning website");
        DefaultMutableTreeNode two = new DefaultMutableTreeNode("Business website");
        DefaultMutableTreeNode three = new DefaultMutableTreeNode("News publishing website");
        DefaultMutableTreeNode four = new DefaultMutableTreeNode("Android app");
        DefaultMutableTreeNode five = new DefaultMutableTreeNode("iOS app");
        DefaultMutableTreeNode six = new DefaultMutableTreeNode("Editor WebApp");
        node1.add(one);
        node1.add(two);
        node1.add(three);
        node2.add(four);
        node2.add(five);
        node3.add(six);
        showTree(node);

    }
}