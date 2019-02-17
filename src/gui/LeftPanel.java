/**
 * @filename LeftPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * The left half of the main window.
 * Consists of the buttons and log.
 * 
 * Can toggle the visibility of both the
 * buttons and log panels
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

import coms.ButtonListener;
import coms.UpdateEvent;
import coms.UpdateListener;
import coms.UpdateType;

@SuppressWarnings("serial")
public class LeftPanel extends JPanel {
    
    // Minimum width of the panel.
    public static final int MINWIDTH = 200;
    
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
        Dimension dim = new Dimension(MINWIDTH, MINWIDTH);
        
        // Panel settings
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
        updateDividers(buttonVis);
    }
    
    // Toggles the visibility of the log panel.
    public void toggleLogPanel() {
        logPanel.setVisible(logVis = !logVis);
        updateDividers(logVis);
    }
    
    // Updates the button/log divider and left/right divider
    // Based on visible panes
    // @flag: Determines whether
    // - Left panel was invisible but now visible (true)
    // - Left panel was visible and still visible (false)
    private void updateDividers(boolean flag) {
        
        // Updating button/log divider
        // If both panels are now visible then resets divider
        // Otherwise removes divider
        if (buttonVis && logVis) {
            splitPane.setDividerLocation((int)getMinimumSize().getHeight());
            splitPane.setDividerSize(10);
        }
        else {
            splitPane.setDividerSize(0);
        }
        
        // Updating left/right divider
        // If left panel is already visible, do nothing
        // If left panel is now invisible fire update event to remove divider
        // If left panel is now visible fire update event to reset divider
        if (buttonVis && logVis) {} // Do nothing
        else if (!buttonVis && !logVis) {
            setVisible(false);
            fireUpdateEvent(this, UpdateType.invisible);
        }
        else if (flag) {
            setVisible(true);
            fireUpdateEvent(this, UpdateType.visible);
        }
    }
    
    public int getNoteLength() {
        return buttonPanel.getNoteLength();
    }
    
    public void addLog(String log) {
        logPanel.addLog(log);
    }
    
    // Adds a listener to the list to send events to.
    public void addUpdateListener(UpdateListener listener) {
        listeners.add(listener);
    }
    
    public void addButtonListener(ButtonListener listener) {
        buttonPanel.addButtonListener(listener);
    }
    
    // Sends out an event to reset the left/right panel divider position
    private void fireUpdateEvent(Object source, UpdateType updateType) {
        for (UpdateListener listener : listeners) {
            listener.updateEvent(new UpdateEvent(source, updateType));
        }
    }
}
