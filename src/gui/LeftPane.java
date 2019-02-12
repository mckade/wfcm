package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LeftPane extends JPanel {
    
    public LeftPane(JFrame frame) {
        int width = frame.getWidth()/3;
        int height = frame.getHeight();
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(width, height));
    }
}
