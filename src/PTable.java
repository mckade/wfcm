/**
 * @filename PTable.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * An object to encapsulate a probability table and its functionality
 */

public class PTable
{
    private double[][] table;
    public PTable(double[][] t)
    {
        if(t == null || t.length == 0)
        {
            System.out.println("ERROR in PTable: table cannot be empty");
            System.exit(1);
        }
        if(t.length != t[0].length)
        {
            System.out.println("ERROR in PTable: table dimensions must match");
            System.exit(1);
        }

        table = t;
    }

    public double get(int x, int y)
    {
        return table[x][y];
    }

    public void print()
    {
        for (int i = 0; i < table.length; i++)
        {
            System.out.print("[");
            for (int j = 0; j < table.length; j++)
            {
                String val = String.format("%.2f", table[i][j]);
                System.out.print(" "+val+" ");
            }
            System.out.println("]");
        }
    }

    public int size()
    {
        return table.length;
    }
}
