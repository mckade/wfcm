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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class VisualizerPanel extends JPanel {
    
    // Components
    private JTable visualizer;
    private VisualizerModel model;

    // Constructor
    public VisualizerPanel() {
        
        // Setup
        setBackground(MainWindow.BACKGROUND);
        setBorder(MainWindow.BORDER);
        setLayout(new BorderLayout());
        
        // Creating components
        model = new VisualizerModel();
        visualizer = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(visualizer);
        
        // Adding components
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void setNotes() {
        
    }
}
