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
    
    // Listener to send events to
    private UpdateListener ulistener;
    private SettingsListener slistener;
    
    // Notes
    private String[] noteHeaders;   // note headers
    private Rectangle[] notes;      // literal
    
    // Table
    private int rowHeight = 15;
    private int rowWidth = 50;
    private int noteArea;
    private Dimension dim;
    private int playLine = 0;
    private int timeSignature = 4;
    
    // Control
    private boolean measure = true; // Whether or not to draw the measures
    private boolean beat = true;    // Whether or not to draw the beats.
    
    // Constructor
    public VisualizerGraphics(UpdateListener ulistener, SettingsListener slistener) {
        // Setup
        this.ulistener = ulistener;
        this.slistener = slistener;
        dim = new Dimension(getWidth()*2, (rowHeight+1)*88);
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
        
        updateVisualizer();
        setUpMouseListener();
    }
    
    // Sets up the mouse listener to the visualizer.
    private void setUpMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            // Temp, moves playLine (aka changes playtime)
            public void mousePressed(MouseEvent e) {
                double point = e.getX()-rowWidth;
                if (point >= noteArea)   // Clicked pass playable notes
                {
                    slistener.setPlayTime(1);
                    slistener.onClick();
                }
                else if (point <= 0)            // Clicked before playable notes
                {
                    slistener.setPlayTime(0);
                    slistener.onClick();
                }
                else                            // Clicked within playable notes
                {
                    slistener.setPlayTime(point/noteArea);
                    slistener.onClick();
                }
                updatePlayLine();
            }
        });
    }
    
    // Updates the visualizer.
    public void updateVisualizer() {
        notes = slistener.getNotes();
        timeSignature = slistener.getTimeSignature();
        
        // Getting the x dimension of the table.
        // This is so we are able to scroll through all the notes.
        noteArea = 0;
        if (notes != null) {
            for (Rectangle note: notes) {
                if (note.x + note.width > noteArea) {
                    noteArea = note.x + note.width;
                }
            }
        }
        dim.width = noteArea + 1000;    // Adding a little extra scroll room
        
        // Updating scrollbar and repainting.
        ulistener.updateEvent(new UpdateEvent(this, UpdateType.scrollBar));
        updatePlayLine();
    }
    
    // Updates the position of the play line.
    public void updatePlayLine() {
        playLine = (int) (slistener.getPlayTime() * noteArea);
        repaint();
    }

    // Painting the component
    // Table and notes
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int x, y, w, h, i;
        
        // Drawing Background
        x = 0;
        y = 0;
        g2.setColor(MainWindow.C_PANEL_BACKGROUND);
        g2.fillRect(x, y, getWidth(), getHeight());
        
        // Drawing row grid lines.
        g2.setColor(MainWindow.C_DIVIDER.darker());
        for (i = 0; i < 88; i++) {
            g2.drawLine(x, y-1, getWidth(), y-1);
            y += rowHeight + 1;
        }
        
        // Drawing measure and beat grid lines if enabled.
        if (measure || beat) {
            y = 0;
            g2.setColor(MainWindow.C_DIVIDER.darker());
            for (i = rowWidth; i < getWidth(); i += rowWidth) {
                x += rowWidth*2;
                if (measure && i % (rowWidth*timeSignature) == 0) {
                    // Measure
                    g2.setColor(MainWindow.C_DIVIDER.brighter());
                    g2.drawLine(x, y, x, getHeight());
                    x += 1;
                    g2.drawLine(x, y, x, getHeight());
                    g2.setColor(MainWindow.C_DIVIDER.darker());
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
                g2.setColor(MainWindow.C_COMPONENT_BORDER);
                g2.fillRect(x, y, w, h);
                g2.setColor(Color.black);
                g2.drawRect(x, y, w, h);
            }
        }
        
        // Drawing play line.
        y = 0;
        g2.setColor(MainWindow.C_BORDER_CLICKED);
        g2.drawLine(playLine+rowWidth, y, playLine+rowWidth, getHeight());
        
        // Drawing Table row headers
        // Ensures the row headers are always showing.
        x = -getX();
        for (i = 0; i < 88; i++) {
            g2.setColor(MainWindow.C_COMPONENT_BACKGROUND);   // Header Block
            g2.fillRect(x, y, rowWidth, rowHeight);
            g2.setColor(MainWindow.C_PANEL_BACKGROUND);       // Header Separator
            g2.drawLine(x, y-1, x+rowWidth-1, y-1);
            g2.setColor(MainWindow.C_COMPONENT_BORDER); // Header Text
            g2.drawString(noteHeaders[i], x+15, y+(rowHeight*4/5));
            y += rowHeight + 1;
        }
    }

    public int getNoteArea() {
        return noteArea;
    }
}
