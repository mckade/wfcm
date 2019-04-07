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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

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
    private double scale = 1;
    private double maxScale = 10;
    private double minScale = .1;
    private double inc = .1;
    private double maxInc = 1;
    private double minInc = .01;

    
    // Control
    private boolean measure = true;     // Whether or not to draw the measures
    private boolean beat = true;        // Whether or not to draw the beats.
    private boolean pressed = false;    // Whether or not the mouse is pressed down.
    private boolean dragging = false;   // Whether or not the mouse has been dragged.
    
    // Constructor
    public VisualizerGraphics(UpdateListener ulistener, SettingsListener slistener) {
        // Setup
        this.ulistener = ulistener;
        this.slistener = slistener;
        dim = new Dimension(getWidth()*2, (rowHeight+1)*89);
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
        MouseAdapter ma = new MouseAdapter() {
            double playTime;
            double point;
            int scrollAmount;
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
            public void mouseDragged(MouseEvent e) {
                if (e.getY()+getY() > rowHeight && !dragging) return;
                dragging = true;
                point = (e.getX()-rowWidth);
                if (point >= noteArea) {        // Clicked pass playable notes
                    playTime = 1;
                    slistener.setPlayTime(playTime);
                }
                else if (point <= 0) {          // Clicked before playable notes
                    playTime = 0;
                    slistener.setPlayTime(playTime);
                }
                else {                          // Clicked within playable notes
                    playTime = point/noteArea;
                    slistener.setPlayTime(playTime);
                }
                updatePlayLine(playTime);
            }
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
            public void mousePressed(MouseEvent e) {
                pressed = true;
                mouseDragged(e);
            }
            public void mouseReleased(MouseEvent e) {
                if (pressed && dragging) {
                    slistener.setPlayTime(playTime);
                    slistener.skipMusicPlayTime();
                }
                pressed = false;
                dragging = false;
            }
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    if (e.getWheelRotation() > 0)
                        decrementScale();
                    else
                        incrimentScale();
                    calculateNoteArea();
                }
                else
                    getParent().dispatchEvent(e);   // Let the scroller take care of it.
            }
        };
        
        addMouseListener(ma);
        addMouseMotionListener(ma);
        addMouseWheelListener(ma);
    }
    
    // Increments the scale
    private void incrimentScale() {
        calculateInc();
        if (scale+inc < maxScale)
            scale+=inc;
        else
            scale = maxScale;
    }
    
    // Decrements the scale
    private void decrementScale() {
        calculateInc();
        if (scale-inc > minScale)
            scale-=inc;
        else
            scale = minScale;
    }
    
    // Calculates the increment based on the current scale
    private void calculateInc() {
        inc = scale*(maxInc + minInc)/10;
    }
    
    // Calculates the note area for the notes.
    private void calculateNoteArea() {
        noteArea = 0;
        if (notes != null) {
            for (Rectangle note: notes) {
                if (note.x + note.width > noteArea) {
                    noteArea = note.x + note.width;
                }
            }
        }
        noteArea *= scale;
        dim.width = noteArea + (int) (1000*scale);  // Adding a little extra scroll room
        ulistener.updateEvent(new UpdateEvent(this, UpdateType.scrollBar));
        updatePlayLine();
    }
    
    // Updates the visualizer.
    public void updateVisualizer() {
        notes = slistener.getNotes();
        calculateNoteArea();
    }
    
    // Updates the position of the play line.
    // If music is playing doesn't update playline if the mouse is pressed.
    public void updatePlayLine() {
        if (pressed && dragging) return;
        playLine = (int) (slistener.getPlayTime() * noteArea);
        repaint();
    }
    
    // Updates the positition of the plane line forcibly.
    private void updatePlayLine(double playTime) {
        playLine = (int) (playTime * noteArea);
        repaint();
    }

    // Painting the component
    // Table and notes
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int x, y, w, h, i;
        
        // Drawing Background (not affected by scale)
        x = 0;
        y = 0;
        g2.setColor(Visuals.C_PANEL_BACKGROUND);
        g2.fillRect(x, y, getWidth(), getHeight());
        
        // Drawing row grid lines. (not affected by scale)
        y = rowHeight+1;
        g2.setColor(Visuals.C_DIVIDER.darker());
        for (i = 0; i < 89; i++) {
            g2.drawLine(x, y-1, getWidth(), y-1);
            y += rowHeight + 1;
        }
        
        // Drawing measure and beat grid lines if enabled. (Affected by scale)
        if (measure || beat) {
            y = 0;
            g2.setColor(Visuals.C_DIVIDER.darker());
            for (i = 0; i < getWidth()/scale; i += rowWidth) {
                x = (int) (i*scale) + rowWidth;
                if (measure && (i % (rowWidth*slistener.getTimeSignature()) == 0
                               || slistener.getTimeSignature() == 1)) {
                    // Measure
                    g2.setColor(Visuals.C_DIVIDER.brighter());
                    g2.drawLine(x, y, x, getHeight());
                    g2.drawLine(x+1, y, x+1, getHeight());
                    g2.setColor(Visuals.C_DIVIDER.darker());
                }
                else if (beat)
                    g2.drawLine(x, y, x, getHeight());
            }
        }
        
        // Drawing notes if set. (Affected by scale)
        // Reflects notes over x-axis, and then moves them down
        // by a magic constant to be in the correct row.
        if (notes != null) {
            for(Rectangle note: notes) {
                x = (int) (note.x*scale) + rowWidth+1;
                y = -note.y * (rowHeight+1) + (rowHeight+1)*97;
                w = (int) (note.width*scale);
                h = rowHeight - 1;
                g2.setColor(Visuals.C_COMPONENT_BORDER);
                g2.fillRect(x, y, w, h);
                g2.setColor(Color.black);
                g2.drawRect(x, y, w, h);
            }
        }
        
        // Drawing play line. (Affected by scale indirectly through noteArea)
        x = playLine + rowWidth;
        y = 0;
        g2.setColor(Visuals.C_BORDER_CLICKED);
        g2.drawLine(x, y, x, getHeight());
        
        // Drawing Table row headers (Not affected by scale)
        // Ensures the row headers are always showing.
        x = -getX();
        y = rowHeight+1;
        for (i = 0; i < 88; i++) {
            g2.setColor(Visuals.C_COMPONENT_BACKGROUND);   // Header Block
            g2.fillRect(x, y, rowWidth, rowHeight);
            g2.setColor(Visuals.C_PANEL_BACKGROUND);       // Header Separator
            g2.drawLine(x, y-1, x+rowWidth-1, y-1);
            g2.setColor(Visuals.C_COMPONENT_BORDER); // Header Text
            g2.drawString(noteHeaders[i], x+15, y+(rowHeight*4/5));
            y += rowHeight+1;
        }
        
        // Drawing Table column header (Not affected by scale)
        x = 0;
        y = -getY();
        g2.setColor(Visuals.C_COMPONENT_BACKGROUND);
        g2.fillRect(x, y, getWidth(), rowHeight);
        
        // Drawing playLine thumb (Affected by scale indirectly through noteArea)
        y = -getY();
        g2.setColor(Visuals.C_BORDER_CLICKED);
        g2.fillOval(playLine + (rowWidth-rowHeight/2), y, rowHeight, rowHeight);
    }

    public int getNoteArea() {
        return noteArea;
    }
}
