/**
 * @filename ButtonEvent.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * An event for when a button is clicked.
 * Buttons include JButtons and JMenuItems.
 * Holds the ID of the button clicked.
 */

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