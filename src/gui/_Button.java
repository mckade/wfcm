/**
 * @filename _Button.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Extends the basic JButton to add a little more functionality,
 * for it's looks for when it's interacting with the user.
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
    
    // Border looks
    public static final Border DEFAULT = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 92, 75), 2),
                    BorderFactory.createLineBorder(new Color(14, 184, 152), 2)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5));
    public static final Border HOVERED = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(80, 55, 110), 2),
                    BorderFactory.createLineBorder(new Color(14, 184, 152), 2)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5));
    public static final Border CLICKED = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(80, 55, 110), 2),
                    BorderFactory.createLineBorder(new Color(180, 76, 180), 2)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
    // Constructor
    public _Button(String text) {
        // Setup
        super(text);
        setBackground(MainWindow.COMPONENT_BACKGROUND);
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
        if (isEnabled()) {
            setBorder(HOVERED);
        }
    }
    public void mouseExited(MouseEvent e) {
        setBorder(DEFAULT);
    }
    public void mousePressed(MouseEvent e) {
        if (isEnabled()) {
            setBorder(CLICKED);
        }
    }
    public void mouseReleased(MouseEvent e) {
        setBorder(DEFAULT);
    }
}
