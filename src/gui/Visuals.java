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
    public static Color C_DIVIDER1;
    public static Color C_DIVIDER2;
    public static Color C_DIVIDER3;
    public static Color C_ARROW_COLOR;
    public static Color C_ARROW_SHADEING;
    public static Color C_TRACK;
    public static Color C_THUMB;
    public static Color C_PLAYLINE;
    public static Color C_NOTE;
    public static Color C_NOTE_SHADING;
    public static Color C_NOTE_LOCKED;
    // Panel visuals
    public static Color C_PANEL_BACKGROUND;
    public static Border B_PANEL_BORDER;
    // Component visuals
    public static Color C_COMPONENT_BACKGROUND;
    public static Color C_COMPONENT_BORDER;
    public static Color C_COMPONENT_PART1;
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
        C_BORDER_HOVERED = new Color(180, 76, 180);
        C_BORDER_CLICKED = C_BORDER_HOVERED;
        C_DIVIDER1 = new Color(0, 62, 52);
        C_DIVIDER2 = new Color(0, 43, 36);
        C_DIVIDER3 = new Color(0, 98, 82);
        C_ARROW_COLOR = new Color(9, 121, 100);
        C_ARROW_SHADEING = new Color(0, 40, 32);
        C_TRACK = C_ARROW_SHADEING;
        C_THUMB = new Color(14, 184, 152);
        C_PLAYLINE = C_BORDER_CLICKED;
        C_NOTE = C_THUMB;
        C_NOTE_SHADING = Color.black;
        C_NOTE_LOCKED = new Color(80, 55, 110);
        // Panel visuals
        C_PANEL_BACKGROUND = new Color(0, 6, 5);
        B_PANEL_BORDER = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4,4,4,4),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDER_OUTER, 2),
                        BorderFactory.createLineBorder(C_BORDER_INNER, 2)));
        // Component visuals
        C_COMPONENT_BACKGROUND = C_ARROW_SHADEING;
        C_COMPONENT_BORDER = C_THUMB;
        C_COMPONENT_PART1 = C_DIVIDER1;
        B_COMPONENT_BORDER = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Visuals.C_BORDER_OUTER, 2),
                BorderFactory.createLineBorder(Visuals.C_BORDER_INNER, 2));
        // Font visuals
        F_HEADING1 = new Font(Font.DIALOG, Font.PLAIN, 16);
        F_HEADING2 = new Font(Font.DIALOG, Font.PLAIN, 12);
        F_BODY = new Font(Font.DIALOG, Font.PLAIN, 14);
        F_BUTTON = F_HEADING2;
        C_FONTCOLOR1 = Color.WHITE;
        C_FONTCOLOR2 = C_COMPONENT_BORDER;
        // Tab
        B_BORDER_TAB = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, C_DIVIDER1),
                BorderFactory.createEmptyBorder(5, 0, 0, 0));
    }
    
    // Light
    public static void setThemeLight() {
        C_BORDER_OUTER = new Color(55, 134, 205);
        C_BORDER_INNER = new Color(157, 195, 230);
        C_BORDER_HOVERED = new Color(40, 224, 224);
        C_BORDER_CLICKED = C_BORDER_HOVERED;
        C_DIVIDER1 = new Color(151, 176, 221);
        C_DIVIDER2 = C_DIVIDER1;
        C_DIVIDER3 = C_DIVIDER1;
        C_ARROW_SHADEING = C_BORDER_OUTER;
        C_ARROW_COLOR = new Color(132, 202, 232);
        C_TRACK = new Color(201, 214, 237);
        C_THUMB = new Color(115, 169, 219);
        C_PLAYLINE = new Color(68, 114, 196);
        C_NOTE = C_THUMB;
        C_NOTE_SHADING = C_BORDER_OUTER;
        C_NOTE_LOCKED = new Color(80, 55, 110);
        // Panel visuals
        C_PANEL_BACKGROUND = new Color(236, 243, 250);
        B_PANEL_BORDER = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4,4,4,4),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDER_OUTER, 2),
                        BorderFactory.createLineBorder(C_BORDER_INNER, 2)));
        // Component visuals
        new Color(180, 199, 231);
        C_COMPONENT_BACKGROUND = C_TRACK;
        C_COMPONENT_BORDER = C_THUMB;
        C_COMPONENT_PART1 = new Color(132, 202, 232);   // change
        B_COMPONENT_BORDER = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Visuals.C_BORDER_OUTER, 2),
                BorderFactory.createLineBorder(Visuals.C_BORDER_INNER, 2));
        // Font visuals
        F_HEADING1 = new Font(Font.DIALOG, Font.PLAIN, 16);
        F_HEADING2 = new Font(Font.DIALOG, Font.PLAIN, 12);
        F_BODY = new Font(Font.DIALOG, Font.PLAIN, 14);
        F_BUTTON = F_HEADING2;
        C_FONTCOLOR1 = Color.BLACK;
        C_FONTCOLOR2 = C_BORDER_OUTER.darker();
        // Tab
        B_BORDER_TAB = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, C_DIVIDER1),
                BorderFactory.createEmptyBorder(5, 0, 0, 0));
    }
    
    // Custom UI/Component visuals.
    public static BasicArrowButton createBasicArrowButton(int orientation) {
        return new BasicArrowButton(orientation, 
                Visuals.C_ARROW_COLOR,
                Visuals.C_ARROW_SHADEING,
                Visuals.C_ARROW_SHADEING,
                Visuals.C_ARROW_COLOR);
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
                g2.setColor(Visuals.C_THUMB);
                g2.fillRect(t.x, t.y, t.width, t.height);
            }
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g;
                Rectangle t = trackRect;
                g2.setColor(Visuals.C_TRACK);
                g2.fillRect(t.x, t.y, t.width, t.height);
            }
        };
        return ui;
    }
}
