/**
 * @filename SettingsListener.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * A listener interface for setting events.
 */

package coms;

import model.RNote;

public interface SettingsListener {
    
    // Visuals/MIDI
    public RNote[] getNotes();
    public void setPlayTime(double playTime);
    public double getPlayTime();
    public void skipMusicPlayTime();
    
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
    public void setKeySignatureWeight(double keySignatureWeight);
    public double getKeySignatureWeight();
    
    // Preferences
    // Follow
    public void setFollow(boolean follow);
    public boolean getFollow();
    // loadSmpleSettings
    public void setLoadSampleSettings(boolean load);
    public boolean getLoadSampleSettings();
    
}
