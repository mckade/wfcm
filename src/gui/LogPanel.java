/**
 * @filename LogPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Log panel that records all relevant events
 * of the music generation process.
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class LogPanel extends JPanel {
    
    // Components
    private JLabel title;
    private JTextArea text;

    public LogPanel(Dimension dim) {
        
        // Panel settings
        setMinimumSize(dim);
        setBackground(MainWindow.BACKGROUND);
        setBorder(MainWindow.BORDER);
        
        // Layout and components
        setLayout(new BorderLayout());
        
        // Creating Log Label
        title = new JLabel("Log");
        title.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(JLabel.CENTER);
        
        // Creating Log Text Area
        text = new JTextArea();
        text.setEditable(false);
        text.setLineWrap(true);
        text.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
        text.setBackground(this.getBackground());
        text.setForeground(Color.WHITE);
        
        // Creating text area scroll pane
        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(0, 92, 75)));
        
        // Adding components
        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);      
    }
    
    // Adds log to the log text area.
    public void addLog(String log) {
        text.append(log + "\n");
    }
}
