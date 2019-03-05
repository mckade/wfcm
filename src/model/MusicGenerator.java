package model;
/*
 * @filename MusicGenerator.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * Controller to use separate pieces to generate music.
 */

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.CPhrase;
import jm.music.data.Score;
import jm.util.Play;
import jm.util.Write;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicGenerator {

    private static AudioInputStream audioInputStream;

    private MarkovTable mTable;
    private WaveFCND wfc;
    private Score s;
    private String OUTPUT = "out.MID";

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
        Part p = new Part("Piano", 0, 0);
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
    }
    
    public void playSong()
    {
        audioFile(OUTPUT);
    }
    
    public void stopSong() {
        Play.stopAudio();
    }
    
    public boolean isPlaying() {
        return true;
    }
    
    public int getTempo() {
        return 100;
    }

    /*
    audioFile is used to playback MIDI files.
    NOTE: taken from the jMusic library (http://www.explodingart.com/jmusic)
    and extended to fit our needs
     */
    public static void audioFile(String var0) {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(var0));
            (new AudioFilePlayThread(audioInputStream)).start();
            System.out.println("Playing audio file " + var0);
        } catch (IOException var2) {
            System.err.println("Play audioFile error: in playAudioFile(): " + var2.getMessage());
        } catch (UnsupportedAudioFileException var3) {
            System.err.println("Unsupported Audio File error: in Play.audioFile():" + var3.getMessage());
        }
    }
}
