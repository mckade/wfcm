package model;

import jm.JMC;

import java.util.Random;
import java.util.Vector;

public class KeySignatureMod extends Modifier {

    private int[] C = new int[] {JMC.C4, JMC.D4, JMC.E4, JMC.F4, JMC.G4, JMC.A4, JMC.B4};
    private int[] G = new int[] {JMC.G4, JMC.A4, JMC.B4, JMC.C4, JMC.D4, JMC.E4, JMC.FS4};
    private int[] D = new int[] {JMC.D4, JMC.E4, JMC.FS4, JMC.G4, JMC.A4, JMC.B4, JMC.CS4};
    private int[] A = new int[] {JMC.A4, JMC.B4, JMC.CS4, JMC.D4, JMC.E4, JMC.FS4, JMC.GS4};
    private int[] E = new int[] {JMC.E4, JMC.FS4, JMC.GS4, JMC.A4, JMC.B4, JMC.CS4, JMC.DS4};
    private int[] B = new int[] {JMC.B4, JMC.CS4, JMC.DS4, JMC.E4, JMC.FS4, JMC.GS4, JMC.AS4};
    private int[] FS = new int[] {JMC.FS4, JMC.GS4, JMC.AS4, JMC.B4, JMC.CS4, JMC.DS4, JMC.F4};
    private int[] CS = new int[] {JMC.CS4, JMC.DS4, JMC.F4, JMC.FS4, JMC.GS4, JMC.AS4, JMC.C4};
    private int[] Af = new int[] {JMC.GS4, JMC.AS4, JMC.C4, JMC.CS4, JMC.DS4, JMC.F4, JMC.G4};
    private int[] Ef = new int[] {JMC.DS4, JMC.F4, JMC.G4, JMC.GS4, JMC.AS4, JMC.C4, JMC.D4};
    private int[] Bf = new int[] {JMC.AS4, JMC.C4, JMC.D4, JMC.DS4, JMC.F4, JMC.G4, JMC.A4};
    private int[] F = new int[] {JMC.F4, JMC.G4, JMC.A4, JMC.AS4, JMC.C4, JMC.D4, JMC.E4};

    private int sig;
    private double weight;
    private Vector<int[]> freshPitches;
    private int cardinality;
    private Vector<int[]> keys;

    public KeySignatureMod(int sig, double[][] pitchProbs, double weight) {

        // Set key signature to be weighted
        this.sig = sig;

        // Put keys in vector
        keys = new Vector<>();
        keys.add(C);
        keys.add(G);
        keys.add(D);
        keys.add(A);
        keys.add(E);
        keys.add(B);
        keys.add(FS);
        keys.add(CS);
        keys.add(Af);
        keys.add(Ef);
        keys.add(Bf);
        keys.add(F);


        // Set weight
        this.weight = weight;

        // Instantiate freshChords
        freshPitches = new Vector<>();

        // Update chord pitch map in MarkovTable
        // and add any fresh pitches to freshPitches
        UpdatePitchMap();

        // set cardinality to the new number of unique sample notes
        cardinality = pitchProbs.length + freshPitches.size();

        // Initiate probability table
        probabilities = new double[cardinality][cardinality];
        fillProbabilities(pitchProbs);

        calculateProbabilities();
    }

