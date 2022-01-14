import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Collections;

public class SwingDemo {

    private static void showTree(DefaultMutableTreeNode node) {
        JFrame frame = new JFrame("Tree");
        JTree tree = new JTree(node);
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        frame.add(tree);
        frame.setSize(550,400);

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