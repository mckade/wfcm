package gui;

import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicSpinnerUI;

@SuppressWarnings("serial")
public class _JSpinner extends JSpinner {

    public _JSpinner(int value, int min, int max, int stepSize) {
        super(new SpinnerNumberModel(value, min, max, stepSize));
    }
    
    public _JSpinner(double value, double min, double max, double stepSize) {
        super(new SpinnerNumberModel(value, min, max, stepSize));
    }
    
    // Updates visuals.
    public void updateVisuals() {
        getEditor().getComponent(0).setBackground(Visuals.C_COMPONENT_BACKGROUND);
        getEditor().getComponent(0).setForeground(Visuals.C_FONTCOLOR1);
        setBorder(Visuals.B_COMPONENT_BORDER);
        setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                BasicArrowButton a = Visuals.createBasicArrowButton(BasicArrowButton.NORTH);
                installNextButtonListeners(a);
                return a;
            }
            protected Component createPreviousButton() {
                BasicArrowButton a = Visuals.createBasicArrowButton(BasicArrowButton.SOUTH);
                installPreviousButtonListeners(a);
                return a;
            }
        });
    }
}
