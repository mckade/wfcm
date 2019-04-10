package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

@SuppressWarnings("serial")
public class _JSlider extends JSlider {
    
    public _JSlider(int min, int max, int value) {
        super(min, max, value);
    }
    
    // Updates visuals.
    public void updateVisuals() {
        setBackground(Visuals.C_PANEL_BACKGROUND);
        setUI(new BasicSliderUI(this) {
            public void paintFocus(Graphics g) {}
            public void paintThumb(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Rectangle t = thumbRect;
                g2.setColor(Visuals.C_COMPONENT_PART1);
                g2.fillRect(t.x, t.y, t.width-3, t.height);
                g2.setColor(Visuals.C_COMPONENT_BORDER);
                g2.drawRect(t.x, t.y, t.width-3, t.height-1);
            }
            public void paintTrack(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Rectangle t = trackRect;
                g2.setColor(Visuals.C_COMPONENT_PART1);
                g2.fillRect(t.x, t.y+7, t.width, t.height/5+1);
                g2.setColor(Visuals.C_COMPONENT_BORDER);
                g2.drawRect(t.x, t.y+7, t.width, t.height/5+1);
            }
        });
    }
}
