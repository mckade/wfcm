package note;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class NoteList extends ArrayList<RNote> {

    private int max;
    private int min;
    
    public NoteList() {
        super();
    }
    
    public int getMax() {
        return max;
    }
    
    public int getMin() {
        return min;
    }
    
    public void setMax(int x) {
        max = x;
    }
    
    public void setMin(int x) {
        min = x;
    }
}
