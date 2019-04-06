package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class Visuals {
    public static final Color C_BORDER_OUTER = new Color(0, 92, 75);
    public static final Color C_BORDER_INNER = new Color(8, 145, 119);
    public static final Color C_BORDER_HOVERED = new Color(80, 55, 110);
    public static final Color C_BORDER_CLICKED = new Color(180, 76, 180);
    public static final Color C_DIVIDER = new Color(0, 62, 52);
    // Panel visuals
    public static final Color C_PANEL_BACKGROUND = new Color(0, 6, 5);
    public static final Border B_PANEL_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4,4,4,4),
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(C_BORDER_OUTER, 2),
                    BorderFactory.createLineBorder(C_BORDER_INNER, 2)));
    // Component visuals
    public static final Color C_COMPONENT_BACKGROUND = new Color(0, 40, 32);
    public static final Color C_COMPONENT_BORDER = new Color(14, 184, 152);
    public static final Border B_COMPONENT_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Visuals.C_BORDER_OUTER, 2),
            BorderFactory.createLineBorder(Visuals.C_BORDER_INNER, 2));
    // Font visuals
    public static final Font F_HEADING1 = new Font(Font.DIALOG, Font.PLAIN, 16);
    public static final Font F_HEADING2 = new Font(Font.DIALOG, Font.PLAIN, 12);
    public static final Color C_FONTCOLOR1 = Color.WHITE;
    public static final Color C_FONTCOLOR2 = new Color(0,0,0);
    // Tab
    public static final Border B_BORDER_TAB = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, C_DIVIDER),
            BorderFactory.createEmptyBorder(5, 0, 0, 0));
    
    // Custom UI/Component visuals.
    public static BasicArrowButton createBasicArrowButton(int orientation) {
        return new BasicArrowButton(orientation, 
                Visuals.C_COMPONENT_BORDER,
                Visuals.C_BORDER_OUTER,
                Visuals.C_BORDER_OUTER,
                Visuals.C_COMPONENT_BORDER);
    }
    
    public static BasicScrollBarUI createScrollBarUI() {
        BasicScrollBarUI ui = new BasicScrollBarUI() {
            protected JButton createDecreaseButton(int orientation) {
                return Visuals.createBasicArrowButton(orientation);
            }
            protected JButton createIncreaseButton(int orientation) {
                return Visuals.createBasicArrowButton(orientation);
            }
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g;
                Rectangle t = thumbRect;
                g2.setColor(Visuals.C_COMPONENT_BORDER);
                g2.fillRect(t.x, t.y, t.width, t.height);
            }
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g;
                Rectangle t = trackRect;
                g2.setColor(Visuals.C_COMPONENT_BACKGROUND);
                g2.fillRect(t.x, t.y, t.width, t.height);
            }
        };
        return ui;
    }
}
