/**
 * @filename VisualizerGraphics.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Draws the note table, and notes onto the component.
 */

package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import coms.UpdateEvent;
import coms.UpdateListener;
import coms.UpdateType;

@SuppressWarnings("serial")
public class VisualizerGraphics extends JComponent {
    
    // Block size for eighth note.
    private static final int EIGHTH = 50;
    
    // Notes
    private String[] noteHeaders;   // note headers
    private Rectangle[] notes;      // literal
    
    // Table
    private int rowHeight = 15;
    private Dimension dim;
    
    // Listener to send events to
    private UpdateListener listener;
    
    // Constructor
    public VisualizerGraphics(UpdateListener listener) {
        // Setup
        this.listener = listener;
        dim = new Dimension(0, (rowHeight+1)*88);
        setPreferredSize(dim);
        
        // Creating note row headers C7 to A-
        String[] noteTypes = {"C", "B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#"};
        noteHeaders = new String[88];
        int num = 7;
        int index = 0;
        for (int i = 0; i < 88; i++) {
            noteHeaders[i] = noteTypes[index] + ((num >= 0)? num : "-");
            if (index == 0) num--;
            index = (index + 1) % noteTypes.length;
        }
    }

    // Setting the notes to draw on the table.
    public void setNotes(Rectangle[] notes) {
        this.notes = notes;
        
        // Getting the x dimension of the table.
        // This is so we are able to scroll through all the notes.
        int maxX = 0;
        for (Rectangle note: notes) {
            if (note.x + note.width > maxX) {
                maxX = note.x + note.width;
            }
        }
        dim.width = maxX;
        
        // Updating scrollbar and repainting.
        listener.updateEvent(new UpdateEvent(this, UpdateType.scrollBar));
        repaint();
    }

    // Painting the component
    // Table and notes
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int x, y, w, h;
        
        // Absolute Position
        x = 0;
        y = 0;
        
        // Drawing Background
        g2.setColor(MainWindow.PANEL_BACKGROUND);
        g2.fillRect(x, y, getWidth(), getHeight());
        
        // Drawing table grid lines
        w = getWidth() + EIGHTH*8;
        for (int i = 0; i < w; i += EIGHTH) {
            if (i % (EIGHTH*8) == 0) {
                // Measure
                g2.setColor(MainWindow.DIVIDER);
                g2.drawLine(x, y, x, getHeight());
            }
            else {
                // Eight when we want
                //g2.setColor(MainWindow.COMPONENT_BACKGROUND);
                //g2.drawLine(x, y, x, getHeight());
            }
            x = i;
        }
        
        // Drawing notes if set.
        // Reflects notes over x-axis, and then moves them down
        // by a magic constant to be in the correct row.
        if (notes != null) {
            for(Rectangle note: notes) {
                x = note.x + EIGHTH + 1;
                y = -note.y * (rowHeight + 1) + 1536;
                w = note.width;
                h = rowHeight - 1;
                g2.setColor(MainWindow.COMPONENT_BORDER_INNER);
                g2.fillRect(x, y, w, h);
                g2.setColor(Color.black);
                g2.drawRect(x, y, w, h);
            }
        }
        
        // Relative Position
        x = -getX();
        y = 0;
        
        // Drawing Table note headers
        // Ensures the note headers are always showing.
        for (int i = 0; i < 88; i++) {
            g2.setColor(MainWindow.COMPONENT_BACKGROUND);
            g2.fillRect(x, y, EIGHTH, rowHeight);
            g2.drawLine(x+EIGHTH, y-1, x + getWidth(), y-1);
            g2.setColor(MainWindow.COMPONENT_BORDER_INNER);
            g2.drawString(noteHeaders[i], x+15, y+(rowHeight*4/5));
            y += rowHeight + 1;
        }
    }
}
