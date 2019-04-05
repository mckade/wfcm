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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import coms.UpdateEvent;
import coms.UpdateListener;
import coms.UpdateType;

@SuppressWarnings("serial")
public class VisualizerGraphics extends JComponent {
    
    // Notes
    private String[] noteHeaders;   // note headers
    private Rectangle[] notes;      // literal
    
    // Table
    private int rowHeight = 15;
    private int rowWidth = 50;
    private Dimension dim;
    private int playLine = 0;
    private double scale = 1;   // Scale of visualizer grid.
    private int timeSignature = 4;
    
    // Control
    private boolean measure = true; // Whether or not to draw the measures
    private boolean beat = false;   // Whether or not to draw the beats.
    
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
    
    // Sets the position of the playLine.
    // @percentage, the percentage of the music that has been played.
    public void setPlayLine(double percentage) {
        playLine = (int) (percentage * dim.width);
        repaint();
    }
    
    // Sets the scale of the visualizer.
    // Ranges from 10% to 100% (.1-1)
    public void setScale(double percentage) {
        if (percentage >= .1 && percentage <= 1) {
            scale = percentage;
        }
    }
    
    // Sets the time signature of the visualizer.
    public void setTimeSignature(int timeSignature) {
        this.timeSignature = timeSignature;
    }

    // Painting the component
    // Table and notes
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int x, y, w, h, i;
        
        // Absolute Position
        x = 0;
        y = 0;
        
        // Drawing Background
        g2.setColor(MainWindow.PANEL_BACKGROUND);
        g2.fillRect(x, y, getWidth(), getHeight());
        
        // Drawing row grid lines.
        
        for (i = 0; i < 88; i++) {
            
        }
        
        // Drawing measure and beat grid lines if enabled.
        if (measure || beat) {
            for (i = rowWidth; i < getWidth()+rowWidth; i += rowWidth) {
                x += rowWidth*2;
                if (measure && i % (rowWidth*timeSignature) == 0) {
                    // Measure
                    g2.setColor(MainWindow.DIVIDER.brighter());
                    g2.drawLine(x, y, x, getHeight());
                    x+=1;
                    g2.drawLine(x, y, x, getHeight());
                }
                else if (!beat) {
                    // Beat
                    g2.setColor(MainWindow.DIVIDER.darker());
                    g2.drawLine(x, y, x, getHeight());
                }
                x = i;
            }
        }
        
        // Drawing notes if set.
        // Reflects notes over x-axis, and then moves them down
        // by a magic constant to be in the correct row.
        if (notes != null) {
            for(Rectangle note: notes) {
                x = note.x + rowWidth + 1;
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
        for (i = 0; i < 88; i++) {
            g2.setColor(MainWindow.COMPONENT_BACKGROUND);
            g2.fillRect(x, y, rowWidth, rowHeight);
            g2.drawLine(x+rowWidth, y-1, x + getWidth(), y-1);
            g2.setColor(MainWindow.COMPONENT_BORDER_INNER);
            g2.drawString(noteHeaders[i], x+15, y+(rowHeight*4/5));
            y += rowHeight + 1;
        }
        
        // Drawing play line.
        y = 0;
        g2.setColor(MainWindow.BORDER_CLICKED);
        g2.drawLine(playLine+rowWidth, y, playLine+rowWidth, getHeight());
    }
}
