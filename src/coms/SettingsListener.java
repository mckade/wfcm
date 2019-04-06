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
    Rectangle[] getNotes();
    void setPlayTime(double playTime);
    double getPlayTime();
    void onClick();
    
    // Settings
    // Note count
    void setNoteCount(int noteCount);
    int getNoteCount();
    
    // Tempo
    void setTempo(int tempo);
    int getTempo();
    
    // Instrument
    void setInstrument(String instrument);
    String getInstrument();
    String[] getInstrumentList();
    
    // Signatures
    void setTimeSignature(int timeSignature);
    int getTimeSignature();
    
    // Preferences
    // Follow
    void setFollow(boolean follow);
    boolean getFollow();
}
