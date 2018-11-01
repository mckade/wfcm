/**
 * @filename MusicGenerator.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Controller to use separate pieces to generate music.
 */

import jm.constants.ProgramChanges;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;

public class MusicGenerator {

    MarkovTable mTable;
    WaveFC wfc;
    Score s;

    public MusicGenerator()
    {
        mTable = new MarkovTable();
        wfc = new WaveFC(mTable);
    }
    
    // Pushes filename to Markov table
    public boolean loadMidiFile(String filename)
    {
        return mTable.loadMidiFile(filename);
    }

    public void generateMusic(int length)
    {
        // use wfc and mTable
        s = new Score("Procedural");
        Part p = new Part("Piano", ProgramChanges.PIANO, 0);
        Note[] notes = wfc.getNotes(length);

        double startTime = 0.0;
        Phrase phr = new Phrase(startTime);
        for(Note note : notes)
        {
            phr.addNote(note);
        }
        p.addPhrase(phr);
        s.addPart(p);
    }

    public void playSong()
    {
        Play.midi(s);
    }

    public void saveSong()
    {

    }
}
