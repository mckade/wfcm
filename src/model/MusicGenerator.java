package model;
/**
 * @filename MusicGenerator.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Controller to use separate pieces to generate music.
 */

import java.io.File;

import jm.audio.AOException;
import jm.audio.Instrument;
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
    
    // Saves the current generation table
    public void saveGenerationTable(File file) {
        // Does nothing atm
    }
    
    // Opens an existing generation table
    public boolean openGenerationTable(File file) {
        return false;
        // Does nothing atm
    }
    
    // Imports a MIDI sample to use in music generation
    public boolean importSample(File file) {
        boolean test = mTable.loadMidiFile(file.getAbsolutePath());
        PTable[] pt = new PTable[2];
        pt[0] = new PTable(mTable.getPitchTable());
        pt[1] = new PTable(mTable.getLengthTable());
        wfc = new WaveFCND(pt);

        return test;
    }
    
    // Exports a music generation table as a new MIDI file
    public void exportMIDI(File file) {
        Write.midi(s, file.getAbsolutePath());
    }
    
    // Generates music given a note length
    public void generateMusic(int length)
    {
        // use wfc and mTable
        s = new Score("Procedural", 105);
        Part p = new Part("Piano", ProgramChanges.PIANO, 0);
        Note[] notes = wfc.getNotes(length);

        double startTime = 0.0;
        Phrase phr = new Phrase(startTime);
        for(Note note : notes)
        {
            phr.addNote(note.getPitch(), note.getDuration());
        }
        //phr.addNoteList(notes, false);
        p.addPhrase(phr);
        s.addPart(p);
    }
    
    public void playSong()
    {
        Play.midi(s);
    }
    
    public void stopSong() {
        Play.stopMidi();
    }
    
    public boolean isPlaying() {
        return true;
    }
}
