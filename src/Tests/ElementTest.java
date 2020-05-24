package tests;

import game.model.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Allows to test the Element class
 */
public class ElementTest {

    /**
     * An element used to make tests
     */
    private Element element;

    /**
     * Allows to initialize the element used for each test
     */
    @Before
    public void setUp() {
        this.element = new Element(1, 2);
    }

    /**
     * Allows to delete the element used
     */
    @After
    public void tearDown() {
        this.element = null;
    }

    /**
     * Tests the setPosition method by checking with the getters the values obtained.
     * Also checks that the method throws an assertion if the given position is not correct.
     */
    @Test
    public void setPosition() {
        assertEquals(1, this.element.getLineIndex());
        assertEquals(2, this.element.getColumnIndex());
        this.element.setPosition(4, 0);
        assertEquals(4, this.element.getLineIndex());
        assertEquals(0, this.element.getColumnIndex());
        assertThrows(IllegalArgumentException.class, () -> this.element.setPosition(-1, 0));
    }

    /**
     * Allows to test the setX() method by checking its calls to the
     * updateRectangle() private method. To do this, the put value is compared
     * to the x rectangle value. Also checks if an exception is thrown when
     * a wrong value is given.
     */
    @Test
    public void setX() {
        this.element.setX(5);
        assertEquals(5, this.element.getRectangle().x);
        this.element.setX(0);
        assertEquals(0, this.element.getRectangle().x);
        assertThrows(IllegalArgumentException.class, () -> this.element.setX(-2));
    }

    /**
     * Allows to test the setY() method by checking its calls to the
     * updateRectangle() private method. To do this, the put value is compared
     * to the y rectangle value. Also checks if an exception is thrown when
     * a wrong value is given.
     */
    @Test
    public void setY() {
        this.element.setY(5);
        assertEquals(5, this.element.getRectangle().y);
        this.element.setY(0);
        assertEquals(0, this.element.getRectangle().y);
        assertThrows(IllegalArgumentException.class, () -> this.element.setY(-1));
    }

    /**
     * Checks that the getRectangle() method by setting the size unity of the Element class
     * and creating a new rectangle corresponding to the rectangle that we should obtain
     * for the element at line 1 and column 2.
     */
    @Test
    public void getRectangle() {
        Rectangle r = new Rectangle(15, 10, 5, 5);
        Element.setSizeUnity(5);
        Element e = new Element(1, 2);
        assertEquals(r, e.getRectangle());
    }

    /**
     * Checks that the setSizeUnity() method is correct by comparing values obtained with
     * the getSizeUnity() method. Also checks that method throws an exception when the
     * size is not correct.
     */
    @Test
    public void setSizeUnity() {
        Element.setSizeUnity(50);
        assertEquals(50, Element.getSizeUnity());
        Element.setSizeUnity(42);
        assertEquals(42, Element.getSizeUnity());
        assertThrows(IllegalArgumentException.class, () -> Element.setSizeUnity(-1));
    }
}