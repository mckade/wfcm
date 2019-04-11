package note;

import java.awt.Rectangle;

@SuppressWarnings("serial")
public class RNote extends Rectangle {
    
    private boolean locked = false;

    public RNote(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public void toggleLock() {
        locked = !locked;
    }
}
