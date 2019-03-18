package model;
/*
 * @filename MusicGenerator.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * Controller to use separate pieces to generate music.
 */

import java.io.File;
import java.util.Vector;

import jm.audio.Instrument;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.CPhrase;
import jm.music.data.Score;
import jm.util.Write;

public class MusicGenerator {

    private MarkovTable mTable;
    private WaveFCND wfc;
    private Score s;
    
    // Temp variable until proper
    private boolean playing = false;

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
    public void generateMusic(int length, int tempo)
    {
        // use wfc and mTable
        s = new Score("Procedural", tempo);
        Part p = new Part("Piano", Instrument.BAG_PIPES, 0);
        Vector<Note[]> notes = wfc.getNotes(length);

        CPhrase phr = new CPhrase();
        for(Note[] chord : notes)
        {
            System.out.println(chord);
            int[] pitches = new int[chord.length];
            for(int i = 0; i < chord.length; i++)
            {
                pitches[i] = chord[i].getPitch();
            }
            phr.addChord(pitches, chord[0].getDuration());
        }
        //phr.addNoteList(notes, false);
        p.addCPhrase(phr);
        s.addPart(p);
        Write.midi(s, MusicState.OUTPUT);
    }
    
    public void playSong()
    {
        playing = true;
        MusicState.audioFile(MusicState.OUTPUT);
    }
    
    public boolean stopSong()
    {
        boolean tmp = playing;
        playing = false;
        MusicState.stop();
        return tmp;
    }
    
    public void pauseSong() {
        MusicState.pause();
    }
    
    public void unpauseSong() {
        MusicState.unpause();
    }
    
    public boolean isPlaying() {
        return playing;
    }
    
    public int getTempo() {
        return 100;
    }
    
    // Returns the notes of the generation.
    public void getNotes() {
        // Stub
    }
}
