package coms;

import java.util.EventObject;

@SuppressWarnings("serial")
public class ButtonEvent extends EventObject {
    
    String id;
    
    public ButtonEvent(Object source, String id ) {
        super(source);
        this.id = id;
    }
    
    public String getID() {
        return id;
    }
}