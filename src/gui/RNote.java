package gui;

import java.awt.Rectangle;

@SuppressWarnings("serial")
public class RNote extends Rectangle {
    
    boolean locked = false;

    public RNote(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
}
