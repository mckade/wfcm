package gui;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class _JScrollPane extends JScrollPane {

    public _JScrollPane(JComponent view) {
        super(view);
        getVerticalScrollBar().setUnitIncrement(8);
        getHorizontalScrollBar().setUnitIncrement(8);
    }
    
    // Updates visuals.
    public void updateVisuals() {
        setBackground(Visuals.C_COMPONENT_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        getVerticalScrollBar().setUI(Visuals.createBasicScrollBarUI());
        getHorizontalScrollBar().setUI(Visuals.createBasicScrollBarUI());
    }
}
