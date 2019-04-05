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
    // chord holds a static reference to the unique chord durations in the sample
    // mapped to an integer (the index used in probability arrays)
    static HashMap<Double, Integer> chordLength;

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
        int chordKey = 0;
        int chordLengthKey = 0;
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
                //System.out.println(chrd[i]);
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

        // Modify chordPitches to include 'mod' number of extra pitches in the appropriate key signature
        // 'mod' will be user defined eventually
        ModKeySig(midiReader.EstimateKeySignature(), 1, chordKey);

        chordTable = new double[chord.size()][chord.size()];
        chordLengthTable = new double[chordLength.size()][chordLength.size()];
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

        // TODO link the modifier lists to UI so users can select
        // different generation parameters. For now, just using
        // dist-1 NoteTransition (see NoteTransition.java for details)
        // and dist-1 NoteDuration (see NoteDuration.java for details)
        pitchMods.add(new ChordTransition(1, chordsWithoutDuration, /*chord.size()*/chordsWithoutDuration.size()));
        lengthMods.add(new ChordDuration(1, chords, chordLength.size()));

        // TODO add modifier weighting, i.e, make some modifiers more
        // important than others with regard to final probability table
        // (Do we want this user controlled with presets?)
        System.out.println("Aggregating modifier probabilities");
        for(int i = 0; i < pitchMods.size(); i++)
        {
            double[][] modProbabilities = pitchMods.get(i).getProbabilities();
            for(int x = 0; x < chordPitches.size(); x++)
            {
                for(int y = 0; y < chordPitches.size(); y++)
                {
                    chordTable[x][y] = modProbabilities[x][y] / pitchMods.size();
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

    /**
     *
     * Increases the probability of notes in the piece's key
     * signature being included in the output by adding extra
     * pitch arrays of the key signature's notes.
     *
     * @param key - int 1 - 12 specifying the key signature to weight
     *            the pitch table for.
     * @param mod - a user specified modifier telling the algorithm
     *            how many additional key signature pitches should
     *            be added.
     * @param chordKey - chordKey for the HashMap of chords
     */
    // TODO: Modify so the pitch octave is random
    private void ModKeySig(int key, int mod, int chordKey) {

        Random rand = new Random();
        rand.setSeed(rand.nextLong());

        int[] A = new int[] {JMC.A4};
        int[] AS = new int[] {JMC.AS4};
        int[] B = new int[] {JMC.B4};
        int[] C = new int[] {JMC.C4};
        int[] CS = new int[] {JMC.CS4};
        int[] D = new int[] {JMC.D4};
        int[] DS = new int[] {JMC.DS4};
        int[] E = new int[] {JMC.E4};
        int[] F = new int[] {JMC.F4};
        int[] FS = new int[] {JMC.FS4};
        int[] G = new int[] {JMC.G4};
        int[] GS = new int[] {JMC.GS4};

        switch(key) {
            case 1: // C / Am
                for (int i = 0; i < mod; ++i) {
                    chordPitches.add(A);
                    if (chord.putIfAbsent(A, chordKey) == null)
                        chordKey++;
                    chordPitches.add(B);
                    if (chord.putIfAbsent(B, chordKey) == null)
                        chordKey++;
                    chordPitches.add(C);
                    if (chord.putIfAbsent(C, chordKey) == null)
                        chordKey++;
                    chordPitches.add(D);
                    if (chord.putIfAbsent(D, chordKey) == null)
                        chordKey++;
                    chordPitches.add(E);
                    if (chord.putIfAbsent(E, chordKey) == null)
                        chordKey++;
                    chordPitches.add(F);
                    if (chord.putIfAbsent(F, chordKey) == null)
                        chordKey++;
                    chordPitches.add(G);
                    if (chord.putIfAbsent(G, chordKey) == null)
                        chordKey++;
                }
                break;
            case 2: // G / Emin
                 for (int i = 0; i < mod; ++i) {
                    chordPitches.add(A);
                     if (chord.putIfAbsent(A, chordKey) == null)
                         chordKey++;
                    chordPitches.add(B);
                     if (chord.putIfAbsent(B, chordKey) == null)
                         chordKey++;
                    chordPitches.add(C);
                     if (chord.putIfAbsent(C, chordKey) == null)
                         chordKey++;
                    chordPitches.add(D);
                     if (chord.putIfAbsent(D, chordKey) == null)
                         chordKey++;
                    chordPitches.add(E);
                     if (chord.putIfAbsent(E, chordKey) == null)
                         chordKey++;
                    chordPitches.add(FS);
                     if (chord.putIfAbsent(FS, chordKey) == null)
                         chordKey++;
                    chordPitches.add(G);
                     if (chord.putIfAbsent(G, chordKey) == null)
                         chordKey++;
                }
                break;
            case 3: // D / Bm
                for (int i = 0; i < mod; ++i) {
                    chordPitches.add(A);
                    if (chord.putIfAbsent(A, chordKey) == null)
                        chordKey++;
                    chordPitches.add(B);
                    if (chord.putIfAbsent(B, chordKey) == null)
                        chordKey++;
                    chordPitches.add(CS);
                    if (chord.putIfAbsent(CS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(D);
                    if (chord.putIfAbsent(D, chordKey) == null)
                        chordKey++;
                    chordPitches.add(E);
                    if (chord.putIfAbsent(E, chordKey) == null)
                        chordKey++;
                    chordPitches.add(FS);
                    if (chord.putIfAbsent(FS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(G);
                    if (chord.putIfAbsent(G, chordKey) == null)
                        chordKey++;
                }
                break;
            case 4: // A / F#m
                for (int i = 0; i < mod; ++i) {
                    chordPitches.add(A);
                    if (chord.putIfAbsent(A, chordKey) == null)
                        chordKey++;
                    chordPitches.add(B);
                    if (chord.putIfAbsent(B, chordKey) == null)
                        chordKey++;
                    chordPitches.add(CS);
                    if (chord.putIfAbsent(CS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(D);
                    if (chord.putIfAbsent(D, chordKey) == null)
                        chordKey++;
                    chordPitches.add(E);
                    if (chord.putIfAbsent(E, chordKey) == null)
                        chordKey++;
                    chordPitches.add(FS);
                    if (chord.putIfAbsent(FS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(GS);
                    if (chord.putIfAbsent(GS, chordKey) == null)
                        chordKey++;
                }
                break;
            case 5: // E / C#m
                for (int i = 0; i < mod; ++i) {
                    chordPitches.add(A);
                    if (chord.putIfAbsent(A, chordKey) == null)
                        chordKey++;
                    chordPitches.add(B);
                    if (chord.putIfAbsent(B, chordKey) == null)
                        chordKey++;
                    chordPitches.add(CS);
                    if (chord.putIfAbsent(CS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(DS);
                    if (chord.putIfAbsent(DS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(E);
                    if (chord.putIfAbsent(E, chordKey) == null)
                        chordKey++;
                    chordPitches.add(FS);
                    if (chord.putIfAbsent(FS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(GS);
                    if (chord.putIfAbsent(GS, chordKey) == null)
                        chordKey++;
                }
                break;
            case 6: // B / G#m
                for (int i = 0; i < mod; ++i) {
                    chordPitches.add(AS);
                    if (chord.putIfAbsent(AS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(B);
                    if (chord.putIfAbsent(B, chordKey) == null)
                        chordKey++;
                    chordPitches.add(CS);
                    if (chord.putIfAbsent(CS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(DS);
                    if (chord.putIfAbsent(DS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(E);
                    if (chord.putIfAbsent(E, chordKey) == null)
                        chordKey++;
                    chordPitches.add(FS);
                    if (chord.putIfAbsent(FS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(GS);
                    if (chord.putIfAbsent(GS, chordKey) == null)
                        chordKey++;
                }
                break;
            case 7: // F# / Ebm
                for (int i = 0; i < mod; ++i) {
                    chordPitches.add(AS);
                    if (chord.putIfAbsent(AS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(B);
                    if (chord.putIfAbsent(B, chordKey) == null)
                        chordKey++;
                    chordPitches.add(CS);
                    if (chord.putIfAbsent(CS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(DS);
                    if (chord.putIfAbsent(DS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(F);
                    if (chord.putIfAbsent(F, chordKey) == null)
                        chordKey++;
                    chordPitches.add(FS);
                    if (chord.putIfAbsent(FS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(GS);
                    if (chord.putIfAbsent(GS, chordKey) == null)
                        chordKey++;
                }
                break;
            case 8: // C# / Bbm
                for (int i = 0; i < mod; ++i) {
                    chordPitches.add(AS);
                    if (chord.putIfAbsent(AS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(C);
                    if (chord.putIfAbsent(C, chordKey) == null)
                        chordKey++;
                    chordPitches.add(CS);
                    if (chord.putIfAbsent(CS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(DS);
                    if (chord.putIfAbsent(DS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(F);
                    if (chord.putIfAbsent(F, chordKey) == null)
                        chordKey++;
                    chordPitches.add(FS);
                    if (chord.putIfAbsent(FS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(GS);
                    if (chord.putIfAbsent(GS, chordKey) == null)
                        chordKey++;
                }
                break;
            case 9: // Ab / Fm
                for (int i = 0; i < mod; ++i) {
                    chordPitches.add(GS);
                    if (chord.putIfAbsent(GS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(AS);
                    if (chord.putIfAbsent(AS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(C);
                    if (chord.putIfAbsent(C, chordKey) == null)
                        chordKey++;
                    chordPitches.add(CS);
                    if (chord.putIfAbsent(CS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(DS);
                    if (chord.putIfAbsent(DS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(F);
                    if (chord.putIfAbsent(F, chordKey) == null)
                        chordKey++;
                    chordPitches.add(G);
                    if (chord.putIfAbsent(G, chordKey) == null)
                        chordKey++;
                }
                break;
            case 10: // Eb / Cm
                for (int i = 0; i < mod; ++i) {
                    chordPitches.add(GS);
                    if (chord.putIfAbsent(GS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(AS);
                    if (chord.putIfAbsent(AS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(C);
                    if (chord.putIfAbsent(C, chordKey) == null)
                        chordKey++;
                    chordPitches.add(D);
                    if (chord.putIfAbsent(D, chordKey) == null)
                        chordKey++;
                    chordPitches.add(DS);
                    if (chord.putIfAbsent(DS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(F);
                    if (chord.putIfAbsent(F, chordKey) == null)
                        chordKey++;
                    chordPitches.add(G);
                    if (chord.putIfAbsent(G, chordKey) == null)
                        chordKey++;
                }
                break;
            case 11: // Bb / Gm
                for (int i = 0; i < mod; ++i) {
                    chordPitches.add(A);
                    if (chord.putIfAbsent(A, chordKey) == null)
                        chordKey++;
                    chordPitches.add(AS);
                    if (chord.putIfAbsent(AS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(C);
                    if (chord.putIfAbsent(C, chordKey) == null)
                        chordKey++;
                    chordPitches.add(D);
                    if (chord.putIfAbsent(D, chordKey) == null)
                        chordKey++;
                    chordPitches.add(DS);
                    if (chord.putIfAbsent(DS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(F);
                    if (chord.putIfAbsent(F, chordKey) == null)
                        chordKey++;
                    chordPitches.add(G);
                    if (chord.putIfAbsent(G, chordKey) == null)
                        chordKey++;
                }
                break;
            case 12: // F / Dm
                for (int i = 0; i < mod; ++i) {
                    chordPitches.add(A);
                    if (chord.putIfAbsent(A, chordKey) == null)
                        chordKey++;
                    chordPitches.add(AS);
                    if (chord.putIfAbsent(AS, chordKey) == null)
                        chordKey++;
                    chordPitches.add(C);
                    if (chord.putIfAbsent(C, chordKey) == null)
                        chordKey++;
                    chordPitches.add(D);
                    if (chord.putIfAbsent(D, chordKey) == null)
                        chordKey++;
                    chordPitches.add(E);
                    if (chord.putIfAbsent(E, chordKey) == null)
                        chordKey++;
                    chordPitches.add(F);
                    if (chord.putIfAbsent(F, chordKey) == null)
                        chordKey++;
                    chordPitches.add(G);
                    if (chord.putIfAbsent(G, chordKey) == null)
                        chordKey++;
                }
                break;
            default:
                System.out.println("Markov table: Invalid input in ModKeySig method call.");
                break;
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
