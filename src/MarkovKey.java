/**
 * @filename MarkovKey
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * A class that will be used as the key in a HashMap, which will be our Markov Table
 * This will make it easier to store the probability of specific interval occurances
 * Based on a suggestion on stackoverflow:
 * https://stackoverflow.com/questions/822322/how-to-implement-a-map-with-multiple-keys
 * from user Guigon
 */

public class MarkovKey {

    public int Note1, Note2;

    // Takes two note pitches
    public MarkovKey(int note1, int note2) {
        this.Note1 = note1;
        this.Note2 = note2;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MarkovKey))
            return false;
        MarkovKey ref = (MarkovKey) obj;
        return this.Note1 == ref.Note1 &&
                this.Note2 == ref.Note2;
    }

    @Override
    public int hashCode() {
        // Might need to rework
        return (int) Math.pow(this.Note1, this.Note2);
    }
}
