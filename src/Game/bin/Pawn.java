package Game.bin;


import Utilities.GameColor;

import java.awt.*;
import java.io.Serializable;

/**
 * Java class that allows to create pawns that all have the same diameter.Each pawn has its own
 * number and its own color.
 */
public class Pawn extends Element implements Serializable {

    /**
     * The diameter af all pawns
     */
    private static int diameter = Element.sizeUnity - Element.sizeUnity / 4;
    /**
     * The color of the pawn
     */
    private final GameColor color;
    /**
     * The ANSI escape code that represents the color
     */
    private final String colorString;
    /**
     * The number of the pawn
     */
    private final int number;

    /**
     * Constructor of the class, allows to initialize the number,
     * the color and the ANSI escape code associate to the color.
     * Calls the superclass constructor with no argument.
     *
     * @param color  the color of the pawn
     * @param number the number of the pawn
     */
    public Pawn(GameColor color, int number) {
        super();
        if (color != null && (number >= 0 || number == -1)) {
            this.color = color;
            this.colorString = color.ANSI_CODE;
            this.number = number;
        } else {
            throw new IllegalArgumentException("Pawn constructor exception");
        }
    }

    /**
     * Allows to paint the pawn by calling the superclass paint(Graphics g) method
     * and by drawing and filling a circle with its color. Displays its string
     * representation on the circle.
     *
     * @param g The graphics to draw on
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(this.color);
        g.fillOval((int) ((this.getColumnIndex() + 1) * Element.sizeUnity + 1 / 8d * Element.sizeUnity),
                (int) ((this.getLineIndex() + 1) * Element.sizeUnity + 1 / 8d * Element.sizeUnity), diameter, diameter);
        g.setColor(Color.black);
        g.drawOval((int) ((this.getColumnIndex() + 1) * Element.sizeUnity + 1 / 8d * Element.sizeUnity),
                (int) ((this.getLineIndex() + 1) * Element.sizeUnity + 1 / 8d * Element.sizeUnity), diameter, diameter);
        g.drawString(this.toString(), (int) ((this.getColumnIndex() + 1.4) * Element.sizeUnity),
                (int) ((this.getLineIndex() + 1.6) * Element.sizeUnity));
        this.setX((this.getColumnIndex() + 1) * Element.sizeUnity);
        this.setY((this.getLineIndex() + 1) * Element.sizeUnity);
    }

    /**
     * String representation of the pawn.
     *
     * @return the number of the pawn or z if the number is -1
     */
    @Override
    public String toString() {
        if (this.number != -1) return Integer.toString(this.number);
        else return "z";
    }

    /**
     * Allows to set the diameter for all pawns.
     *
     * @param diameter the new diameter
     */
    public static void setDiameter(int diameter) {
        if (diameter >= 0) {
            Pawn.diameter = diameter;
        }
    }

    /**
     * Allows to get the number of the pawn.
     *
     * @return the pawn's number
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Allows to get the ANSI escape code to display the color
     * of the pawn in console mode.
     *
     * @return the ANSI escape code
     */
    public String getANSIColor() {
        return this.colorString;
    }

}
