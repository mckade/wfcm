package gui;

import java.awt.*;

public class VisualizerGraphics extends Canvas {
    private int rowHeight = 10;
    private Rectangle[] notes;

    public VisualizerGraphics()
    {
        int size = 100;
        notes = new Rectangle[size];
        for(int x = 0; x < size; x++)
        {
            notes[x] = new Rectangle(x*rowHeight, rowHeight*x, 3*rowHeight, rowHeight);
        }
    }

    public VisualizerGraphics(Rectangle[] notes)
    {
        this.notes = notes;
    }

    public void setNotes(Rectangle[] notes)
    {
        System.out.println("Set " + notes.length + " notes");
        this.notes = notes;
        repaint();
    }

    public void paint(Graphics g)
    {
        for(int i = 0; i < notes.length; i++)
        {
            int x = notes[i].x;
            int y = 800 - notes[i].y * rowHeight;
            int w = notes[i].width;
            int h = rowHeight;
            g.setColor(Color.white);
            g.fillRect(x, y, w, h);
            g.setColor(Color.black);
            g.drawRect(x, y, w, h);
            System.out.println("Drawing: " + x +", "+ y +", "+ w +", "+ h);
        }
    }
}
