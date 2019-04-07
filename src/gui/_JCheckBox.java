package gui;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class _JCheckBox extends JCheckBox {
    
    public _JCheckBox(String text) {
        super(text);
    }
    
    // Updates visuals.
    public void updateVisuals() {
        setBackground(Visuals.C_PANEL_BACKGROUND);
        setForeground(Visuals.C_FONTCOLOR1);
    }
}