    public int bayesianKeyFinder(Vector<double[]> chords) {

        // Segment the input into segments of at most 4 notes/chords
        Vector<Vector<double[]>> segments = new Vector<>();
        Vector<double[]> segment = new Vector<>();
        for (int i = 0; i < chords.size(); ++i) {
            if (segment.size() < 12) {
                segment.add(chords.get(i));
                if (i == chords.size() - 1) {
                    segments.add(segment);
                    segment = new Vector<>();
                }
            } else {
                segments.add(segment);
                segment = new Vector<>();
            }
        }

        // Calculate the probability of a certain sequence of pitches (surface)
        // given a structure (key signature).
        // A set of key-profile values will be calculate for each key.
        // These values will be the % of segments in which the scale-degree occurs
        // specific to that key.
        Vector<Vector<Double>> keySigProfiles = new Vector<>();
        for (int[] key : keys) {
            Vector<Double> keyProfiles = new Vector<>();
            for (int pitch : key) {
                double occurs = 0.0;
                boolean found;
                for (Vector<double[]> seg : segments) {
                    found = false;
                    for (double[] segPitch : seg) {
                        if (segPitch[0] % 12 == (1.0 * pitch) % 12 && !found) {
                            found = true;
                            occurs++;
                        }
                    }
                }
                keyProfiles.add(occurs / segments.size());
            }
            keySigProfiles.add(keyProfiles);
        }

        // Now calculate the probability of each key signature
        // First segment has 1/24 probability. Proceeding segments have
        // 0.8 probability of staying in same key.
        // We will ignore key changes.
        double probability;
        double presentProd;
        double absentProd;
        Vector<Double> profiles;
        Vector<Double> probabilities = new Vector<>();
        for (int i = 0; i < keySigProfiles.size(); ++i) {
            probability = 1.0 / 24.0;
            profiles = keySigProfiles.get(i);
            for (int j = 0; j < segments.size(); ++j) {
                if (profiles.get(0) == 0.0) {
                    presentProd = Math.log(0.01);
                    absentProd = Math.log(0.99);
                }
                else if (profiles.get(0) == 1) {
                    presentProd = Math.log(0.99);
                    absentProd = Math.log(0.01);
                } else {
                    presentProd = Math.log(profiles.get(0));
                    absentProd = Math.log(1 - profiles.get(0));
                }
                for (int k = 1; k < profiles.size(); ++k) {
                    if (profiles.get(k) == 0.0) {
                        presentProd += Math.log(0.01);
                        absentProd += Math.log(0.99);
                    }
                    else if (profiles.get(k) == 1) {
                        presentProd += Math.log(0.99);
                        absentProd += Math.log(0.01);
                    } else {
                        presentProd += Math.log(profiles.get(k));
                        absentProd += Math.log(profiles.get(k));
                    }
                }
                if (j == 0)
                    probability += presentProd + absentProd;
                else
                    probability += Math.log(0.8) + presentProd + absentProd;
            }
            probabilities.add(probability);
        }

        int index = 0;
        return index;
    }

    /**
     *
     * Increases the probability of notes in the piece's key
     * signature being included in the output by adding extra
     * pitch arrays of the key signature's notes.
     *
     */
    // TODO: Modify so the pitch octave is random
    private void UpdatePitchMap() {

        int[] keySig = keys.get(sig);


        // Attempt to add new pitches to MarkovTable's chord HashMap.
        // Also add newly added pitches to an array for later probability calculations.
        for (int pitch : keySig) {
            int[] pitchArr = new int[]{pitch};
            if (MarkovTable.chord.putIfAbsent(pitchArr, MarkovTable.chordKey) == null) {
                MarkovTable.chordKey++;
                freshPitches.add(pitchArr);
            }
        }
    }


    public void fillProbabilities(double[][] old) {
        for (int x = 0; x < old.length; ++x) {
            for (int y = 0; y < old.length; ++y) {
                probabilities[x][y] = old[x][y];
            }
        }
    }

    @Override
    protected void calculateProbabilities() {

        if(weight == 1.0)
            weight = .999;
        int freshIndex = cardinality - freshPitches.size();
        double adjustedWeight;
        double adjustedSum;

        // Normalize the probabilities so each row adds to 1.0
        // and make the weight be the probability a new value
        // is transitioned to, i.e., a .25 weight will transition to
        // a fresh pitch 25% of the time
        for (int x = 0; x < cardinality; ++x) {
            // add up the old row
            double sum = 0;
            for (int y = 0; y < freshIndex; ++y) {
                sum += probabilities[x][y];
            }

            // increase the sum to it represents 'weight' more data
            adjustedSum = sum / (1.0 - weight);
            // adjust the weight  to be (amount of new probability data) / (number of new pitches * adjusted sum)
            adjustedWeight = (adjustedSum - sum) / (freshPitches.size() * adjustedSum);

            // divide by the total to normalize old probabilities
            for (int y = 0; y < freshIndex; ++y) {
                probabilities[x][y] /= adjustedSum;
            }

            // set new probabilities equal to the adjusted weight
            for (int y = freshIndex; y < cardinality; ++y) {
                probabilities[x][y] = adjustedWeight;
            }
        }

        // increase the sum to it represents 'weight' more data
        adjustedSum = freshIndex / (1.0 - weight);
        // adjust the weight  to be (amount of new probability data) / (number of new pitches * adjusted sum)
        adjustedWeight = (adjustedSum - freshIndex) / (freshPitches.size() * adjustedSum);
        // Modify outgoing transition probabilities for fresh notes
        for (int x = freshIndex; x < cardinality; ++x) {
            for (int y = 0; y < freshIndex; ++y) {
                probabilities[x][y] = 1.0 / (freshIndex * adjustedSum);
            }

            for (int y = freshIndex; y < cardinality; ++y) {
                probabilities[x][y] = adjustedWeight;
            }
        }
    }
}
