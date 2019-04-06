package model;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;

import coms.UpdateEvent;
import coms.UpdateListener;
import coms.UpdateType;

/*
 * MusicState acts as an interface between the midi player
 * and the GUI.
 */
public class MusicState implements MetaEventListener
{
    // default output file
    static String OUTPUT = "out.MID";

    // midi player thread
    private AudioFilePlayThread audio;

    // Listener to send events to
    private UpdateListener listener;

    // playback progress
    private double playPercentage = 0.0;

    MusicState(UpdateListener listener) {
        this.listener = listener;
    }

    // Play the MIDI file located at OUTPUT
    void play() {
        if(audio != null)
            return;

        audio = new AudioFilePlayThread(this, playPercentage);
        audio.start();
    }

    // Stop the midi player
    void stop()
    {
        if(audio == null)
            return;
        audio.stopMusic();
        try {
            audio.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audio = null;
        System.out.println("Audio play thread stopped");
    }

    // Notified when the thread currently playing the midi is finished
    private void songFinished()
    {
        listener.updateEvent(new UpdateEvent(this, UpdateType.music));
    }

    // Pause the midi player
    void pause()
    {
        if(audio == null)
            return;
        audio.pause();
    }

    // unpause the midi player
    void unpause()
    {
        if(audio == null)
            return;
        audio.unpause();
    }

    void skip(double percentage)
    {

        if(audio !=  null)
            audio.skip(percentage);
    }

    // Send an event to update the visualizer scroll bar.
    // This makes the scroll window keep the played note in frame
    void scroll(double percentage)
    {
        UpdateEvent e = new UpdateEvent(this, UpdateType.scrollBar);
        e.setScroll(percentage);
        listener.updateEvent(e);
    }

    // Listen for Meta Events from the midi player
    @Override
    public void meta(MetaMessage meta)
    {
        if(meta.getType() == 47)
        {
            // just received a stop message
            songFinished();
            audio.stopMusic();
        }
    }
}
