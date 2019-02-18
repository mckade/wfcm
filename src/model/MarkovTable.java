package model;
/**
 * @filename MarkovTable
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Manages probability modifiers and combines them into a
 * single probability table per feature (e.g., pitch & duration)
 *
 * Used by WaveFCND to generate music
 */

import java.io.IOException;
import java.util.*;

import jm.music.data.CPhrase;
import jm.music.data.Note;

public class MarkovTable {

    // Fields
    private MidiReader midiReader;
    private Note[] notes;
    private CPhrase chords;
    private double[][] pitchTable;  // pitch transition probabilities
    private double[][] lengthTable; // duration transition probabilities

    // pitch holds a static reference to the unique note pitches in the sample
    // mapped to an integer (the index used in probability arrays)
    public static HashMap<Integer, Integer> pitch;
    // length holds a static reference to the unique note durations in the sample
    // mapped to an integer (the index used in probability arrays)
    public static HashMap<Double, Integer> length;
    
    // Constructor
    public MarkovTable()
    {
        midiReader = new MidiReader();
    }
    
    // Loads midi file
    public boolean loadMidiFile(String filename)
    {
        // Get midi data for processing.
        try {
            midiReader.readMidi(filename);
            notes = midiReader.midiData.getNotes();
            chords = midiReader.midiData.getChords();
        } catch (IOException ex) {
            System.out.println("Could not read file: " + filename);
        }
        // Checking if file was successfully read.
        if (notes == null) return false;

        // initialize keys to 0
        int pitchKey = 0;
        int lengthKey = 0;
        pitch = new HashMap<>();
        length = new HashMap<>();

        // scan the sample and update the pitch and length maps
        System.out.println("Creating pitch and length maps");
        for (Note note : notes)
        {
            // putIfAbsent returns null if the key was not previously mapped
            // to a value. In this case, we just inserted a new value in the
            // hashmap and need to increment the key
            if (pitch.putIfAbsent(note.getPitch(), pitchKey) == null)
                pitchKey++;
            if (length.putIfAbsent(note.getDuration(), lengthKey) == null)
                lengthKey++;
        }

        pitchTable = new double[pitch.size()][pitch.size()];
        lengthTable = new double[length.size()][length.size()];

        generateTable(notes);

        return true;
    }
    
    // Generate a music probability table given midi data
    private void generateTable(Note[] notes)
    {
        System.out.println("Starting table generation");
        List<Modifier> pitchMods = new ArrayList<>();
        List<Modifier> lengthMods = new ArrayList<>();

        // TODO link the modifier lists to UI so users can select
        // different generation parameters. For now, just using
        // dist-1 and dist-4 NoteTransition (see NoteTransition.java for details)
        // and dist-1 NoteDuration (see NoteDuration.java for details)
        pitchMods.add(new NoteTransition(1, notes, pitch.size()));
        pitchMods.add(new NoteTransition(4, notes, pitch.size()));

        lengthMods.add(new NoteDuration(1, notes, length.size()));

        // TODO add modifier weighting, i.e, make some modifiers more
        // important than others with regard to final probability table
        // (Do we want this user controlled with presets?)
        System.out.println("Aggregating modifier probabilities");
        for(int i = 0; i < pitchMods.size(); i++)
        {
            double[][] modProbabilities = pitchMods.get(i).getProbabilities();
            for(int x = 0; x < pitch.size(); x++)
            {
                for(int y = 0; y < pitch.size(); y++)
                {
                    pitchTable[x][y] = modProbabilities[x][y] / pitchMods.size();
                }
            }
        }

        for(int i = 0; i < lengthMods.size(); i++)
        {
            double[][] modProbabilities = lengthMods.get(i).getProbabilities();
            for(int x = 0; x < length.size(); x++)
            {
                for(int y = 0; y < length.size(); y++)
                {
                    lengthTable[x][y] = modProbabilities[x][y] / lengthMods.size();
                }
            }
        }
    }

    public double[][] getPitchTable()
    {
        return pitchTable;
    }

    public double[][] getLengthTable()
    {
        return lengthTable;
    }

    public static int getPitch(int index)
    {
        for(Map.Entry<Integer, Integer> e : pitch.entrySet())
        {
            if(index == e.getValue())
            {
                return e.getKey();
            }
        }

        return 0;
    }

    public static double getDuration(int index)
    {
        for(Map.Entry<Double, Integer> e : length.entrySet())
        {
            if(index == e.getValue())
            {
                return e.getKey();
            }
        }

        return 0.0;
    }
}
