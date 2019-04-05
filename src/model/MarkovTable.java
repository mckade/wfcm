package model;
/*
  @filename MarkovTable
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Manages probability modifiers and combines them into a
 * single probability table per feature (e.g., pitch & duration)
 *
 * Used by WaveFCND to generate music
 */

import jm.JMC;
import jm.music.data.Score;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

class MarkovTable {

    // Fields
    private MidiReader midiReader;
    private Vector<double[]> chords;
    private Vector<int[]> chordPitches;
    private double[][] chordTable; // chord transition probabilities
    private double[][] chordLengthTable; // chord duration transition probabilities
    private Rectangle[] sample;
    private double timeScale = 100.0;

    // chord holds a static reference to the unique pitch arrays in the sample
    // mapped to an integer (the index used in probability arrays)
    static HashMap<int[], Integer> chord;
    static int chordKey;
    // chord holds a static reference to the unique chord durations in the sample
    // mapped to an integer (the index used in probability arrays)
    static HashMap<Double, Integer> chordLength;
    int chordLengthKey;

    // Constructor
    MarkovTable()
    {
        midiReader = new MidiReader();
    }

    double getTimeScale() {
        return timeScale;
    }

    double getTempo()
    {
        return midiReader.getTempo();
    }

    Rectangle[] getSample()
    {
        return sample;
    }

    Score getScore() {return midiReader.getMidiScore();}

    // Loads midi file
    boolean loadMidiFile(String filename)
    {
        // Get midi data for processing.
        midiReader.readMidi(filename);
        chords = midiReader.getChords();
        // Checking if file was successfully read.
        if (chords == null) return false;

        // initialize keys to 0
        chordKey = 0;
        chordLengthKey = 0;
        chord = new HashMap<>();
        chordLength = new HashMap<>();
        Vector<Rectangle> rects = new Vector<>();

        // scan the sample and update the pitch and length maps
        System.out.println("Creating chord and chord length maps");
        chordPitches = new Vector<>();
        double time = 0.0;
        for (double[] chrd : chords) {
            // Make a copy of chord pitch array without the duration at the end
            int[] newChrd = new int[chrd.length - 1];
            for (int i = 0; i < chrd.length - 1; ++i) {
                System.out.println(chrd[i]);
                newChrd[i] = (int)chrd[i];
                rects.add(new Rectangle(
                        (int)(time*timeScale), // x = noteStartTime * scale
                        (int)chrd[i], // y = pitch
                        (int)(chrd[chrd.length - 1]*timeScale), // width = duration * scale
                        1)); //height -- updated in VisualizerGraphics, so this value doesn't matter
            }

            time += chrd[chrd.length - 1];

            // Add new chord to chordPitches as int[]
            chordPitches.add(newChrd);

            if (chord.putIfAbsent(newChrd, chordKey) == null)
                chordKey++;

            if (chordLength.putIfAbsent(chrd[chrd.length - 1], chordLengthKey) == null)
                chordLengthKey++;
        }


        sample = rects.toArray(new Rectangle[0]);

        generateTable(chordPitches);

        return true;
    }
    
    // Generate a music probability table given midi data
    private void generateTable(Vector<int[]> chordsWithoutDuration)
    {
        System.out.println("Starting table generation");
        List<Modifier> pitchMods = new ArrayList<>();
        List<Modifier> lengthMods = new ArrayList<>();
        List<Modifier> keySigMods = new ArrayList<>();

        // TODO link the modifier lists to UI so users can select
        // different generation parameters. For now, just using
        // dist-1 NoteTransition (see NoteTransition.java for details)
        // and dist-1 NoteDuration (see NoteDuration.java for details)
        pitchMods.add(new ChordTransition(1, chordsWithoutDuration, chordsWithoutDuration.size()));
        lengthMods.add(new ChordDuration(1, chords, chordLength.size()));
        int signature = midiReader.estimateKeySignature();
        for (Modifier ct : pitchMods) {
            keySigMods.add(new KeySignatureMod(signature, ct.getProbabilities(), 0.5));
        }

        double[][] keySigModProbabilities = keySigMods.get(0).getProbabilities();

        chordTable = new double[keySigModProbabilities.length][keySigModProbabilities.length];
        chordLengthTable = new double[chordLength.size()][chordLength.size()];


        // TODO add modifier weighting, i.e, make some modifiers more
        // important than others with regard to final probability table
        // (Do we want this user controlled with presets?)
        System.out.println("Aggregating modifier probabilities");
        for(int i = 0; i < pitchMods.size(); i++)
        {
            double[][] modProbabilities = pitchMods.get(i).getProbabilities();
            for(int x = 0; x < keySigModProbabilities.length; x++)
            {
                for(int y = 0; y < keySigModProbabilities.length; y++)
                {
                    if (x < chordPitches.size() && y < chordPitches.size())
                        chordTable[x][y] = modProbabilities[x][y] / pitchMods.size();
                    else
                        chordTable[x][y] = keySigModProbabilities[x][y] / pitchMods.size();
                }
            }
        }

        for(int i = 0; i < lengthMods.size(); i++)
        {
            double[][] modProbabilities = lengthMods.get(i).getProbabilities();
            for(int x = 0; x < chordLength.size(); x++)
            {
                for(int y = 0; y < chordLength.size(); y++)
                {
                    chordLengthTable[x][y] = modProbabilities[x][y] / lengthMods.size();
                }
            }
        }
    }

    double[][] getPitchTable()
    {
        return chordTable;
    }

    double[][] getLengthTable()
    {
        return chordLengthTable;
    }

    static int[] getPitch(int index) {
        for (Map.Entry<int[], Integer> e: chord.entrySet()) {
            if (index == e.getValue()){
                return e.getKey();
            }
        }

        return null;
    }

    static double getDuration(int index) {
        for (Map.Entry<Double, Integer> e: chordLength.entrySet()) {
            if (index == e.getValue()) {
                return e.getKey();
            }
        }

        return 0.0;
    }
}
