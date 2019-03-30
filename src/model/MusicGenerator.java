package model;
/*
 * @filename MusicGenerator.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * Controller to use separate pieces to generate music.
 */

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Vector;

import jm.audio.Instrument;
import jm.music.data.CPhrase;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Score;
import jm.util.Write;

public class MusicGenerator {

    private MarkovTable mTable;
    private WaveFCND wfc;
    private Score s;
    private Rectangle[] noteData;

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

        // copy the sample file to out.MID so it can be played
        try {
            Files.copy(file.toPath(), new File(MusicState.OUTPUT).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        Vector<Rectangle> nd = new Vector<>();

        CPhrase phr = new CPhrase();
        double time = 0.0;
        for(Note[] chord : notes)
        {
            // save note info in noteTable
            for(Note n : chord)
            {
                nd.add(new Rectangle((int)(time*mTable.getTimeScale()), n.getPitch(),
                        (int)(n.getDuration()*mTable.getTimeScale()), 1));
            }

            time += chord[0].getDuration();

            // System.out.println(chord);
            int[] pitches = new int[chord.length];
            for(int i = 0; i < chord.length; i++)
            {
                pitches[i] = chord[i].getPitch();
            }
            phr.addChord(pitches, chord[0].getDuration());
        }

        noteData = nd.toArray(new Rectangle[nd.size()]);
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
    public Rectangle[] getNotes() {
        return noteData;
    }

    // Returns the notes of the generation.
    public Rectangle[] getSampleNotes() {
        return mTable.getSample();
    }
}
