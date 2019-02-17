package coms;

import java.util.EventObject;

@SuppressWarnings("serial")
public class ButtonEvent extends EventObject {
    
    private int length;

    public ButtonEvent(Object source, int length ) {
        super(source);
        this.length = length;
    }
    
    public int getLength() {
        return length;
    }
}
