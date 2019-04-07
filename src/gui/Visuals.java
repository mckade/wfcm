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
    public static Color C_BORDER_OUTER;
    public static Color C_BORDER_INNER;
    public static Color C_BORDER_HOVERED;
    public static Color C_BORDER_CLICKED;
    public static Color C_DIVIDER;
    // Panel visuals
    public static Color C_PANEL_BACKGROUND;
    public static Border B_PANEL_BORDER;
    // Component visuals
    public static Color C_COMPONENT_BACKGROUND;
    public static Color C_COMPONENT_BORDER;
    public static Border B_COMPONENT_BORDER;
    // Font visuals
    public static Font F_HEADING1;
    public static Font F_HEADING2;
    public static Font F_BODY;
    public static Font F_BUTTON;
    public static Color C_FONTCOLOR1;
    public static Color C_FONTCOLOR2;
    // Tab
    public static Border B_BORDER_TAB;
    
    // Set theme
    // Dark
    public static void setThemeDark() {
        C_BORDER_OUTER = new Color(0, 92, 75);
        C_BORDER_INNER = new Color(8, 145, 119);
        C_BORDER_HOVERED = new Color(80, 55, 110);
        C_BORDER_CLICKED = new Color(180, 76, 180);
        C_DIVIDER = new Color(0, 62, 52);
        // Panel visuals
        C_PANEL_BACKGROUND = new Color(0, 6, 5);
        B_PANEL_BORDER = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4,4,4,4),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDER_OUTER, 2),
                        BorderFactory.createLineBorder(C_BORDER_INNER, 2)));
        // Component visuals
        C_COMPONENT_BACKGROUND = new Color(0, 40, 32);
        C_COMPONENT_BORDER = new Color(14, 184, 152);
        B_COMPONENT_BORDER = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Visuals.C_BORDER_OUTER, 2),
                BorderFactory.createLineBorder(Visuals.C_BORDER_INNER, 2));
        // Font visuals
        F_HEADING1 = new Font(Font.DIALOG, Font.PLAIN, 16);
        F_HEADING2 = new Font(Font.DIALOG, Font.PLAIN, 12);
        F_BODY = new Font(Font.DIALOG, Font.PLAIN, 14);
        F_BUTTON = F_HEADING1;
        C_FONTCOLOR1 = Color.WHITE;
        C_FONTCOLOR2 = new Color(0,0,0);
        // Tab
        B_BORDER_TAB = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, C_DIVIDER),
                BorderFactory.createEmptyBorder(5, 0, 0, 0));
    }
    
    // Light
    public static void setThemeLight() {
        C_BORDER_OUTER = Color.WHITE;
        C_BORDER_INNER = Color.WHITE;
        C_BORDER_HOVERED = Color.WHITE;
        C_BORDER_CLICKED = Color.WHITE;
        C_DIVIDER = Color.WHITE;
        // Panel visuals
        C_PANEL_BACKGROUND = Color.WHITE;
        B_PANEL_BORDER = BorderFactory.createLineBorder(Color.WHITE);
        // Component visuals
        C_COMPONENT_BACKGROUND = Color.WHITE;
        C_COMPONENT_BORDER = Color.WHITE;
        B_COMPONENT_BORDER = BorderFactory.createLineBorder(Color.WHITE);
        // Font visuals
        F_HEADING1 = new Font(Font.DIALOG, Font.PLAIN, 1);
        F_HEADING2 = new Font(Font.DIALOG, Font.PLAIN, 1);
        F_BODY = new Font(Font.DIALOG, Font.PLAIN, 1);
        F_BUTTON = F_HEADING1;
        C_FONTCOLOR1 = Color.WHITE;
        C_FONTCOLOR2 = Color.WHITE;
        // Tab
        B_BORDER_TAB = BorderFactory.createLineBorder(Color.WHITE);
    }
    
    // Custom UI/Component visuals.
    public static BasicArrowButton createBasicArrowButton(int orientation) {
        return new BasicArrowButton(orientation, 
                Visuals.C_COMPONENT_BORDER,
                Visuals.C_BORDER_OUTER,
                Visuals.C_BORDER_OUTER,
                Visuals.C_COMPONENT_BORDER);
    }
    
    public static BasicScrollBarUI createBasicScrollBarUI() {
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
