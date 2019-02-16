/**
 * @filename SettingsPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Settings panel to hold all possible settings
 * to change or influence music generation.
 * Not yet implemented.
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SettingsPanel extends JPanel {
    
    // Components
    JLabel comingSoon;
    
    // Constructor
    public SettingsPanel(Dimension dim) {
        
        // Panel settings
        setPreferredSize(dim);
        setBackground(MainWindow.BACKGROUND);
        setBorder(MainWindow.BORDER);
        
        // Layout and components
        setLayout(new BorderLayout());
        
        // Creating components
        comingSoon = new JLabel("Settings Coming Soon!");
        comingSoon.setFont(new Font(Font.DIALOG, Font.PLAIN, 40));
        comingSoon.setForeground(Color.WHITE);
        comingSoon.setHorizontalAlignment(JLabel.CENTER);
        
        // Adding components
        add(comingSoon, BorderLayout.CENTER);
    }
}
