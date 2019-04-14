package gui;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

@SuppressWarnings("serial")
public class _JComboBox<T> extends JComboBox<T> {
    
    public _JComboBox(T[] list) {
        super(list);
    }
    
    // Updates visuals.
    public void updateVisuals() {
        setBackground(Visuals.C_COMPONENT_BACKGROUND);
        setForeground(Visuals.C_FONTCOLOR1);
        setBorder(Visuals.B_COMPONENT_BORDER);
        setUI(new BasicComboBoxUI() {
            public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
                super.paintCurrentValue(g, bounds, false);
            }
            protected JButton createArrowButton() {
                return Visuals.createBasicArrowButton(BasicArrowButton.SOUTH);
            }
        });
    }
}
