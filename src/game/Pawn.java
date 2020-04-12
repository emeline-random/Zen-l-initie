package Game;


import Utilities.GameColor;

import java.awt.*;
import java.io.Serializable;

public class Pawn extends Element implements Serializable {

    private static int diameter = Element.sizeUnity - Element.sizeUnity / 4;
    private final static int DIMENSION = GameBoard.getDIMENSION();
    private GameColor color;
    private String colorString;
    private int number;

    public Pawn(GameColor color, Player player, int number) {
        super();
        if (color != null && player != null && number >= 0) {
            this.color = color;
            this.colorString = color.ANSI_CODE;
            this.number = number;
        }
    }

    public Pawn() {
        super();
        this.color = GameColor.RED;
        this.colorString = GameColor.RED.ANSI_CODE;
        this.number = -1;
    }

    protected void movePawn(int linePosition, int columnPosition) {
        if (columnPosition < DIMENSION && columnPosition >= 0 && linePosition < DIMENSION && linePosition >= 0) {
            this.setPosition(linePosition, columnPosition);
        }
    }

    @Override
    public String toString() {
        if (this.number != -1) return Integer.toString(this.number);
        else return "z";
    }

    @Override
    protected void paint(Graphics g) {
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

    public int getNumber() {
        return this.number;
    }

    public static void setDiameter(int diameter) {
        Pawn.diameter = diameter;
    }

    public String getColorString() {
        return this.colorString;
    }

}
