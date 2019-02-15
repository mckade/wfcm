/**
 * @filename UpdateEvent.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * An update event holding various info on want needs updated.
 * - Left/Right pane divider needs reset
 */

package coms;

import java.util.EventObject;

@SuppressWarnings("serial")
public class UpdateEvent extends EventObject {
    
    private UpdateType updateType;

    public UpdateEvent(Object source, UpdateType updateType) {
        super(source);
        this.updateType = updateType;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }
}
