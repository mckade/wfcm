package model;

public class KeySignatureMod extends Modifier {

    private int sig;

    public KeySignatureMod(int sig) {
        // Set key signature to weight
        this.sig = sig;
    }

    @Override
    protected void calculateProbabilities() {

    }
}
