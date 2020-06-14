package game.view;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Java class that allows to change the scheme of the application in graphic view by setting
 * the appropriate LookAndFeel in the UIManager. This class uses the flatlaf library
 * to provides a light and a dark scheme.
 */
public class SwitchScheme {

    /**Allows to stock the current used scheme*/
    private static Scheme currentScheme;

    /**The available schemes*/
    public enum Scheme {
        LIGHT,
        DARK,
        NIMBUS,
        SYSTEM
    }

    /**
     * Allows to change the scheme of the application depending
     * on the given scheme.
     * @param scheme the chosen scheme
     */
    public static void switchScheme(Scheme scheme) {
        currentScheme = scheme;
        switch (scheme) {
            case DARK:
                changeScheme(FlatDarculaLaf.class.getName());
                break;
            case LIGHT:
                changeScheme(FlatLightLaf.class.getName());
                break;
            case NIMBUS:
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        changeScheme(info.getClassName());
                        break;
                    }
                }
                break;
            case SYSTEM:
                    changeScheme(UIManager.getSystemLookAndFeelClassName());
                break;
        }
    }

    /**
     * Allows to change the LookAndFeel of the UIManager using the className of the new laf.
     * @param laf the new LookAndFeel classname.
     */
    private static void changeScheme(String laf){
        try {
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            switchScheme(Scheme.LIGHT);
        }
    }

    /**
     * @return the current scheme
     */
    public static Scheme getCurrentScheme() {
        return SwitchScheme.currentScheme;
    }

    public static boolean isDark(){
        return currentScheme == Scheme.DARK;
    }

}
