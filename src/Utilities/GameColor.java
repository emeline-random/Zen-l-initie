package utilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * class that allows to create personalized colors, it extends AWT Color object so can be used
 * as so but can also be used in a console with it's ANSI_CODE. It will also prints the name
 * of the color instead of the rgb values usually given by Color class.
 * This class defines some static instances of itself that may be useful in preventing the
 * re-creation of similar objects..
 */
public class GameColor extends Color {

    public final static GameColor RED = new GameColor(255, 0, 0, "\u001B[31m", "red");
    public final static GameColor YELLOW = new GameColor(255, 250, 205, "\u001B[33m", "yellow");
    public final static GameColor GREEN = new GameColor(144, 238, 144, "\033[38;5;28m", "green");
    public final static GameColor PINK = new GameColor(255, 228, 225, "\033[38;5;206m", "pink");
    public final static GameColor BLUE = new GameColor(135, 206, 250, "\u001B[34m", "blue");
    public final static GameColor PURPLE = new GameColor(230, 230, 250, "\033[38;5;91m", "purple");
    public final static GameColor WHITE = new GameColor(255, 255, 255, "\u001B[37m", "white");
    public final static GameColor CYAN = new GameColor(224, 255, 255, "\u001B[36m", "cyan");
    public final static GameColor ORANGE = new GameColor(255, 218, 185, "\033[38;5;202m", "orange");
    public final static GameColor BROWN = new GameColor(210, 180, 140, "\033[38;5;88m", "brown");
    public final static GameColor BURGUNDY = new GameColor(178, 34, 34, "\033[38;5;124m", "burgundy");
    public final static GameColor TURQUOISE = new GameColor(175, 238, 238, "\033[38;5;87m", "turquoise");

    /**
     * the ansi code to print a text in the color in a console
     */
    public final String ANSI_CODE;
    /**
     * the name of the color
     */
    public final String NAME;

    /**
     * Constructor of the class, initializes the object with the given values and creating the
     * awt Color object with the given rgb values.
     *
     * @param r    red coefficient
     * @param g    green coefficient
     * @param b    blue coefficient
     * @param code the ansi escape code linked to the color
     * @param name the name of the color
     */
    public GameColor(int r, int g, int b, String code, String name) {
        super(r, g, b);
        if (code != null && name != null) {
            this.ANSI_CODE = code;
            this.NAME = name;
        } else {
            throw new IllegalArgumentException("GameColor initialization error");
        }
    }

    /**
     * Allows to get all the colors that are initialize in class variables
     * in a table
     *
     * @return the 12 GameColor objects that are initialize above
     */
    public static GameColor[] getColors() {
        return new GameColor[]{BLUE, BROWN, BURGUNDY, CYAN, GREEN, ORANGE, PINK, PURPLE, TURQUOISE, WHITE, YELLOW};
    }

    /**
     * Allows to get a random that is one of the 12 colors
     * defined in the class. If the color parameter is not null, the method
     * will not returned this color.
     *
     * @param color the color that must not be returned (can be null)
     * @return a random GameColor
     */
    public static GameColor getRandomColor(GameColor color) {
        ArrayList<GameColor> colors = new ArrayList<>(Arrays.asList(GameColor.getColors()));
        if (color != null) {
            colors.remove(color);
        }
        return colors.get((int) (Math.random() * colors.size()));
    }

    /**
     * toString method of the class that simply returns the NAME attribute of the
     * GameColor object
     *
     * @return the name of the color
     */
    public String toString() {
        return this.NAME;
    }
}
