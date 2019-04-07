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
    private JScrollPane scrollPane;

    // Constructor
    public LogPanel() {
        // Setup
        setLayout(new BorderLayout());
        
        // Creating components
        // Label
        title = new JLabel("Log", JLabel.CENTER);
        // Text
        text = new JTextArea();
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        setUpMouselistener();
        // Scroll pane
        scrollPane = new JScrollPane(text);
        
        // Adding components
        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // Updates visuals.
    public void updateVisuals() {
        setBackground(Visuals.C_PANEL_BACKGROUND);
        setBorder(Visuals.B_PANEL_BORDER);
        title.setFont(Visuals.F_HEADING1);
        title.setForeground(Visuals.C_FONTCOLOR1);
        text.setFont(Visuals.F_BODY);
        text.setBackground(Visuals.C_PANEL_BACKGROUND);
        text.setForeground(Visuals.C_FONTCOLOR1);
        scrollPane.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Visuals.C_BORDER_OUTER));
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
