package Compiler;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class SwingDemo {

    public static void showTree(DefaultMutableTreeNode node , String output) {
        JFrame frame = new JFrame("Tree " + output);
        JTree tree = new JTree(node);
        JScrollPane scrollPane = new JScrollPane(tree);
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(scrollPane);
        frame.setSize(1500,700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


}