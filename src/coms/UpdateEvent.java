/**
 * @filename UpdateEvent.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * An update event holding the update type.
 * Depending on the update type different actions will take place.
 */

package coms;

import java.util.EventObject;

@SuppressWarnings("serial")
public class UpdateEvent extends EventObject {
    
    private UpdateType updateType;
    private double playTime;

    public UpdateEvent(Object source, UpdateType updateType) {
        super(source);
        this.updateType = updateType;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }
    
    public void setPlayTime(double playTime) {
        this.playTime = playTime;
    }

    public double getPlayTime() {
        return playTime;
    }


}
