/**
 * @filename ButtonPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Button Panel that holds the basic functions for music generation.
 * Not yet implemented
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel {
    
    JLabel comingSoon;  // Temp
    
    // Constructor
    public ButtonPanel(Dimension dim) {
        
        // Panel settings
        setMinimumSize(dim);
        setBackground(MainWindow.BACKGROUND);
        setBorder(MainWindow.BORDER);
        
        // Layout and components
        setLayout(new BorderLayout());
        
        // Creating components
        comingSoon = new JLabel("Buttons Coming Soon!");
        comingSoon.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
        comingSoon.setForeground(Color.WHITE);
        comingSoon.setHorizontalAlignment(JLabel.CENTER);
        
        // Adding components
        add(comingSoon, BorderLayout.CENTER);
    }
}
