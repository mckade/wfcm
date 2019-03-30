package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class VisualizerGraphics extends JComponent {
    
    private String[] noteHeaders = {"C", "B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#"};
    private String[] rowHeaders;
    private Rectangle[] notes;
    private int rowHeight;
    private Dimension dim;
    
    public VisualizerGraphics(int rowHeight) {
        this.rowHeight = rowHeight;
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
        int maxX = 0;
        for (Rectangle note: notes) {
            if (note.x + note.width > maxX) {
                maxX = note.x + note.width;
            }
        }
        dim.width = maxX;
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
        
        // Drawing notes if set
        if (notes != null) {
            for(Rectangle note: notes) {
                x = note.x + 50;
                y = 800 - note.y * (rowHeight + 1);
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
            g2.fillRect(x, y, 50, rowHeight);
            g2.drawLine(x+50, y-1, x + getWidth(), y-1);
            g2.setColor(fore);
            g2.drawString(rowHeaders[i], x+15, y+(rowHeight*4/5));
            y += rowHeight + 1;
        }
    }
}
