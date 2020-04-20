package Utilities;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {

    private static boolean on = true;

    public enum Sounds {
        move,
        impossibleMove,
        buttonPressed,
        eat
    }

    public static void play(Sounds s){
        if (Sound.isOn()) {
            new Thread(() -> {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/" + s.name() + ".wav"));
                    clip.open(audioInputStream);
                    clip.start();
                    Thread.sleep(clip.getFrameLength());
                    clip.stop();
                    clip.close();
                } catch (IOException | LineUnavailableException | UnsupportedAudioFileException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static boolean isOn() {
        return on;
    }

    public static void setOn(boolean isOn) {
        on = isOn;
    }
}
