public class MusicGenerator {

    private int[][] table;
    MarkovTable mTable;
    WaveFC wfc;

    public MusicGenerator(String filename)
    {
        mTable = new MarkovTable(filename);
        wfc = new WaveFC();
    }

    public void generateMusic(int length)
    {
        // use wfc and mTable
    }

    public void playSong()
    {

    }

    public void saveSong()
    {

    }
}
