/**
 * @filename SettingsListener.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * A listener interface for setting events.
 */

package coms;

import java.awt.Rectangle;

public interface SettingsListener {
    
    // Visuals/MIDI
    public Rectangle[] getNotes();
    public void setPlayTime(double playTime);
    public double getPlayTime();
    
    // Settings
    // Note count
    public void setNoteCount(int noteCount);
    public int getNoteCount();
    
    // Tempo
    public void setTempo(int tempo);
    public int getTempo();
    
    // Instrument
    public void setInstrument(String instrument);
    public String getInstrument();
    public String[] getInstrumentList();
    
    // Signatures
    public void setTimeSignature(int timeSignature);
    public int getTimeSignature();
    
    // Preferences
    // Follow
    public void setFollow(boolean follow);
    public boolean getFollow();
}
