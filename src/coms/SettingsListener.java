/**
 * @filename SettingsListener.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * A listener interface for setting events.
 */

package coms;

public interface SettingsListener {
    public void setNoteCount(int noteCount);
    public int getNoteCount();
    public void setTempo(int tempo);
    public int getTempo();
    public void setInstrument(String instrument);
    public String getInstrument();
    public String[] getInstrumentList();
}
