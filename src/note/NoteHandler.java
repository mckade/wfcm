/**
 * @filename NoteFinder.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Helps find the correct note from a list of notes.
 * After given a list of notes to look through it will
 * - Organize the notes by rows.
 * - Find the largest value of x for each particular row.
 * - Finds the smallest value of x for each particular row.
 */

package note;

import java.util.HashMap;

public class NoteHandler {
    
    private HashMap<Integer, NoteList> notes;
    
    public NoteHandler() {
        notes = new HashMap<Integer, NoteList>();
    }
    
    // Receives a RNote array and organizes the notes by row
    public void setNotes(RNote[] rnotes) {
        notes.clear();
        if (rnotes == null) return;
        NoteList rowNotes = null;
        
        // Going through each note
        for (RNote note: rnotes) {
            if(notes.containsKey(note.y)) {
                // Row already known
                rowNotes = notes.get(note.y);
                rowNotes.add(note); // Adding note to array
                
                // Checking min x
                if (note.x < rowNotes.getMin())
                    rowNotes.setMin(note.x);
                // Checking max x
                if (note.x + note.width > rowNotes.getMax())
                    rowNotes.setMax(note.x + note.width);
            }
            else {
                // Row not known
                rowNotes = new NoteList();              // Creating Array
                rowNotes.add(note);                     // Adding note
                rowNotes.setMin(note.x);                // Setting min
                rowNotes.setMax(note.x + note.width);   // Setting max
                notes.put(note.y, rowNotes);            // Adding to hashmap
            }
        }
    }
    
    // Returns the note at the given spot.
    // Returns null if no note was found.
    public RNote getNote(int x, int y) {
        if (notes.isEmpty()) return null;           // Checking if notes exist
        if (!notes.containsKey(y)) return null;     // Checking if there are notes in the desired row
        NoteList rowNotes = notes.get(y);           // Getting note list for row
        if (rowNotes.getMin() > x) return null;     // Checking if x is greater then the max
        if (rowNotes.getMax() < x) return null;     // Checking if x is smaller then the min
        for (RNote note: rowNotes) {
            if (x >= note.x && x <= note.x + note.width)
                return note;
        }
        return null;
    }
}
