/**
 * @filename LogPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Log panel that records all relevant events of the music generation process.
 * Provides feedback to the user.
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class LogPanel extends JPanel {
    
    // Components
    private JLabel title;
    private JTextArea text;
    private JPopupMenu menu;
    private JMenuItem clear;

    // Constructor
    public LogPanel() {
        // Setup
        setBackground(Visuals.C_PANEL_BACKGROUND);
        setBorder(Visuals.B_PANEL_BORDER);
        setLayout(new BorderLayout());
        
        // Creating components
        // Label
        title = new JLabel("Log", JLabel.CENTER);
        title.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        title.setForeground(Color.WHITE);
        // Text
        text = new JTextArea();
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
        text.setBackground(this.getBackground());
        text.setForeground(Color.WHITE);
        setUpMouselistener();
        
        // Creating text scroll pane
        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(0, 92, 75)));
        
        // Adding components
        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);      
    }
    
    // Adds a log
    public void addLog(String log) {
        text.append("- " + log + '\n');
    }
    
    // Sets up the mouse listener over the log.
    // This allows a right click to give a popup menu.
    // This menu then allows the user to clear the log.
    private void setUpMouselistener() {
        // Creating pop up menu
        menu = new JPopupMenu();
        clear = new JMenuItem("Clear Log");
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                text.setText("");
            }
        });
        menu.add(clear);
        
        // Adding mouse listener
        text.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
}
