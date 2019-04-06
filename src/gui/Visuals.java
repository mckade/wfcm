package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

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
}
