package gui;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class SpinSlider extends JComponent
implements ChangeListener {
    
    // Listener to send evens to
    ChangeListener listener;
    
    // Components
    _JSlider slider;
    _JSpinner spinner;
    
    public SpinSlider(int min, int max, int value, int stepSize) {
        // Setup
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        // Creating components
        slider = new _JSlider(min, max, value);
        slider.addChangeListener(this);
        spinner = new _JSpinner(value, min, max, stepSize);
        spinner.addChangeListener(this);
        
        // Adding components
        add(slider);
        add(spinner);
    }
    
    // Updates visuals.
    public void updateVisuals() {
        slider.updateVisuals();
        spinner.updateVisuals();
    }
    
    // Gets the value of the spinner/slider
    public int getValue() {
        return slider.getValue();
    }
    
    // Sets the value of the spinner/slider
    public void setValue(int value) {
        slider.setValue(value);
    }
    
    // Sets the listener.
    public void setChangeListener(ChangeListener listener) {
        this.listener = listener;
    }
    
    // Fires off that the state has changed.
    private void fireStateChanged(ChangeEvent e) {
        if (listener != null)
            listener.stateChanged(e);
    }

    // If the value of the slider or spinner changes,
    // Updates the appropriate component.
    public void stateChanged(ChangeEvent e) {
        if (slider.getValue() == (int)spinner.getValue()) return;
        if (e.getSource() == slider)
            spinner.setValue(slider.getValue());
        else
            slider.setValue((int)spinner.getValue());
        fireStateChanged(new ChangeEvent(this));
    }
}
