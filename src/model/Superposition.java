package model;
/**
 * @filename Superposition.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * An object to encapsulate a possibility superposition and
 * its operations
 */


public class Superposition
{
    private int[] sp;
    Superposition prev;
    Superposition next;
    PTable prob;

    public Superposition(PTable pr, Superposition p, Superposition n)
    {
        sp = new int[pr.size()];
        for(int i = 0; i < pr.size(); i++)
        {
            sp[i] = 1;
        }

        prev = p;
        next = n;
        prob = pr;
    }

    public void reset()
    {
        for(int i = 0; i < sp.length; i++)
        {
            sp[i] = 1;
        }

        if(next != null)
            next.reset();
    }

    public void print()
    {
        System.out.print("[ ");
        for(int i = 0; i < sp.length; i++)
        {
            System.out.print(sp[i] + " ");
        }
        System.out.print("]\n");
    }

    public void setNext(Superposition n)
    {
        next = n;
    }

    public Superposition getNext()
    {
        return next;
    }

    public int getPTableSize()
    {
        return prob.size();
    }

    public int getSPAt(int i)
    {
        return sp[i];
    }

    public int[] getSP()
    {
        return sp;
    }

    public int size()
    {
        int count = 0;
        for(int i = 0; i < sp.length; i++)
        {
            if(sp[i] == 1)
                count++;
        }
        return count;
    }

    public void collapse()
    {
        double[] probabilities = getProbabilities();

        // random roll between 0 and 1
        double rnd = Math.random();
        // find the value associated with the roll
        double sum = 0.0f;
        // for each note in the table
        for(int i = 0; i < prob.size(); i++)
        {
            // if the value was picked
            if(probabilities[i] + sum >= rnd)
            {
                // define this superposition as that value
                for(int j = 0; j < sp.length; j++)
                {
                    if(i != j)
                    {
                        sp[j] = 0;
                    }
                    else
                    {
                        sp[j] = 1;
                    }
                }

                // Propagate the constraints of the selection
                if (prev != null)
                {
                    prev.propagateLeft(sp);
                }
                if (next != null)
                {
                    next.propagateRight(sp);
                }

                return;
            }

            sum += probabilities[i];
        }
    }

    /*
     * Using the previous and next superpositions, find the
     * transition probabilities of the current superposition
     */
    private double[] getProbabilities()
    {
        // holds probabilities of how the prev note can transition given this sp
        // i.e., what are the transition probabilities into this sp
        double [] prevp = new double[sp.length];
        // holds probabilities of how this note can transition given next sp
        // i.e., what are the transition probabilities out of this sp
        double [] nextp = new double[sp.length];
        double [] cur = new double[sp.length];

        double prevsum = 0.0;
        double cursum = 0.0;

        // aggregate prev and next probabilities
        for(int i = 0; i < sp.length; i++)
        {
            if(prev != null && prev.getSPAt(i) != 0)
            {
                add(prevp, getRowProbability(i, sp));
            }

            if(next != null && sp[i] != 0)
            {
                add(nextp, getRowProbability(i, next.getSP()));
            }
        }

        if(next != null)
        {
            // Given the probability of the next sp, calculate the probability
            // of this one. That is, what is the chance I am sp[i]?
            // This is done by weighting this sp's transitions from r with the
            // probability this note is r. Then, multiply by sp[c] to eliminate
            // impossibilities.
            for(int r = 0; r < sp.length; r++)
            {
                if(sp[r] == 0)
                    continue;
                for(int c = 0; c < sp.length; c++)
                {
                    cur[r] += nextp[c] * prob.get(r,c);
                }
            }
        }

        // normalize prev and next probabilities
        for(int i = 0; i < sp.length; i++)
        {
            if(prev != null)
            {
                prevsum += prevp[i];
            }
            if(next != null)
            {
                cursum += cur[i];
            }
        }

        for(int i = 0; i < sp.length; i++)
        {
            if(prev != null)
            {
                prevp[i] /= prevsum;
            }

            if(next != null)
            {
                cur[i] /= cursum;
            }
        }

        // average cur and next
        if(prev == null)
            return cur;
        else if(next == null)
            return prevp;

        for(int i = 0; i < sp.length; i++)
        {
            cur[i] = (cur[i] + prevp[i]) / 2.0;
        }

        return cur;
    }

    /*
     * This function returns the probabilities of how this row, r, can
     * transition given a mask
     * The mask is an array of 0s and 1s denoting the notes in the next superposition,
     * i.e., the notes that are still possible to go to
     */
    private double[] getRowProbability(int r, int[] mask)
    {
        double[] res = new double[sp.length];

        double sum = 0.0;
        for(int i = 0; i < sp.length; i++)
        {
            res[i] = mask[i] * prob.get(r, i);
            sum += res[i];
        }

        // normalize res
        for(int i = 0; i < sp.length; i++)
        {
            if(mask[i] != 0)
            {
                res[i] /= sum;
            }
        }

        return res;
    }

    /*
     * Add this array to another and store it in this
     */
    private void add(double[] ths, double[] other)
    {
        for(int i = 0; i < ths.length; i++)
        {
            ths[i] += other[i];
        }
    }

    /*
     * Recursively propagate constraints to the prev superposition
     */
    private void propagateLeft(int[] n)
    {
        boolean changed = false;

        for(int i = 0; i < sp.length; i++)
        {
            if(!hasTransitionFrom(i, n))
            {
                // there is no transition, this value cannot exist
                sp[i] = 0;
                changed = true;
            }
        }

        if(prev != null && changed == true)
            prev.propagateLeft(sp);
    }

    /*
     * Recursively propagate constraints to the next superposition
     */
    private void propagateRight(int[] p)
    {
        boolean changed = false;

        for(int i = 0; i < sp.length; i++)
        {
            if(!hasTransitionTo(i, p))
            {
                // there is no transition, this value cannot exist
                sp[i] = 0;
                changed = true;
            }
        }

        if(next != null && changed == true)
            next.propagateRight(sp);
    }

    /*
     * Returns true if a transition from sp[from] to t exists
     */
    private boolean hasTransitionFrom(int from, int[] t)
    {
        // if this value doesn't exist in the superposition, there can
        // be no transitions out of it
        if(sp[from] == 0)
            return false;

        for(int i = 0; i < t.length; i++)
        {
            // if the probability from value 'from' to value 'i' exists and
            // t[i] exists in the superposition, a transition exists
            if(t[i] * prob.get(from, i) != 0)
            {
                return true;
            }
        }

        return false;
    }

    /*
     * Returns true if a transition from f to sp[to] exists
     */
    private boolean hasTransitionTo(int to, int[] f)
    {
        // if this value doesn't exist in the superposition, there can
        // be no transitions into it
        if(sp[to] == 0)
            return false;

        for(int i = 0; i < f.length; i++)
        {
            // if the probability from value 'i' to value 'to' exists and
            // f[i] exists in the superposition, a transition exists
            if(f[i] * prob.get(i, to) != 0)
            {
                return true;
            }
        }

        return false;
    }
}
