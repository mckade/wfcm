package gui;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class _JSpinner extends JSpinner {

    public _JSpinner(int value, int min, int max, int stepSize) {
        super(new SpinnerNumberModel(value, min, max, stepSize));
        setUp();
    }
    
    public _JSpinner(double value, double min, double max, double stepSize) {
        super(new SpinnerNumberModel(value, min, max, stepSize));
        setUp();
    }
    
    private void setUp() {
        getEditor().getComponent(0).setBackground(MainWindow.C_COMPONENT_BACKGROUND);
        getEditor().getComponent(0).setForeground(MainWindow.C_FONTCOLOR1);
        setBorder(MainWindow.B_COMPONENT_BORDER);
    }
}
