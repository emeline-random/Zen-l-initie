package Game.bin;

import java.awt.*;

/**
 * Allows to create an Element object that has an image representation.
 */
public class ChineseSymbol extends Element {

    /**
     * the image of the object
     */
    private final Image icon;

    /**
     * Constructor of the class that initializes the image and that also sets
     * the line index and the column index in the superclass Element by calling
     * the appropriate constructor.
     *
     * @param lineIndex   the line index of the element
     * @param columnIndex the column index of the element
     * @param icon        the image that represents the element
     */
    public ChineseSymbol(int lineIndex, int columnIndex, Image icon) {
        super(lineIndex, columnIndex);
        if (icon != null) {
            this.icon = icon;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Allows to redefine the paint method of the super class by adding the draw of the symbol
     * in the graphics of a component. It first calls the paint method of the superclass (that
     * put the background color) and then draws its icon.
     *
     * @param g the Graphics to paint
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Image image = this.icon.getScaledInstance(Element.sizeUnity, Element.sizeUnity, Image.SCALE_DEFAULT);
        g.drawImage(image, (this.getColumnIndex() + 1) * Element.sizeUnity, (this.getLineIndex() + 1) * Element.sizeUnity, null);
    }

}
