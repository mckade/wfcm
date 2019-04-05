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

import coms.SettingsListener;
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
    private boolean beat = true;    // Whether or not to draw the beats.
    
    // Listener to send events to
    private UpdateListener ulistener;
    private SettingsListener slistener;
    
    // Constructor
    public VisualizerGraphics(UpdateListener ulistener, SettingsListener slistener) {
        // Setup
        this.ulistener = ulistener;
        this.slistener = slistener;
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
        
        setUpMouseListener();
    }
    
    // Sets up the mouse listener to the visualizer.
    private void setUpMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            // TEMP: Sets the play line at mouse position.
            public void mouseClicked(MouseEvent e) {
                double t1 = e.getX()-rowWidth;
                double t2 = dim.width;
                t1 = t1/t2;
                setPlayLine(t1);
                slistener.setPlayTime(t1);
            }
        });
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
        ulistener.updateEvent(new UpdateEvent(this, UpdateType.scrollBar));
        repaint();
    }
    
    // Sets the position of the playLine.
    // @percentage, the percentage of the music that has been played.
    public void setPlayLine(double percentage) {
        playLine = (int) (percentage * dim.width);
        repaint();
    }
    
    // Sets the scale of the visualizer.
    // Ranges from 10% to 200% (.1-2)
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
        
        // Drawing Background
        x = 0;
        y = 0;
        g2.setColor(MainWindow.PANEL_BACKGROUND);
        g2.fillRect(x, y, getWidth(), getHeight());
        
        // Drawing row grid lines.
        g2.setColor(MainWindow.DIVIDER.darker());
        for (i = 0; i < 88; i++) {
            g2.drawLine(x, y-1, getWidth(), y-1);
            y += rowHeight + 1;
        }
        
        // Drawing measure and beat grid lines if enabled.
        if (measure || beat) {
            y = 0;
            g2.setColor(MainWindow.DIVIDER.darker());
            for (i = rowWidth; i < getWidth(); i += rowWidth) {
                x += rowWidth*2;
                if (measure && i % (rowWidth*timeSignature) == 0) {
                    // Measure
                    g2.setColor(MainWindow.DIVIDER.brighter());
                    g2.drawLine(x, y, x, getHeight());
                    x += 1;
                    g2.drawLine(x, y, x, getHeight());
                    g2.setColor(MainWindow.DIVIDER.darker());
                }
                else if (beat) {
                    // Beat
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
        
        // Drawing play line.
        y = 0;
        g2.setColor(MainWindow.BORDER_CLICKED);
        g2.drawLine(playLine+rowWidth, y, playLine+rowWidth, getHeight());
        
        // Drawing Table row headers
        // Ensures the row headers are always showing.
        x = -getX();
        for (i = 0; i < 88; i++) {
            g2.setColor(MainWindow.COMPONENT_BACKGROUND);   // Header Block
            g2.fillRect(x, y, rowWidth, rowHeight);
            g2.setColor(MainWindow.PANEL_BACKGROUND);       // Header Separator
            g2.drawLine(x, y-1, x+rowWidth-1, y-1);
            g2.setColor(MainWindow.COMPONENT_BORDER_INNER); // Header Text
            g2.drawString(noteHeaders[i], x+15, y+(rowHeight*4/5));
            y += rowHeight + 1;
        }
    }
}
