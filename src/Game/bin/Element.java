package game.bin;

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
    private static int sizeUnity = 50;

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
        } else throw new IllegalArgumentException();
    }

    /**
     * Allows to fill a rectangle which corresponds to the square of the Element
     * on the board. The color of the rectangle depends on if the sum of both
     * size and columns indexes is pair or impair.
     * @param g the graphics fo fill the rectangle.
     */
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

    /**
     * Allows to update the rectangle object, creates a new Rectangle with the
     * size unity of an Element and the x and y attributes of this element.
     */
    private void updateRectangle() {
        this.rectangle = new Rectangle(this.x, this.y, Element.sizeUnity, Element.sizeUnity);
    }

    /**
     * Allows to set the position of an element in the board with an index
     * for the line and another for the column. Both indexes need to be more
     * or equal to 0.
     * @param line the index of the line
     * @param column the index of the column
     */
    public void setPosition(int line, int column) {
        if (line >= 0 && column >= 0) {
            this.lineIndex = line;
            this.columnIndex = column;
        } else throw new IllegalArgumentException();
    }

    /**
     * Allows to set the absolute x position of the upper left corner of the rectangle
     * that represents the element.
     * @param x the absolute horizontal value
     */
    public void setX(int x) {
        if (x >= 0) {
            this.x = x;
            updateRectangle();
        } else throw new IllegalArgumentException();
    }

    /**
     * Allows to set the absolute y position of the upper left corner of the rectangle
     * that represents the element.
     * @param y the absolute vertical value
     */
    public void setY(int y) {
        if (y >= 0) {
            this.y = y;
            updateRectangle();
        } else throw new IllegalArgumentException();
    }

    /**
     * @return the rectangle that represents the shape on the board
     */
    public Rectangle getRectangle() {
        return this.rectangle;
    }

    /**
     * @return the line index of the element
     */
    public int getLineIndex() {
        return this.lineIndex;
    }

    /**
     * @return The column index of the element
     */
    public int getColumnIndex() {
        return this.columnIndex;
    }

    /**
     * Allows to set the color of all rectangles that the sum
     * of both line and column indexes is impair.
     * @param impairColor the color for impair squares
     */
    public static void setImpairColor(Color impairColor) {
        Element.impairColor = impairColor;
    }

    /**
     * Allows to set the color of all rectangles that the sum
     * of both line and column indexes is pair.
     * @param pairColor the color for pair squares
     */
    public static void setPairColor(Color pairColor) {
        Element.pairColor = pairColor;
    }

    /**
     * Allows to set the size of all sides of all rectangles on the board.
     * This size needs to be at least equals to 0.
     * @param sizeUnity the nex size.
     */
    public static void setSizeUnity(int sizeUnity){
        if (sizeUnity >= 0) {
            Element.sizeUnity = sizeUnity;
        } else throw new IllegalArgumentException();
    }

    /**
     * @return the size of all sides of all rectangles on the board.
     */
    public static int getSizeUnity(){
        return sizeUnity;
    }
}
