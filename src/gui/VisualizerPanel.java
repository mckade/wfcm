/**
 * @filename VisualizerPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Visualizer panel that allows for the user to interact with
 * the music generation process.
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class VisualizerPanel extends JPanel {
    
    // Components
    private VisualizerGraphics visualizer;
    
    // Constructor
    public VisualizerPanel() {
        
        // Setup
        setBackground(MainWindow.PANEL_BACKGROUND);
        setBorder(MainWindow.BORDER);
        setLayout(new BorderLayout());
        
        // Creating components
        visualizer = new VisualizerGraphics(15);
        JScrollPane scrollPane = new JScrollPane(visualizer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void setNotes(Rectangle[] notes) {
        visualizer.setNotes(notes);
    }
}
