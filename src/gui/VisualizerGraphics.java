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
    
    private static final int EIGHTH = 50;
    
    private String[] noteHeaders = {"C", "B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#"};
    private String[] rowHeaders;
    private Rectangle[] notes;
    private int rowHeight = 15;
    private Dimension dim;
    
    private UpdateListener listener;
    
    public VisualizerGraphics(UpdateListener listener) {
        this.listener = listener;
        dim = new Dimension(0, (rowHeight+1)*88);
        setPreferredSize(dim);
        
        // Creating note row headers
        rowHeaders = new String[88];
        int num = 7;
        int index = 0;
        for (int i = 0; i < 88; i++) {
            rowHeaders[i] = noteHeaders[index] + ((num >= 0)? num : "-");
            if (index == 0) num--;
            index = (index + 1) % noteHeaders.length;
        }
    }

    public void setNotes(Rectangle[] notes) {
        this.notes = notes;
        //System.out.println("Added " + notes.length + " notes");
        int maxX = 0;
        for (Rectangle note: notes) {
            if (note.x + note.width > maxX) {
                maxX = note.x + note.width;
            }
            //System.out.println("Rect: " + note.x + ", " + note.y + ", " + note.width + ", " + note.height);
        }
        dim.width = maxX;
        listener.updateEvent(new UpdateEvent(this, UpdateType.scrollBar));
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int x, y, w, h;
        Color fore = new Color(14, 184, 152);
        
        // Absolute Position
        x = 0;
        y = 0;
        
        // Drawing Background
        g2.setColor(MainWindow.PANEL_BACKGROUND);
        g2.fillRect(x, y, getWidth(), getHeight());
        
        // Drawing vertical grid lines (measures)
        w = getWidth() + EIGHTH*8;
        g2.setColor(MainWindow.COMPONENT_BACKGROUND);
        for (int i = 0; i < w; i += EIGHTH*8) {
            g2.drawLine(x, y, x, getHeight());
            x = i;
        }
        
        // Drawing notes if set
        if (notes != null) {
            for(Rectangle note: notes) {
                x = note.x + EIGHTH + 1;
                y = -note.y * (rowHeight + 1) + 1536; // 1536, the magic constant.
                w = note.width;
                h = rowHeight - 1;
                g2.setColor(fore);
                g2.fillRect(x, y, w, h);
                g2.setColor(Color.black);
                g2.drawRect(x, y, w, h);
            }
        }
        
        // Relative Position
        x = -getX();
        y = 0;
        
        // Drawing Table
        for (int i = 0; i < 88; i++) {
            g2.setColor(MainWindow.COMPONENT_BACKGROUND);
            g2.fillRect(x, y, EIGHTH, rowHeight);
            g2.drawLine(x+EIGHTH, y-1, x + getWidth(), y-1);
            g2.setColor(fore);
            g2.drawString(rowHeaders[i], x+15, y+(rowHeight*4/5));
            y += rowHeight + 1;
        }
    }
}
