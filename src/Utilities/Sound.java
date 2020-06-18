package utilities;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.IOException;

/**
 * Allows the application to performs sounds on different actions.
 */
public class Sound {

    /**
     * Indicates if the sound of the application is activated or not
     */
    private static boolean on = true;

    /**
     * The available sounds
     */
    public enum Sounds {
        MOVE,
        IMPOSSIBLE_MOVE,
        BUTTON_PRESSED,
        EAT
    }

    /**
     * Allows to play a sound of the Sounds enumeration in a new Thread.
     * When the sound is over, the clip is close.
     *
     * @param s one of the Sounds sound.
     */
    public static void play(Sounds s) {
        if (Sound.isOn()) {
            new Thread(() -> {
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getClassLoader().getResource
                            ("sounds/" + s.name().toLowerCase() + ".wav"));
                    DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
                    Clip clip = (Clip) AudioSystem.getLine(info);
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

    public static ImageIcon getImage(boolean lightIconOnly) {
        if (lightIconOnly){
            if (on) return ViewUtilities.getSchemeIcon("soundOn.png");
            else return ViewUtilities.getSchemeIcon("soundOff.png");
        }else {
            if (on) return ViewUtilities.getSchemeIcon("soundOn2.png");
            else return ViewUtilities.getSchemeIcon("soundOff2.png");
        }
    }

    /**
     * @return True if the sound is activated, false otherwise.
     */
    public static boolean isOn() {
        return on;
    }

    /**
     * Allows to turn on and off the sound
     *
     * @param isOn True if the sound should be on, false otherwise
     */
    public static void setOn(boolean isOn) {
        on = isOn;
    }
}
