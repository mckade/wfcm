/**
 * @filename _Button.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Extends the basic JButton to add a little more functionality.
 * Changes how the button looks when interacted with by the user.
 */

package gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class _Button extends JButton
implements MouseListener {
    
    // Border visuals
    public static final Border DEFAULT = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MainWindow.C_BORDER_OUTER, 2),
                    BorderFactory.createLineBorder(MainWindow.C_COMPONENT_BORDER, 2)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5));
    public static final Border HOVERED = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MainWindow.C_BORDER_HOVERED, 2),
                    BorderFactory.createLineBorder(MainWindow.C_COMPONENT_BORDER, 2)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5));
    public static final Border CLICKED = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MainWindow.C_BORDER_HOVERED, 2),
                    BorderFactory.createLineBorder(MainWindow.C_BORDER_CLICKED, 2)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
    public _Button(String text) {
        // Setup
        super(text);
        setBackground(MainWindow.C_COMPONENT_BACKGROUND);
        setForeground(Color.WHITE);
        setBorder(DEFAULT);
        setModel(new DefaultButtonModel() {
            public boolean isPressed() {
                return false;
            }
        });
        addMouseListener(this);
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
