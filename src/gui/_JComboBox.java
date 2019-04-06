package gui;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

@SuppressWarnings("serial")
public class _JComboBox extends JComboBox<Object> {
    
    public _JComboBox(String[] list) {
        super(list);
        setBackground(Visuals.C_COMPONENT_BACKGROUND);
        setForeground(Visuals.C_FONTCOLOR1);
        setBorder(Visuals.B_COMPONENT_BORDER);
        setUI(new BasicComboBoxUI() {
            protected JButton createArrowButton() {
                return Visuals.createBasicArrowButton(BasicArrowButton.SOUTH);
            }
        });
    }
}
