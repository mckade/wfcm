package model;

import java.util.Vector;

public class ChordDuration extends Modifier{

    int distance;
    Vector<double[]> chords;
    int cardinality;
    public ChordDuration(int dist, Vector<double[]> ch, int c)
    {
        // set notes to be the sample notes
        chords = ch;
        // set cardinality to the number of unique sample notes
        cardinality = c;
        // instantiate the probability table
        probabilities = new double[cardinality][cardinality];

        // distance bounds checking
        if(dist < 1)
            distance = 1;
        else if(dist > chords.size() - 1)
            distance = chords.size() - 1;
        else
            distance = dist;

        calculateProbabilities();
    }


    @Override
    protected void calculateProbabilities() {

        int i = 0, first, second;
        // holds the number of occurances of note[i]
        // just a dynamic programming trick to avoid rescanning arrays
        int[] counts = new int[cardinality];
        while(i + distance < chords.size())
        {
            // index of note[i]'s pitch
            first = MarkovTable.chordLength.get(chords.get(i)[chords.get(i).length - 1]);
            // index of note[i+distance]'s pitch
            second = MarkovTable.chordLength.get(chords.get(i+distance)[chords.get(i+distance).length - 1]);
            probabilities[first][second] += 1.0;
            counts[first] += 1;

            i++;
        }

        // normalize the counts to make them probabilities
        // if no data was seen for a note, make all transitions
        // from that note have equal probability
        for(int x = 0; x < probabilities.length; x++)
        {
            for(int y = 0; y < probabilities.length; y++)
            {
                if(counts[x] == 0)
                    probabilities[x][y] = 1.0 / cardinality;
                else
                    probabilities[x][y] /= counts[x];
            }
        }
    }
}
