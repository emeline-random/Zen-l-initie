package game.view;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

/**
 * Java class that allows to change the scheme of the application in graphic view by setting
 * the appropriate LookAndFeel in the UIManager. This class uses the flatlaf library
 * to provides a light and a dark scheme.
 * This class also defines the colors that will be used in the application.
 */
public class Scheme {

    /**Allows to stock the current used scheme*/
    private static Schemes currentScheme;
    /**
     * the dark color of the game
     */
    public final static Color DARK_COLOR = new Color(26, 14, 5);
    /**
     * the light color of the game
     */
    public final static Color LIGHT_COLOR = new Color(230, 216, 202);

    /**The available schemes*/
    public enum Schemes {
        LIGHT,
        DARK,
        SYSTEM
    }

    /**
     * Allows to change the scheme of the application depending
     * on the given scheme.
     * @param scheme the chosen scheme
     */
    public static void switchScheme(Schemes scheme) {
        currentScheme = scheme;
        switch (scheme) {
            case DARK:
                changeScheme(FlatDarculaLaf.class.getName());
                break;
            case LIGHT:
                changeScheme(FlatLightLaf.class.getName());
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
            switchScheme(Schemes.LIGHT);
        }
    }

    /**
     * @return the current scheme
     */
    public static Schemes getCurrentScheme() {
        return Scheme.currentScheme;
    }

    /**
     * @return true if the current scheme is Scheme.DARK, false otherwise
     */
    public static boolean isDark(){
        return currentScheme == Schemes.DARK;
    }

    /**
     * Allows to get the background color of the scheme that is the light color if the scheme is light
     * and the dark color if it is dark.
     * @return the background of the scheme
     */
    public static Color getSchemeBackground(){
        if (isDark()) return DARK_COLOR;
        else return LIGHT_COLOR;
    }

}
