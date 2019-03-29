package gui;

import java.awt.*;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class VisualizerGraphics extends JPanel {
    
    private int rowHeight = 10;
    private Rectangle[] notes;
    
    public VisualizerGraphics() {
        setBackground(MainWindow.PANEL_BACKGROUND);
    }

    public void setNotes(Rectangle[] notes)
    {
        System.out.println("Set " + notes.length + " notes");
        this.notes = notes;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (notes == null) return;
        int x, y, w, h;
        for(Rectangle note: notes) {
            x = note.x;
            y = 800 - note.y * rowHeight;
            w = note.width;
            h = rowHeight;
            g2.setColor(Color.white);
            g2.fillRect(x, y, w, h);
            g2.setColor(Color.black);
            g2.drawRect(x, y, w, h);
        }
    }
}
