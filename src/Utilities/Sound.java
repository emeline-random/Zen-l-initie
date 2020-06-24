package utilities;


import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Allows the application to performs res.sounds on different actions.
 */
public class Sound {

    /**
     * Indicates if the sound of the application is activated or not
     */
    private static boolean on = true;
    /**
     * The list containing the played ins background sounds
     */
    private static final ArrayList<Clip> CLIPS = new ArrayList<>();

    /**
     * The available res.sounds
     */
    public enum Sounds {
        MOVE,
        IMPOSSIBLE_MOVE,
        BUTTON_PRESSED,
        EAT,
        MENU
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
                    Clip clip = getOpenedClip("res/sounds/" + s.name().toLowerCase() + ".wav");
                    clip.start();
                    Thread.sleep(clip.getFrameLength());
                    clip.stop();
                    clip.close();
                } catch (InterruptedException e) {
                    System.out.println("an error occurred while playing the sound, you can try to set it off then on.");
                }
            }).start();
        }
    }

    /**
     * Allows to play a sound in background as long as the sound is not turned off
     */
    private static void backgroundSound() {
        new Thread(() -> {
            try {
                Clip clip = getOpenedClip("res/sounds/menu.wav");
                CLIPS.add(clip);
                while (on && CLIPS.contains(clip)) {
                    clip.start();
                    Thread.sleep(127000);
                    clip.stop();
                    clip.setFramePosition(0);
                }
            } catch (Exception e) {
                System.out.println("an error occurred while playing the sound, you can try to set it off then on.");
            }
        }).start();
    }

    /**
     * Allows to get a clip that is ready to be played
     *
     * @param path the path of the sound
     * @return the corresponding clip
     */
    private static Clip getOpenedClip(String path) {
        Clip clip = null;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getClassLoader().getResource(path));
            clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, audioInputStream.getFormat()));
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.out.println("an error occurred while playing the sound, you can try to set it off then on.");
        }
        return clip;
    }

    /**
     * Allows to get the image corresponding to the actual state of the sound
     *
     * @param lightIconOnly allows to get only light icons (for the menu)
     * @return the image representing the state of the sound
     */
    public static ImageIcon getImage(boolean lightIconOnly) {
        if (lightIconOnly) {
            if (on) return ViewUtilities.getSchemeIcon("soundOn.png", true);
            else return ViewUtilities.getSchemeIcon("soundOff.png", true);
        } else {
            if (on) return ViewUtilities.getSchemeIcon("soundOn2.png", true);
            else return ViewUtilities.getSchemeIcon("soundOff2.png", true);
        }
    }

    /**
     * Allows to stop the background music
     */
    public static void stopBackground() {
        for (Clip clip : CLIPS) {
            if (clip.isOpen()) {
                clip.stop();
                clip.close();
            }
        }
        CLIPS.clear();
    }

    /**
     * @return True if the sound is activated, false otherwise.
     */
    public static boolean isOn() {
        return on;
    }

    /**
     * Allows to turn on and off the sound, stop the background music at first.
     *
     * @param isOn            True if the sound should be on, false otherwise
     * @param startBackground True to set the background music on (only if sound is on)
     */
    public static void setOn(boolean isOn, boolean startBackground) {
        on = isOn;
        stopBackground();
        if (on && startBackground) backgroundSound();
    }
}
