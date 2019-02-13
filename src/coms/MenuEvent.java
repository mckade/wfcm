/**
 * @filename MenuEvent.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * A menu event holding the id of the button clicked.
 * Look at MenuBar for a list of events.
 */

package coms;

import java.util.EventObject;

@SuppressWarnings("serial")
public class MenuEvent extends EventObject {
    
    String id;

    public MenuEvent(Object source, String id) {
        super(source);
        this.id = id;
    }
    
    public String getID() {
        return id;
    }
}
