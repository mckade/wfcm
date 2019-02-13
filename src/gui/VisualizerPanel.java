/**
 * @filename VisualizerPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Visualizer panel that allows for the user to interact with
 * the music generation process.
 * Not yet implemented.
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class VisualizerPanel extends JPanel {
    
    // Components
    private JLabel comingSoon;

    // Constructor
    public VisualizerPanel() {
        
        // Panel settings
        setBackground(MainWindow.BACKGROUND);
        setBorder(MainWindow.BORDER);
        
        // Layout and components
        setLayout(new BorderLayout());
        
        // Creating components
        comingSoon = new JLabel("Visualizer Coming Soon!");
        comingSoon.setFont(new Font(Font.DIALOG, Font.PLAIN, 40));
        comingSoon.setForeground(Color.WHITE);
        comingSoon.setHorizontalAlignment(JLabel.CENTER);
        
        // Adding components
        add(comingSoon, BorderLayout.CENTER);
    }
}
