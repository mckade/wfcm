package model;
/**
 * @filename Modifier.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * Abstract class for probability modifiers,
 * e.g., 1-1 note transitions, rhythms, etc.
 * */

public abstract class Modifier
{
    protected double[][] probabilities;
    protected abstract void calculateProbabilities();

    public double[][] getProbabilities()
    {
        return probabilities;
    }
}
