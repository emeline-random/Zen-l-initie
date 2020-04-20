package Game.bin;

import java.awt.*;
import java.io.Serializable;

/**
 * Java class that allows to represent any element on the board. Every
 * object that is on the board extends this class that allows to save
 * the position of elements.
 */
public class Element implements Serializable {

    /**The absolute x position in graphic mode*/
    private int x;
    /**The absolute y position in graphic mode*/
    private int y;
    /**The vertical position on the board*/
    private int lineIndex;
    /**The horizontal position on the board*/
    private int columnIndex;
    /**The rectangle corresponding to the square occupied by the element */
    private Rectangle rectangle;
    /**The color of all impair squares*/
    private static Color impairColor = Color.white;
    /**The color of all pair squares*/
    private static Color pairColor = Color.black;
    /**The size of the sides of all squares*/
    protected static int sizeUnity = 50;

    /**
     * Allows to create an element that has no significant information.
     * The x and y values are set to -1 and the rectangle to null.
     */
    public Element() {
        this.x = -1;
        this.y = -1;
        this.rectangle = null;
    }

    /**
     * Allows to create an element with a precise position. The x and
     * y values are computed and the corresponding rectangle is created.
     * @param lineIndex the line where the element is
     * @param columnIndex the column where the element is
     */
    public Element(int lineIndex, int columnIndex) {
        if (lineIndex >= 0 && columnIndex >= 0) {
            this.lineIndex = lineIndex;
            this.columnIndex = columnIndex;
            this.x = (this.columnIndex + 1) * Element.sizeUnity;
            this.y = (this.lineIndex + 1) * Element.sizeUnity;
            this.rectangle = new Rectangle(this.x, this.y, Element.sizeUnity, Element.sizeUnity);
        }
    }

    public void paint(Graphics g) {
        this.x = (this.columnIndex + 1) * Element.sizeUnity;
        this.y = (this.lineIndex + 1) * Element.sizeUnity;
        updateRectangle();
        if ((this.lineIndex + this.columnIndex) % 2 == 1) {
            g.setColor(impairColor);
        } else {
            g.setColor(pairColor);
        }
        g.fillRect(this.x, this.y, sizeUnity, sizeUnity);
    }

    private void updateRectangle() {
        this.rectangle = new Rectangle(this.x, this.y, Element.sizeUnity, Element.sizeUnity);
    }

    public void setPosition(int line, int column) {
        if (line >= 0) {
            this.lineIndex = line;
        }
        if (column >= 0) {
            this.columnIndex = column;
        }
    }

    public void setX(int x) {
        if (x >= 0) {
            this.x = x;
            updateRectangle();
        }
    }

    public void setY(int y) {
        if (y >= 0) {
            this.y = y;
            updateRectangle();
        }
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }

    public int getLineIndex() {
        return this.lineIndex;
    }

    public int getColumnIndex() {
        return this.columnIndex;
    }

    public static void setImpairColor(Color impairColor) {
        Element.impairColor = impairColor;
    }

    public static void setPairColor(Color pairColor) {
        Element.pairColor = pairColor;
    }

    public static void setSizeUnity(int sizeUnity){
        Element.sizeUnity = sizeUnity;
    }

    public static int getSizeUnity(){
        return sizeUnity;
    }
}
