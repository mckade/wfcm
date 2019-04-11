/**
 * @filename NoteFinder.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Helps find the correct note from a list of notes.
 * After given a list of notes to look through it will
 * - Organize the notes by rows.
 */

package gui;

import java.util.ArrayList;
import java.util.HashMap;

import model.RNote;

public class NoteFinder {
    
    private RNote[] notes;
    private HashMap<Integer, ArrayList<RNote>> rows;
    
    public NoteFinder() {}  // Does nothing
    
    // Receives a RNote array and organizes the notes by row
    public void setNotes(RNote[] notes) {
        this.notes = notes;
        rows = new HashMap<Integer, ArrayList<RNote>>();
        if (this.notes == null) return;
        
        for (RNote note: notes) {
            if(rows.containsKey(note.y)) {
                rows.get(note.y).add(note);
            }
            else {
                ArrayList<RNote> rowNotes = new ArrayList<RNote>();
                rowNotes.add(note);
                rows.put(note.y, rowNotes);
            }
        }
    }
    
    // Returns the note at the given spot.
    // Returns null if no note was found.
    public RNote getNote(int x, int y) {
        if (notes == null) return null;
        if (!rows.containsKey(y)) return null;
        ArrayList<RNote> rowNotes = rows.get(y);
        for (RNote note: rowNotes) {
            if (x >= note.x && x <= note.x + note.width)
                return note;
        }
        return null;
    }
}
