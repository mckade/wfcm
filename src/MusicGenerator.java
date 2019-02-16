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
import jm.util.Write;

public class MusicGenerator {

    MarkovTable mTable;
    WaveFCND wfc;
    Score s;

    public MusicGenerator()
    {
        mTable = new MarkovTable();
    }
    
    // Pushes filename to Markov table
    public boolean loadMidiFile(String filename)
    {
        return mTable.loadMidiFile(filename);
    }

    public void generateMusic(int length)
    {
        if(wfc == null)
        {
            PTable[] pt = new PTable[2];
            if(mTable.getPitchTable() == null || mTable.getLengthTable() == null)
                System.out.println("null table");
            pt[0] = new PTable(mTable.getPitchTable());
            pt[1] = new PTable(mTable.getLengthTable());
            wfc = new WaveFCND(pt);
        }
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
        Write.midi(s, "output.MID");
    }
}
