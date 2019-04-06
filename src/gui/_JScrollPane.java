package gui;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class _JScrollPane extends JScrollPane {

    public _JScrollPane(JComponent view) {
        super(view);
        setBackground(Visuals.C_COMPONENT_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JScrollBar vb = getVerticalScrollBar();
        JScrollBar hb = getHorizontalScrollBar();
        vb.setUnitIncrement(8);
        hb.setUnitIncrement(8);
        vb.setUI(Visuals.createScrollBarUI());
        hb.setUI(Visuals.createScrollBarUI());
    }
}
