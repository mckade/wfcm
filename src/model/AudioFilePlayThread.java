//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
// NOTE: taken from the jMusic library (http://www.explodingart.com/jmusic)
// and extended to fit our needs
//

package model;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.DataLine.Info;

class AudioFilePlayThread extends Thread {
    byte[] tempBuffer = new byte[1024];
    private AudioInputStream audioInputStream;
    private MusicState ms;

    public AudioFilePlayThread(AudioInputStream strm, MusicState ms)
    {
        this.audioInputStream = strm;
        this.ms = ms;
    }

    public void run() {
        try {
            AudioFormat format = this.audioInputStream.getFormat();
            Info info = new Info(SourceDataLine.class, format);
            SourceDataLine source = (SourceDataLine)AudioSystem.getLine(info);
            source.open(format);
            source.start();

            long prev = System.currentTimeMillis();
            int index;
            while(!ms.isStopped() &&
                    (index = this.audioInputStream.read(this.tempBuffer, 0, this.tempBuffer.length)) != -1) {
                if (index > 0) {
                    source.write(this.tempBuffer, 0, index);
                    while(ms.isPaused()){sleep(100); prev = System.currentTimeMillis();}
                    long cur = System.currentTimeMillis();
                    ms.updateTime(cur - prev);
                    prev = cur;
                }
            }

            source.drain();
            source.stop();
            source.close();
            source.close();
            this.audioInputStream.close();
            ms.songFinished();
        } catch (Exception var5) {
            System.out.println("jMusic AudioFilePlayThread error");
            var5.printStackTrace();
        }
    }
}
