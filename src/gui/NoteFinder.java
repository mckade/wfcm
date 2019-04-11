/**
 * @filename NoteFinder.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Helps find the correct note from a list of notes.
 * First it will take in an array of RNotes.
 * Next it will split up the notes into their respective rowsjava
 */

package gui;

import model.RNote;

public class NoteFinder {
    
    RNote[] notes;
    
    public NoteFinder() {
        
    }
    
    // Receives a RNote array and organizes the notes
    public void setNotes(RNote[] notes) {
        this.notes = notes;
    }
    
    // Returns the note at the given spot.
    // Returns null if no note was found.
    public RNote getNote(int x, int y) {
        if (notes == null) return null;
        for (RNote note: notes) {
            if (note.y != y) continue;
            if (x >= note.x && x <= note.width)
                return note;
        }
        return null;
    }
}
