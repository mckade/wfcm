/**
 * @filename LeftPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * The left half of the main window.
 * Consists of the buttons and log.
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import coms.UpdateEvent;
import coms.UpdateListener;

@SuppressWarnings("serial")
public class LeftPanel extends JPanel {
    
    // Minimum width of the panel.
    public static final int WIDTH = 200;
    
    // Holds listeners to send events to.
    private ArrayList<UpdateListener> listeners;
    
    // Panels
    private ButtonPanel buttonPanel;
    private LogPanel logPanel;
    private JSplitPane splitPane;
    
    // Visible Panels
    private boolean buttonVis = true;
    private boolean logVis = true;

    // Constructor
    public LeftPanel() {
        
        // Setup
        listeners = new ArrayList<UpdateListener>();
        Dimension dim = new Dimension();
        dim.width = WIDTH;
        dim.height = WIDTH;
        
        // Panel settings
        setPreferredSize(dim);
        setMinimumSize(dim);
        
        // Layout and panels
        setLayout(new BorderLayout());
        
        // Creating panels
        buttonPanel = new ButtonPanel(dim);
        logPanel = new LogPanel(dim);
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buttonPanel, logPanel);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitPane.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {}
                    public void paint(Graphics g) {
                        g.setColor(new Color(0, 62, 52));
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });
        
        // Adding panels
        add(splitPane, BorderLayout.CENTER);
    }
    
    // Toggles the visibility of the buttons panel.
    public void toggleButtonPanel() {
        buttonPanel.setVisible(buttonVis = !buttonVis);
        if (buttonVis) {
            // Resetting buttons/log divider position
            splitPane.setDividerLocation((int)getMinimumSize().getHeight());
        }
        
        // Checking if both panels are invisible
        if (!buttonVis && !logVis) {
            setVisible(false);
        }
        // Checking if left panel is now visible
        else if (buttonVis && !logVis){
            setVisible(true);
            fireUpdateEvent(this);
        }
    }
    
    // Toggles the visibility of the log panel.
    public void toggleLogPanel() {
        logPanel.setVisible(logVis = !logVis);
        if (logVis) {
            // Resetting buttons/log divider position
            splitPane.setDividerLocation((int)getMinimumSize().getHeight());
        }
        
        // Checking if both panels are invisible
        if (!buttonVis && !logVis) {
            setVisible(false);
        }
        // Checking if left panel is now visible
        else if (!buttonVis && logVis){
            setVisible(true);
            fireUpdateEvent(this);
        }
    }
    
    // Adds a listener to the list to send events to.
    public void addUpdateListener(UpdateListener listener) {
        listeners.add(listener);
    }
    
    // Sends out an event to reset the left/right panel divider position
    private void fireUpdateEvent(Object source) {
        for (UpdateListener listener : listeners) {
            listener.updateCalled(new UpdateEvent(source));
        }
    }
}
