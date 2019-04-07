/**
 * @filename _Button.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Extends the basic JButton to add a little more functionality.
 * Changes how the button looks when interacted with by the user.
 */

package gui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;

@SuppressWarnings("serial")
public class _JButton extends JButton
implements MouseListener {
    
    // Border visuals
    public static Border DEFAULT;
    public static Border HOVERED;
    public static Border CLICKED;
    
    public _JButton(String text) {
        // Setup
        super(text);
        setModel(new DefaultButtonModel() {
            public boolean isPressed() {
                return false;
            }
        });
        setUI(new BasicButtonUI() {
            protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect,
                    Rectangle iconRect) {}
        });
        addMouseListener(this);
    }
    
    // Updates visuals.
    public void updateVisuals() {
        DEFAULT = BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Visuals.C_BORDER_OUTER, 2),
                        BorderFactory.createLineBorder(Visuals.C_COMPONENT_BORDER, 2)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        HOVERED = BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Visuals.C_BORDER_HOVERED, 2),
                        BorderFactory.createLineBorder(Visuals.C_COMPONENT_BORDER, 2)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        CLICKED = BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Visuals.C_BORDER_HOVERED, 2),
                        BorderFactory.createLineBorder(Visuals.C_BORDER_CLICKED, 2)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setBackground(Visuals.C_COMPONENT_BACKGROUND);
        setForeground(Visuals.C_FONTCOLOR1);
        setBorder(DEFAULT);
        setFont(Visuals.F_BUTTON);
    }
    
    // Mouse events
    // Changes the look of the button
    public void mouseClicked(MouseEvent e) {} // Does nothing
    public void mouseEntered(MouseEvent e) {
        if (isEnabled())
            setBorder(HOVERED);
    }
    public void mouseExited(MouseEvent e) {
        setBorder(DEFAULT);
    }
    public void mousePressed(MouseEvent e) {
        if (isEnabled())
            setBorder(CLICKED);
    }
    public void mouseReleased(MouseEvent e) {
        setBorder(DEFAULT);
    }
}
