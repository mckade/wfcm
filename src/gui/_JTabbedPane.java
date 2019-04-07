package gui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

@SuppressWarnings("serial")
public class _JTabbedPane extends JTabbedPane {

    public _JTabbedPane() {
        super();
    }
    
    // Updates visuals.
    public void updateVisuals() {
        setBackground(Visuals.C_COMPONENT_BACKGROUND);
        setForeground(Visuals.C_FONTCOLOR2);
        setBorder(Visuals.B_PANEL_BORDER);
        setUI(new BasicTabbedPaneUI() {
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
                    Rectangle iconRect, Rectangle textRect, boolean isSelected) {}
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                    boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(tabPane.getBackground());
                g2.fillRect(x, y, w, h);
            }
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                    boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(2));
                if(getFocusIndex() == tabIndex) {
                    g2.setColor(Visuals.C_BORDER_CLICKED);
                    g2.drawRect(x, y, w, h);
                }
                else {
                    g2.setColor(Visuals.C_BORDER_OUTER);
                    g2.drawRect(x, y-1, w, h);
                }
            }
        });
    }
}
