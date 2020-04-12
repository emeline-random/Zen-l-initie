package Game;

import java.awt.*;
import java.io.Serializable;

public class Element implements Serializable {

    private int x;
    private int y;
    private int lineIndex;
    private int columnIndex;
    private Rectangle rectangle;
    private static Color impairColor = Color.white;
    private static Color pairColor = Color.black;
    protected static int sizeUnity = 50;

    public Element() {
        this.x = -1;
        this.y = -1;
        this.rectangle = null;
    }

    public Element(int lineIndex, int columnIndex) {
        if (lineIndex >= 0 && columnIndex >= 0) {
            this.lineIndex = lineIndex;
            this.columnIndex = columnIndex;
            this.x = (this.columnIndex + 1) * Element.sizeUnity;
            this.y = (this.lineIndex + 1) * Element.sizeUnity;
            this.rectangle = new Rectangle(this.x, this.y, Element.sizeUnity, Element.sizeUnity);
        }
    }

    public void setPosition(int line, int column) {
        if (line >= 0) {
            this.lineIndex = line;
        }
        if (column >= 0) {
            this.columnIndex = column;
        }
    }

    public int getLineIndex() {
        return this.lineIndex;
    }

    public int getColumnIndex() {
        return this.columnIndex;
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

    protected void paint(Graphics g) {
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

    public Rectangle getRectangle() {
        return this.rectangle;
    }

    private void updateRectangle() {
        this.rectangle = new Rectangle(this.x, this.y, Element.sizeUnity, Element.sizeUnity);
    }

    public static void setImpairColor(Color impairColor) {
        Element.impairColor = impairColor;
    }

    public static void setPairColor(Color pairColor) {
        Element.pairColor = pairColor;
    }
}
